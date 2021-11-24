package org.wangpai.wchat.persistence.complex.repository;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wangpai.wchat.persistence.complex.dao.SessionDao;
import org.wangpai.wchat.persistence.complex.dao.SessionsDialogDataDao;
import org.wangpai.wchat.ui.model.external.driver.query.GetAllSessionsDialogDataRequest;
import org.wangpai.wchat.ui.model.external.driver.query.GetAllSessionsDialogDataResponse;
import org.wangpai.wchat.ui.model.external.driver.query.GetSessionDialogDataRequest;
import org.wangpai.wchat.ui.model.external.driver.query.GetSessionDialogDataResponse;
import org.wangpai.wchat.ui.model.functions.chats.SingleMsg;
import org.wangpai.wchat.ui.model.functions.chats.SingleSessionDialogData;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;

@Repository
public class SessionsDialogDataRepository {
    @Autowired
    private SessionsDialogDataDao sessionsDialogDataDao;

    @Autowired
    private SessionDao sessionDao;

    /**
     * 就算要查询的 Session 没有任何消息记录，也会返回一个不为 null 的对象，只是其中关于消息记录的列表的长度为 0（也不为 null）。
     * 换句话说，只要有这个 Session，就能得到这个对象。
     * 如果没有这个 Session，GetSessionDialogDataResponse 中的 dialogData 会为 null
     *
     * @since 2022-1-11
     */
    public GetSessionDialogDataResponse getSessionDialogData(GetSessionDialogDataRequest request) {
        var singleDialogDataList = this.sessionsDialogDataDao.getSessionDialogData(request);
        var singleMsgList = new ArrayList<SingleMsg>(singleDialogDataList.size());
        for (var singleDialogData : singleDialogDataList) {
            var user = new User()
                    .setId(singleDialogData.getUserId())
                    .setIp(singleDialogData.getIp())
                    .setPort(singleDialogData.getPort())
                    .setNickname(singleDialogData.getNickname())
                    .setAvatar(singleDialogData.getAvatar());

            var singleMsg = new SingleMsg()
                    .setUser(user)
                    .setTime(singleDialogData.getTime())
                    .setMsgType(singleDialogData.getMsgType());
            switch (singleDialogData.getMsgType()) {
                case TEXT -> singleMsg.setMsg(singleDialogData.getTextMsg());
                case BINARY -> singleMsg.setMsg(singleDialogData.getBinaryMsg());

                default -> {
                    // 敬请期待
                }
            }

            singleMsgList.add(singleMsg);
        }

        var sessionInfo = this.sessionDao.getSessionInfo(request.getSessionId());
        SingleSessionDialogData singleSessionDialogData = null;
        // 如果连这个 Session 也没有，直接返回一个字段为 null 的对象。如果不为空，构造需要返回的数据
        if (sessionInfo != null) {
            singleSessionDialogData = new SingleSessionDialogData()
                    .setSessionId(sessionInfo.getSessionId())
                    .setAvatar(sessionInfo.getAvatar())
                    .setName(sessionInfo.getNickname())
                    .setMsgReminderCounter(sessionInfo.getMsgReminderCounter())
                    .setDisturb(sessionInfo.isDisturb())
                    .setDialogRecords(singleMsgList);
        }

        return new GetSessionDialogDataResponse().setDialogData(singleSessionDialogData);
    }

    public GetAllSessionsDialogDataResponse getAllSessionsDialogData(GetAllSessionsDialogDataRequest getAllSessionsDialogDataRequest) {
        var sessions = this.sessionDao.getAllSessionsInfo();
        var dialogDataList = new ArrayList<SingleSessionDialogData>(sessions.size());
        var begin = getAllSessionsDialogDataRequest.getBegin();
        var unit = getAllSessionsDialogDataRequest.getUnit();
        for (var session : sessions) {
            var request = new GetSessionDialogDataRequest()
                    .setBegin(begin)
                    .setUnit(unit)
                    .setSessionId(session.getSessionId());
            var getSessionDialogDataResponse = this.getSessionDialogData(request);
            dialogDataList.add(getSessionDialogDataResponse.getDialogData());
        }

        return new GetAllSessionsDialogDataResponse().setDialogDataList(dialogDataList);
    }
}
