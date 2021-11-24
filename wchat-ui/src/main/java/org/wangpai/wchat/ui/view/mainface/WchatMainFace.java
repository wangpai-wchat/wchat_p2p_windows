package org.wangpai.wchat.ui.view.mainface;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.external.driver.event.OnSendChatMsgEvent;
import org.wangpai.wchat.ui.model.external.driver.query.GetMeResponse;
import org.wangpai.wchat.ui.model.external.driver.query.GetUserDetailsByUserIdRequest;
import org.wangpai.wchat.ui.model.external.driver.query.GetUserDetailsByUserIdResponse;
import org.wangpai.wchat.ui.model.external.hook.WchatUiOnHooks;
import org.wangpai.wchat.ui.model.external.hook.WchatUiQueryHooks;
import org.wangpai.wchat.ui.model.functionbar.FunctionsItem;
import org.wangpai.wchat.ui.model.functionbar.FunctionsManager;
import org.wangpai.wchat.ui.model.functions.chats.SingleMsg;
import org.wangpai.wchat.ui.model.universal.UiDatabase;
import org.wangpai.wchat.ui.model.universal.WchatUtil;
import org.wangpai.wchat.ui.view.base.FxController;
import org.wangpai.wchat.ui.view.functions.chats.ChatsList;
import org.wangpai.wchat.ui.view.functions.chats.DialogContent;
import org.wangpai.wchat.ui.view.functions.contacts.ContactsList;
import org.wangpai.wchat.ui.view.functions.contacts.friends.Friends;
import org.wangpai.wchat.universal.protocol.internal.server.ContactCode;
import org.wangpai.wchat.universal.util.time.TimeMode;
import org.wangpai.wchat.universal.util.time.TimeUtil;

import static org.wangpai.wchat.universal.protocol.internal.ui.MsgType.TEXT;

@Accessors(chain = true)
public class WchatMainFace extends MainFace implements FxController {
    @FXML
    private AnchorPane wchatMainFace;

    @FXML
    private AnchorPane functionBar;

    @FXML
    public Button functionBarAvatar;

    @FXML
    private AnchorPane chats;

    @FXML
    private AnchorPane chatsGroup;

    @FXML
    private Button chatsButton;

    @FXML
    private AnchorPane contacts;

    @FXML
    private AnchorPane contactsGroup;

    @FXML
    private Button contactsButton;

    @FXML
    private TextArea chatsTextInputArea;

    @FXML
    public Label chatsAlertMsg;

    private ChatsList chatsList;

    private ContactsList contactsList;

    @Setter
    private FunctionsItem functionsPressedState = FunctionsItem.NULL;

    public WchatMainFace setAvatar(byte[] avatarData) {
        return this.setFunctionBarAvatar(avatarData);
    }

    private WchatMainFace setFunctionBarAvatar(byte[] avatarData) {
        this.functionBarAvatar.setGraphic(
                WchatUtil.byteArray2ImageView(avatarData, this.functionBarAvatar.getPrefWidth()));
        return this;
    }

    /**
     * 此方法不能手动调用触发，且只能声明为 public
     *
     * @since 2021-10-30
     */
    public WchatMainFace() {
        super();
    }

    @Override
    public AnchorPane getComponent() {
        return this.wchatMainFace;
    }

