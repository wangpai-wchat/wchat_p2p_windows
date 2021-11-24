package org.wangpai.wchat.ui.view.functions.chats;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import lombok.Getter;
import lombok.Setter;
import org.wangpai.wchat.ui.model.external.driver.event.OnAddSessionEvent;
import org.wangpai.wchat.ui.model.external.driver.query.GetAllSessionsDialogDataRequest;
import org.wangpai.wchat.ui.model.external.driver.query.GetAllSessionsDialogDataResponse;
import org.wangpai.wchat.ui.model.functions.chats.SessionMode;
import org.wangpai.wchat.ui.model.functions.chats.SingleSessionDialogData;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;
import org.wangpai.wchat.ui.model.universal.UiDatabase;
import org.wangpai.wchat.ui.view.base.FxController;
import org.wangpai.wchat.ui.view.functions.contacts.friends.Friends;
import org.wangpai.wchat.universal.util.time.TimeUtil;

public class ChatsList implements FxController {
    @FXML
    private ListView chatsList;

    private static ChatsList instance;

    /**
     * 最近被选择的会话项
     *
     * @since 2022-1-6
     */
    @Setter
    @Getter
    private SessionElement beingSelected;

    public static ChatsList getInstance() {
        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(ChatsList.class.getResource("ChatsList.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + ChatsList.class);
            exception.printStackTrace();
        }
        instance = fxmlLoader.getController();
        node.setUserData(instance);
        return instance;
    }

    /**
     * 当需要本类对象的代码并不想初始化本类对象时，可以调用本方法
     *
     * @since 2021-11-28
     */
    public static ChatsList getExistingInstance() {
        return instance;
    }

    @Override
    public ListView getComponent() {
        return this.chatsList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 只查询最近 1 条的数据
        var request = new GetAllSessionsDialogDataRequest()
                .setBegin(0)
                .setUnit(1);
        var response = (GetAllSessionsDialogDataResponse) UiDatabase.getExternalApi().getAllSessionsDialogData(request);
        var dataList = response.getDialogDataList();
        if (dataList == null) {
            return;
        }

        this.addSessions(dataList);
    }

    /**
     * 根据序号 location，在 ListView 中新增 Session 数据。
     *
     * @param location 要插入的 Session 数据位置。序号从 0 开始
     * @return 返回刚刚新建的 SessionElement
     * @since 2021-11-16
     */
    public SessionElement addSession(int location, SingleSessionDialogData sessionData) {
        var sessionElement = SessionElement.getInstance();
        sessionElement.setSource(this)
                .setId(sessionData.getSessionId())
                .setAvatar(sessionData.getAvatar())
                .setName(sessionData.getName())
                .setMsgReminder(sessionData.getMsgReminderCounter())
                .setDisturb(sessionData.isDisturb());

        var dialogRecords = sessionData.getDialogRecords();
        if (dialogRecords != null && dialogRecords.size() > 0) {
            var recentData = dialogRecords.get(0);
            sessionElement.setTime(recentData.getTime());
            switch (recentData.getMsgType()) {
                case TEXT -> {
                    sessionElement.setMsgDigest((String) recentData.getMsg());
                }

                default -> {
                    sessionElement.setMsgDigest(""); // 敬请期待
                }
            }
        } else {
            sessionElement.setMsgDigest("")
                    .setTime(TimeUtil.getTime());
        }

        this.chatsList.getItems().add(location, sessionElement.getComponent());
        return sessionElement;
    }

    /**
     * 在 ListView 尾部新增 Session 数据
     *
     * @since 2021-11-16
     */
    public SessionElement addSession(SingleSessionDialogData sessionData) {
        return this.addSession(this.chatsList.getItems().size(), sessionData);
    }

    public void addSessions(List<SingleSessionDialogData> sessionDataList) {
        for (var sessionData : sessionDataList) {
            this.addSession(sessionData);
        }
    }

    /**
     * 新增一个默认的空 Session，此 Session 会置顶
     *
     * @since 2021-11-17
     */
    public SessionElement addDefaultSession(User user) {
        var dialogData = new SingleSessionDialogData();
        dialogData.setSessionId(user.getId())
                .setAvatar(user.getAvatar())
                .setName(user.getName())
                .setDialogRecords(null)
                .setMsgReminderCounter(0)
                .setDisturb(true);
        return this.addSession(0, dialogData);
    }

    /**
     * 新增一个默认的空 Session，此 Session 会置顶
     *
     * 此方法会在数据库中也新建一个 Session
     *
     * @since 2021-11-28
     */
    public SessionElement addDefaultSession(String sessionId) {
        var user = Friends.getExistingInstance().searchUserByUserId(sessionId);

        var event = new OnAddSessionEvent()
                .setSessionId(sessionId)
                .setMode(SessionMode.PAIR)
                .setUserId(user.getId())
                .setDisturb(false)
                .setMsgReminderCounter(0);
        UiDatabase.getExternalApi().onAddSession(event);

        var dialogData = new SingleSessionDialogData();
        dialogData.setSessionId(sessionId)
                .setAvatar(user.getAvatar())
                .setName(user.getName())
                .setDialogRecords(null)
                .setMsgReminderCounter(0)
                .setDisturb(true);
        return this.addSession(0, dialogData);
    }

    /**
     * 此方法只应由其子组件调用
     *
     * @since 2021-11-28
     */
    public void updateSelectedState(SessionElement selected) {
        // 设置 ListView 中的选中效果
        this.chatsList.getSelectionModel().select(selected.getComponent());

        // 取消其它子组件的选中标记
        var items = this.chatsList.getItems();
        for (var item : items) {
            var child = (SessionElement) ((Node) item).getUserData();
            if (child == selected) {
                child.setSelected(true);
            } else {
                child.setSelected(false);
            }
        }

        this.beingSelected = selected;
    }

    /**
     * 通过 sessionId 来从 ChatsList 中寻找子组件
     *
     * @return 如果找到了，返回该组件。如果 sessionId 不正确，返回 null
     * @since 2021-11-28
     */
    public SessionElement getElementBySessionId(String sessionId) {
        var chatsListItems = this.chatsList.getItems();
        SessionElement sessionElement = null;
        for (var item : chatsListItems) {
            sessionElement = ((SessionElement) ((Node) item).getUserData());
            if (sessionElement.getId().equals(sessionId)) {
                return sessionElement;
            }
        }

        return null;
    }
}
