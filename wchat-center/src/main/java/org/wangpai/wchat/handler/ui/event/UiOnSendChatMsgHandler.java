package org.wangpai.wchat.handler.ui.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.client.external.api.ClientApi;
import org.wangpai.wchat.persistence.complex.dao.SessionDao;
import org.wangpai.wchat.persistence.origin.domain.SessionsDialogData;
import org.wangpai.wchat.persistence.origin.mapper.SessionsDialogDataMapper;
import org.wangpai.wchat.ui.model.external.driver.event.OnSendChatMsgEvent;
import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;
import org.wangpai.wchat.universal.handler.EventHandler;
import org.wangpai.wchat.universal.protocol.internal.server.DataUnit;
import org.wangpai.wchat.universal.protocol.internal.server.Identifier;
import org.wangpai.wchat.universal.protocol.internal.ui.MsgType;
import org.wangpai.wchat.universal.util.id.IdUtil;

/**
 * @since 2022-1-5
 */
@Service
public class UiOnSendChatMsgHandler implements EventHandler {
    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private SessionsDialogDataMapper sessionsDialogDataMapper;

    @Override
    public WchatFeedback handle(WchatEvent OnSendChatMsgEvent) {
        var event = (OnSendChatMsgEvent) OnSendChatMsgEvent;

        MsgType msgType = event.getMsgType();
        switch (msgType) {
            case TEXT -> {
                var clientConfig = this.sessionDao.getClientConfig(event.getSessionId());
                var contactCode = event.getContactCode();
                var msg = (String) event.getMsg();
                var time = event.getTime();
                var dataUnit = new DataUnit()
                        .setIdentifier(Identifier.CHAT_MSG_TEXT)
                        .setData(msg);
                ClientApi.send(dataUnit, clientConfig, contactCode, time); // 对外发信息

                var sessionsDialogData = new SessionsDialogData()
                        .setId(IdUtil.idGenerator())
                        .setUserId(event.getUserId())
                        .setSessionId(event.getSessionId())
                        .setTime(time)
                        .setMsgType(event.getMsgType())
                        .setTextMsg(msg);
                this.sessionsDialogDataMapper.save(sessionsDialogData); // 更新数据库
            }
        }

        return null;
    }
}