    public static WchatMainFace getInstance() {
        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(WchatMainFace.class.getResource("WchatMainFace.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + WchatMainFace.class);
            exception.printStackTrace();
        }
        WchatMainFace fxmlController = fxmlLoader.getController();
        node.setUserData(fxmlController);
        return fxmlController;
    }

    public void quit() {
        this.onQuit(null);
        Platform.exit();
    }

    public FunctionsItem whoIsBeingPressed() {
        return this.functionsPressedState;
    }

    private void updateIconsState(FunctionsItem function, String newIconPath) {
        if (this.whoIsBeingPressed() == function) {
            return;
        } else {
            var recent = this.whoIsBeingPressed();
            if (recent != null && recent != FunctionsItem.NULL) {
                // 将 Style 设置为空串时，JavaFX 会将之前调用方法 setStyle 产生的所有效果都消除
                this.getButtonByEnum(recent).setStyle("");
            }
            this.getButtonByEnum(function).setStyle(String.format("-fx-background-image: url('%s')", newIconPath));
            this.setFunctionsPressedState(function);
        }
    }

    private Button getButtonByEnum(FunctionsItem function) {
        return switch (function) {
            case CHATS -> this.chatsButton;
            case CONTACTS -> this.contactsButton;
            case NULL -> null;
            default -> null; // 敬请期待
        };
    }

    @Override
    public WchatMainFace afterConfiguration(Object... paras) {
        return this.afterConfiguration((WchatUiOnHooks) paras[0], (WchatUiQueryHooks) paras[1]);
    }

    public WchatMainFace afterConfiguration(WchatUiOnHooks onHooks, WchatUiQueryHooks requestHooks) {
        this.initFirst(onHooks, requestHooks);

        this.initContacts(); // 它要先于 Chats 组件的初始化
        this.initChats();
        this.initMore(); // 敬请期待
        this.initMe();

        this.initFunctionsManager(); // 它不能先于各组件的初始化而初始化
        this.initLast();
        return this;
    }

    private void initFirst(WchatUiOnHooks onHooks, WchatUiQueryHooks queryHooks) {
        TimeUtil.setTimeMode(TimeMode.OFFLINE);
        TimeUtil.setCached(true); // 初始化时，需要多次读取同一时间，所以临时开启时间缓存
        this.setWchatUiOnHooks(onHooks).setWchatUiQueryHooks(queryHooks);
        UiDatabase.setRoot(this.getComponent());
        UiDatabase.setExternalApi(this);
    }

    private void initContacts() {
        this.contactsList = ContactsList.getInstance().afterConfiguration();
        this.contactsGroup.getChildren().add(this.contactsList.getComponent());
    }

    private void initChats() {
        this.chatsList = ChatsList.getInstance();
        this.chatsGroup.getChildren().add(this.chatsList.getComponent());
    }

    private void initMore() {
        // 敬请期待
    }

    private void initMe() {
        var me = ((GetMeResponse) this.getMe(null)).getMe();
        this.setAvatar(me.getAvatar());
    }

    private void initFunctionsManager() {
        FunctionsManager.put(FunctionsItem.CHATS, this.chats);
        FunctionsManager.put(FunctionsItem.CONTACTS, this.contacts);
    }

    private void initLast() {
        // 最开始默认显示聊天界面
        this.onActionChats(null);
        TimeUtil.setCached(false); // 初始化结束，关闭时间缓存，避免脏读
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 暂时不需要实现
    }

    /**
     * 点击头像触发更改头像
     *
     * @since 2021-11-24
     */
    @FXML
    public void onActionFunctionBarAvatar(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("请选择一张图片");
        // 设置打开初始地址
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // 可选的文件类型
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("图片", "*.jpg", "*.png"));
        File file = fileChooser.showOpenDialog(this.wchatMainFace.getScene().getWindow());

        // 如果用户取消选择，不作任何事情
        if (file == null) {
            return;
        }

        var avatarData = new byte[0];
        try {
            avatarData = WchatUtil.file2byteArray(file);
        } catch (IOException e) {
            e.printStackTrace(); // TODO：处理异常
        }
        this.setAvatar(avatarData);

        // 向后台触发更新头像的回调
//        var wchatEvent = WchatEvent.getInstance();
//        wchatEvent.setData(WchatEventCarrierType.DATA_TYPE, WchatEventCarrierType.BINARY);
//        wchatEvent.setData(WchatEventCarrierType.BINARY, avatarData);
//        this.onUpdateAvatar(wchatEvent); // FIXME

        UiDatabase.forceUpdateMainFace(); // 更新头像后，强制更新 UI 界面

        System.out.println("onActionFunctionBarAvatar："
                + this.functionBarAvatar.getWidth() + ", " + this.functionBarAvatar.getHeight());
    }

