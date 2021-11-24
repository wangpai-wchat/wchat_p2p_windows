package org.wangpai.wchat.ui.model.external.api;

import javafx.application.Platform;
import org.wangpai.wchat.ui.model.functions.chats.SingleSessionDialogData;
import org.wangpai.wchat.ui.view.functions.chats.ChatsList;
import org.wangpai.wchat.ui.view.functions.chats.DialogContent;
import org.wangpai.wchat.universal.protocol.internal.ui.SingleTextMsg;

/**
 * 本类方法的实现必须使用方法 Platform.runLater 来包裹
 *
 * @since 2021-12-28
 */
public class UiApi {
    /**
     * 向 Ui 反馈收到的新消息
     *
     * @since 2021-11-28
     */
    @Deprecated
    public static void addNewMsg(SingleSessionDialogData sessionData) {
        Platform.runLater(() -> {
            var sessionId = sessionData.getSessionId();
            var sessionElement = ChatsList.getExistingInstance().getElementBySessionId(sessionId);
            if (sessionElement == null) { // 如果没有找到，则新建一个 SessionElement
                var chatsList = ChatsList.getExistingInstance();
                sessionElement = chatsList.addDefaultSession(sessionId);
            }
            var dialogRecords = sessionData.getDialogRecords();
            var recentData = dialogRecords == null ? null : dialogRecords.get(0);
            // 更新 session 组件的数据
            sessionElement.setTime(recentData.getTime());

            switch (recentData.getMsgType()) {
                case TEXT -> {
                    sessionElement.setMsgDigest((String) recentData.getMsg());
                }

                default -> {
                    sessionElement.setMsgDigest(""); // 敬请期待
                }
            }

            if (sessionElement.isSelected()) { // 如果新消息所对应的会话是正在打开的会话，更新会话窗口
                DialogContent.getExistingInstance().addDialogMsgFully(dialogRecords);
            } else { // 如果不是正在打开的会话，将未读消息数加上新消息的条数
                sessionElement.setMsgReminder(sessionElement.getMsgReminderCounter() + dialogRecords.size());
            }
        });

    }

    /**
     * 向 Ui 反馈收到的新消息
     *
     * @since 2021-11-28
     */
    public static void addNewMsg(SingleTextMsg msg) {
        Platform.runLater(() -> {
            var sessionId = msg.getSessionId();
            // 如果 sessionId 为 null，则视 sessionId 为 userId
            sessionId = sessionId == null ? msg.getUserId() : sessionId;
            var sessionElement = ChatsList.getExistingInstance().getElementBySessionId(sessionId);
            if (sessionElement == null) { // 如果没有找到，则新建一个 SessionElement
                var chatsList = ChatsList.getExistingInstance();
                sessionElement = chatsList.addDefaultSession(sessionId);
            }

            // 更新 session 组件的数据
            sessionElement.setTime(msg.getTime());

            switch (msg.getMsgType()) {
                case TEXT -> {
                    sessionElement.setMsgDigest((String) msg.getMsg());
                }

                default -> {
                    sessionElement.setMsgDigest(""); // 敬请期待
                }
            }

            if (sessionElement.isSelected()) { // 如果新消息所对应的会话是正在打开的会话，更新会话窗口
                DialogContent.getExistingInstance().addDialogMsgIncrementally(msg);
            } else { // 如果不是正在打开的会话，将未读消息数加 1
                sessionElement.setMsgReminder(sessionElement.getMsgReminderCounter() + 1);
            }
        });
    }
}
