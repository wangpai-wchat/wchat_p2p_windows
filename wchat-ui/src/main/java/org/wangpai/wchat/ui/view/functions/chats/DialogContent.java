package org.wangpai.wchat.ui.view.functions.chats;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.external.driver.query.GetMeResponse;
import org.wangpai.wchat.ui.model.external.driver.query.GetSessionDialogDataRequest;
import org.wangpai.wchat.ui.model.external.driver.query.GetSessionDialogDataResponse;
import org.wangpai.wchat.ui.model.functions.chats.MsgDialog;
import org.wangpai.wchat.ui.model.functions.chats.SingleMsg;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;
import org.wangpai.wchat.ui.model.universal.UiDatabase;
import org.wangpai.wchat.ui.view.base.FxController;
import org.wangpai.wchat.ui.view.functions.contacts.friends.Friends;
import org.wangpai.wchat.universal.protocol.internal.ui.SingleTextMsg;
import org.wangpai.wchat.universal.util.time.TimeUtil;

@Accessors(chain = true)
public class DialogContent implements FxController {
    private static DialogContent instance;

    @FXML
    private AnchorPane dialogContent;

    @FXML
    private ListView dialogList;

    @Setter(AccessLevel.NONE) // 此字段禁止自动生成 setter 方法，因为已经有了同名的方法了
    private Label title;

    /**
     * 规定：此 sessionId 与 SessionElement 中的 id 相同
     */
    @Setter
    @Getter
    private String sessionId;

    @Getter(AccessLevel.NONE) // 此字段禁止自动生成 setter 方法，因为已经有了同名的方法了
    private User me;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private boolean cachedMe;

    public DialogContent setTitle(String newTitle) {
        this.title.setText(newTitle);
        return this;
    }

    /**
     * 返回的是单例对象
     *
     * @since 2021-11-5
     */
    public static DialogContent getInstance() {
        if (instance != null) {
            return instance;
        }

        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(DialogContent.class.getResource("DialogContent.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + DialogContent.class);
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
    public static DialogContent getExistingInstance() {
        return instance;
    }

    /**
     * 此方法只有初次绑定时才会有效果
     *
     * @since 2021-11-5
     */
    public DialogContent bindContainer(String parent) {
        var children = UiDatabase.getElementById(parent, AnchorPane.class).getChildren();
        var thisBox = DialogContent.getInstance().getComponent();
        if (!children.contains(thisBox)) {
            children.add(thisBox);
        }

        return this;
    }

    @Override
    public Pane getComponent() {
        return this.dialogContent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 绑定标题组件
        this.title = UiDatabase.getElementById("chatsTitle", Label.class);
    }

    @Override
    public DialogContent afterConfiguration(Object... paras) {
        var request = new GetSessionDialogDataRequest()
                .setSessionId(this.getSessionId())
                .setBegin(0)
                .setUnit(100);
        var response = (GetSessionDialogDataResponse) UiDatabase.getExternalApi().getSessionDialogData(request);
        var dialogData = response.getDialogData();
        if (dialogData != null) {
            var dialogRecords = dialogData.getDialogRecords();
            /**
             * 因为数据库返回的消息记录是按时间降序排列的，所以最新的消息会出现在 dialogRecords 的前部，
             * 而消息显示时，由屏幕从上至下，应该按照时间升序显示。所以需要对 dialogRecords 进行逆置
             */
            Collections.reverse(dialogRecords);
            this.addDialogMsgFully(dialogRecords);
        } else {
            this.addDefaultDialogMsg();
        }

        this.scrollToTheBottom(); // 将滑条滚到底部
        return this;
    }

    /**
     *
     *
     * @since 2021-11-17
     */
    public DialogContent scrollToTheBottom() {
        /**
         * 因为 JavaFX 的 Bug，将滑条置底的方法在滑条第一次出现时不会起作用，第二次才会起作用。
         * 因此，将滑条置底的方法用 Platform.runLater 来包装，这样此方法就相当于在第二次才会调用。
         */
        Platform.runLater(() -> {
            /**
             * 方法 scrollTo 的参数范围为 [0, 列表元素总数 - 1 - 列表框可视元素总数]。
             * 此值以列表上部可视的第一个元素的序号为计。
             * 如果参数范围超出这个值，不会产生异常，而是取端点值
             */
            this.dialogList.scrollTo(this.dialogList.getItems().size());
        });

        return this;
    }

    private User getMe() {
        // 如果 me 已初始化且已开启 me 缓存，则使用缓存数据
        if (this.me != null && this.cachedMe) {
            return this.me;
        }

        var response = (GetMeResponse) UiDatabase.getExternalApi().getMe(null);
        this.me = response.getMe();
        return this.me;
    }

    /**
     * 增量添加。向列表的最下方添加 msg
     *
     * @since 2021-11-6
     */
    public void addDialogMsgIncrementally(SingleMsg msg) {
        var me = this.getMe();
        MsgDialog dialog = null;
        if (me.equals(msg.getUser())) {
            dialog = RightDialog.getInstance()
                    .setName(msg.getUser().getName());
        } else {
            dialog = LeftDialog.getInstance();

        }
        dialog.setTime(msg.getTime())
                .setAvatar(msg.getUser().getAvatar())
                .setMsg(msg.getMsg(), msg.getMsgType());
        this.dialogList.getItems().add(dialog.getComponent());
    }

    /**
     * 增量添加。向列表的最下方添加 msg
     *
     * @since 2022-1-3
     */
    public void addDialogMsgIncrementally(SingleTextMsg msg) {
        var singleMsg = new SingleMsg();
        singleMsg.setUser(Friends.getExistingInstance().searchUserByUserId(msg.getUserId()))
                .setTime(msg.getTime())
                .setMsgType(msg.getMsgType())
                .setMsg(msg.getMsg());
        this.addDialogMsgIncrementally(singleMsg);
    }

    /**
     * 全量添加
     *
     * @since 2021-11-6
     */
    public void addDialogMsgFully(List<SingleMsg> dialogRecords) {
        this.clearAllDialogMsg();
        TimeUtil.setCached(true); // 初始化时，需要多次读取同一时间，所以临时开启时间缓存
        this.setCachedMe(true); // 开启数据库缓存
        for (var msg : dialogRecords) {
            this.addDialogMsgIncrementally(msg);
        }
        TimeUtil.setCached(false); // 初始化结束，关闭时间缓存，避免脏读
        this.setCachedMe(false); // 关闭数据库缓存
    }

    /**
     * 新增一个默认的空 dialog
     *
     * @since 2021-11-17
     */
    private void addDefaultDialogMsg() {
        this.clearAllDialogMsg();
    }

    private void clearAllDialogMsg() {
        if (this.dialogList == null) {
            return;
        }
        this.dialogList.getItems().clear();
    }

    @FXML
    public void onClickDialogContentList(MouseEvent mouseEvent) {
    }
}