    @FXML
    public void onActionChats(ActionEvent actionEvent) {
        System.out.println("onClickChatsButton");

        FunctionsItem thisFunction = FunctionsItem.CHATS;
        // 如果本按钮是当前被点击的按钮，本回调不做任何事情
        if (this.whoIsBeingPressed() == thisFunction) {
            return;
        }
        this.updateIconsState(thisFunction, "/system/icons/mainface/chats-pressed.png");
        FunctionsManager.switchFunctions(thisFunction);
    }

    /**
     * 当光标接触本按钮时，会触发此回调
     *
     * @since 2021-11-14
     */
    @FXML
    public void onMouseEnteredChats(MouseEvent mouseEvent) {
        // 如果本按钮是当前被点击的按钮，本回调不做任何事情
        System.out.println("onMouseEnteredChats");
        if (this.whoIsBeingPressed() == FunctionsItem.CHATS) {
            return;
        }

        this.chatsButton.setStyle("-fx-background-image: url('/system/icons/mainface/chats-hover.png')");
    }

    /**
     * 当光标离开本按钮时，会触发此回调
     *
     * @since 2021-11-14
     */
    @FXML
    public void onMouseExitedChats(MouseEvent mouseEvent) {
        // 如果本按钮是当前被点击的按钮，本回调不做任何事情
        if (this.whoIsBeingPressed() == FunctionsItem.CHATS) {
            return;
        }

        // 将 Style 设置为空串时，JavaFX 会将之前调用方法 setStyle 产生的所有效果都消除
        this.chatsButton.setStyle("");
    }

    @FXML
    public void onActionContacts(ActionEvent actionEvent) {
        System.out.println("onClickContactsButton");

        FunctionsItem thisFunction = FunctionsItem.CONTACTS;
        // 如果本按钮是当前被点击的按钮，本回调不做任何事情
        if (this.whoIsBeingPressed() == thisFunction) {
            return;
        }
        this.updateIconsState(thisFunction, "/system/icons/mainface/contacts-pressed.png");
        FunctionsManager.switchFunctions(thisFunction);
    }

    @FXML
    public void onMouseEnteredContacts(MouseEvent mouseEvent) {
        System.out.println("onDragEnteredContacts");

        // 如果本按钮是当前被点击的按钮，本回调不做任何事情
        if (this.whoIsBeingPressed() == FunctionsItem.CONTACTS) {
            return;
        }
        this.contactsButton.setStyle("-fx-background-image: url('/system/icons/mainface/contacts-hover.png')");
    }

    @FXML
    public void onMouseExitedContacts(MouseEvent mouseEvent) {
        System.out.println("onDragExitedContacts");

        // 如果本按钮是当前被点击的按钮，本回调不做任何事情
        if (this.whoIsBeingPressed() == FunctionsItem.CONTACTS) {
            return;
        }

        // 将 Style 设置为空串时，JavaFX 会将之前调用方法 setStyle 产生的所有效果都消除
        this.contactsButton.setStyle("");
    }

    @FXML
    public void onActionMore(ActionEvent event) {
        // 敬请期待
    }

    @FXML
    public void onMouseEnteredMore(MouseEvent mouseEvent) {
        // 敬请期待
    }

    @FXML
    public void onMouseExitedMore(MouseEvent mouseEvent) {
        // 敬请期待
    }

