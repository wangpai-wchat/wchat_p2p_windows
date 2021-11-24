package org.wangpai.wchat.handler.server.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.persistence.complex.dao.SessionDao;
import org.wangpai.wchat.persistence.complex.dao.UserDao;
import org.wangpai.wchat.persistence.origin.domain.SessionsDialogData;
import org.wangpai.wchat.persistence.origin.mapper.SessionsDialogDataMapper;
import org.wangpai.wchat.server.external.driver.event.OnReceiveEvent;
import org.wangpai.wchat.ui.model.external.api.UiApi;
import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;
import org.wangpai.wchat.universal.handler.EventHandler;
import org.wangpai.wchat.universal.protocol.internal.ui.SingleTextMsg;
import org.wangpai.wchat.universal.util.id.IdUtil;

import static org.wangpai.wchat.universal.protocol.internal.ui.MsgType.TEXT;

@Service
public class ServerOnReceiveHandler implements EventHandler {
    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private SessionsDialogDataMapper sessionsDialogDataMapper;

    @Override
    public WchatFeedback handle(WchatEvent onReceiveEvent) {
        var event = (OnReceiveEvent) onReceiveEvent;
        var dataUnit = event.getDataUnit();

        switch (dataUnit.getIdentifier()) {
            case CHAT_MSG_TEXT -> {
                var msg = (String) dataUnit.getData();
                var clientConfig = event.getClientConfig();
                var user = this.userDao.getUserByClientConfig(clientConfig);
                var result = this.sessionDao.getSessionId(clientConfig);

                /**
                 * 此处如果 sessionId 为 null，代表会话中还没有这个联系人的信息
                 * 如果 sessionId 为 null，目前暂由 userId 顶替
                 */
                String sessionId = result == null ? user.getId() : result.getSessionId();
                var userId = user.getId();
                var time = event.getDateTime();
                var singleTextMsg = new SingleTextMsg()
                        .setSessionId(sessionId)
                        .setUserId(userId)
                        .setTime(time)
                        .setMsgType(TEXT)
                        .setMsg(msg);
                UiApi.addNewMsg(singleTextMsg);

                /**
                 * 注意：此处不能选择先更新数据库。因为如果 sessionId 不存在，则需要 UI 先在数据库中创建这个 Session
                 */
                var sessionsDialogData = new SessionsDialogData()
                        .setId(IdUtil.idGenerator())
                        .setUserId(userId)
                        .setSessionId(sessionId)
                        .setTime(time)
                        .setMsgType(TEXT)
                        .setTextMsg(msg);
                this.sessionsDialogDataMapper.save(sessionsDialogData); // 更新数据库
            }

            default -> {
                // 此 case 不应该发生
            }
        }

        return null;
    }
}