    /**
     * 设置 ENTER 触发发送信息，但不触发文本换行。设置 CTRL+ENTER 触发发送信息
     *
     * @since 2021-11-22
     */
    @FXML
    public void onKeyPressedInputArea(KeyEvent keyEvent) {
        // 如果按下了回车键
        if (keyEvent.getCode() == KeyCode.ENTER) {
            // 获得此时的光标位置。此位置为刚刚输入的换行符之后
            var caretPosition = this.chatsTextInputArea.getCaretPosition();

            // 如果已经按下的按键中包含 Control 键
            if (!keyEvent.isControlDown()) { // 如果输入的不是组合键 CTRL+ENTER，去掉换行符，然后将文本发送
                // 获得输入文本，此文本包含刚刚输入的换行符
                var text = this.chatsTextInputArea.getText();
                // 获得换行符两边的文本
                var front = text.substring(0, caretPosition - 1);
                var end = text.substring(caretPosition);
                this.chatsTextInputArea.setText(front + end);
                this.onClickedChatsSend(null); // 发送信息
            } else {
                // 获得输入文本，此文本不包含刚刚输入的换行符
                var text = this.chatsTextInputArea.getText();
                // 获得光标两边的文本
                var front = text.substring(0, caretPosition);
                var end = text.substring(caretPosition);
                /**
                 * 注意：Windows 会将回车解释成 `\n\r`，但 TextArea 清除文本中所有的 `\r`。
                 * 换句话说，当在 Windows 输入回车时，实际上输入的是 `\n\r`。
                 * 但当向 TextArea 输入 `\n\r` 时，TextArea 会移除所有的 `\r`。
                 * 从 TextArea 得到的字符串中不会包含任何 `\r`
                 */
                this.chatsTextInputArea.setText(front + System.lineSeparator() + end); // 在光标处插入换行符
                // 将光标移至换行符
                this.chatsTextInputArea.positionCaret(caretPosition + 1);
            }
        }
    }

    @FXML
    public void onClickedChatsSend(MouseEvent mouseEvent) {
        this.chatsTextInputArea.requestFocus();

        var textEntered = this.chatsTextInputArea.getText();
        // 如果用户没有输入就点发送，将显示一条文本告警。此告警将自动于 2.5s 之后消失
        if (textEntered == null || Objects.equals(textEntered, "")) {
            this.chatsAlertMsg.setVisible(true);

            KeyFrame action = new KeyFrame(Duration.millis(2500), event -> {
                this.chatsAlertMsg.setVisible(false);
            });
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(action);
            timeline.setAutoReverse(false);
            timeline.setCycleCount(1);
            timeline.play();

            return;
        }

        var dialogContent = DialogContent.getExistingInstance();
        var sessionId = dialogContent.getSessionId();

        var request = new GetUserDetailsByUserIdRequest().setUserId(sessionId);
        var response = (GetUserDetailsByUserIdResponse) UiDatabase.getExternalApi().getUserDetailsByUserId(request);
        var contactCode = new ContactCode()
                .setMyContactCode(response.getMyContactCode())
                .setOtherPartyContactCode(response.getOtherPartyContactCode());
        var time = TimeUtil.getTime();
        var me = ((GetMeResponse) this.getMe(null)).getMe();

        // 向 center 发送信息
        var onSendChatMsgEvent = new OnSendChatMsgEvent();
        onSendChatMsgEvent.setSessionId(sessionId)
                .setUserId(me.getId())
                .setContactCode(contactCode)
                .setMsgType(TEXT) // 暂时只支持发送文本信息
                .setMsg(textEntered)
                .setTime(time);

        this.onSendChatMsg(onSendChatMsgEvent);

        // 更新 UI 界面
        var msg = new SingleMsg();
        msg.setUser(me)
                .setMsgType(TEXT)
                .setMsg(textEntered)
                .setTime(time);
        dialogContent.addDialogMsgIncrementally(msg);

        // 善后处理
        dialogContent.scrollToTheBottom();
        this.chatsTextInputArea.clear();
        this.chatsTextInputArea.requestFocus();
    }

    @FXML
    public Label chatsChatInfo;

    @FXML
    public void onMouseClickedChatsInfo(MouseEvent mouseEvent) {
        var sessionElement = ChatsList.getExistingInstance().getBeingSelected();
        var friendsElement = Friends.getExistingInstance().getElementByUserId(sessionElement.getId());

        this.onActionContacts(null);
        friendsElement.onClickFriendsElement(null);
    }

    @FXML
    public void onActionMinimize(ActionEvent event) {
        ((Stage) this.wchatMainFace.getScene().getWindow()).setIconified(true);
    }

    @FXML
    public void onActionMaximize(ActionEvent event) {
        System.out.println("onActionMaximize clicked");

        // 敬请期待
    }

    @FXML
    public void onActionClose(ActionEvent event) {
        this.quit();
    }

}
