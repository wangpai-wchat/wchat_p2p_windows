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
     * ?????????????????????????????????????????????????????? public
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
         * ???????????????????????? resources ??? XXX.class ???????????? ?????? ??? ??? ????????????????????????????????????
         * ?????????????????? XXX ????????????????????? xxx?????????????????????????????????????????? resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(WchatMainFace.class.getResource("WchatMainFace.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // ???????????????????????? load???????????????????????? getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() ????????????...." + WchatMainFace.class);
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
                // ??? Style ?????????????????????JavaFX ???????????????????????? setStyle ??????????????????????????????
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
            default -> null; // ????????????
        };
    }

    @Override
    public WchatMainFace afterConfiguration(Object... paras) {
        return this.afterConfiguration((WchatUiOnHooks) paras[0], (WchatUiQueryHooks) paras[1]);
    }

    public WchatMainFace afterConfiguration(WchatUiOnHooks onHooks, WchatUiQueryHooks requestHooks) {
        this.initFirst(onHooks, requestHooks);

        this.initContacts(); // ???????????? Chats ??????????????????
        this.initChats();
        this.initMore(); // ????????????
        this.initMe();

        this.initFunctionsManager(); // ????????????????????????????????????????????????
        this.initLast();
        return this;
    }

    private void initFirst(WchatUiOnHooks onHooks, WchatUiQueryHooks queryHooks) {
        TimeUtil.setTimeMode(TimeMode.OFFLINE);
        TimeUtil.setCached(true); // ??????????????????????????????????????????????????????????????????????????????
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
        // ????????????
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
        // ?????????????????????????????????
        this.onActionChats(null);
        TimeUtil.setCached(false); // ???????????????????????????????????????????????????
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ?????????????????????
    }

    /**
     * ??????????????????????????????
     *
     * @since 2021-11-24
     */
    @FXML
    public void onActionFunctionBarAvatar(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("?????????????????????");
        // ????????????????????????
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // ?????????????????????
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("??????", "*.jpg", "*.png"));
        File file = fileChooser.showOpenDialog(this.wchatMainFace.getScene().getWindow());

        // ?????????????????????????????????????????????
        if (file == null) {
            return;
        }

        var avatarData = new byte[0];
        try {
            avatarData = WchatUtil.file2byteArray(file);
        } catch (IOException e) {
            e.printStackTrace(); // TODO???????????????
        }
        this.setAvatar(avatarData);

        // ????????????????????????????????????
//        var wchatEvent = WchatEvent.getInstance();
//        wchatEvent.setData(WchatEventCarrierType.DATA_TYPE, WchatEventCarrierType.BINARY);
//        wchatEvent.setData(WchatEventCarrierType.BINARY, avatarData);
//        this.onUpdateAvatar(wchatEvent); // FIXME

        UiDatabase.forceUpdateMainFace(); // ?????????????????????????????? UI ??????

        System.out.println("onActionFunctionBarAvatar???"
                + this.functionBarAvatar.getWidth() + ", " + this.functionBarAvatar.getHeight());
    }

    @FXML
    public void onActionChats(ActionEvent actionEvent) {
        System.out.println("onClickChatsButton");

        FunctionsItem thisFunction = FunctionsItem.CHATS;
        // ????????????????????????????????????????????????????????????????????????
        if (this.whoIsBeingPressed() == thisFunction) {
            return;
        }
        this.updateIconsState(thisFunction, "/system/icons/mainface/chats-pressed.png");
        FunctionsManager.switchFunctions(thisFunction);
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @since 2021-11-14
     */
    @FXML
    public void onMouseEnteredChats(MouseEvent mouseEvent) {
        // ????????????????????????????????????????????????????????????????????????
        System.out.println("onMouseEnteredChats");
        if (this.whoIsBeingPressed() == FunctionsItem.CHATS) {
            return;
        }

        this.chatsButton.setStyle("-fx-background-image: url('/system/icons/mainface/chats-hover.png')");
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @since 2021-11-14
     */
    @FXML
    public void onMouseExitedChats(MouseEvent mouseEvent) {
        // ????????????????????????????????????????????????????????????????????????
        if (this.whoIsBeingPressed() == FunctionsItem.CHATS) {
            return;
        }

        // ??? Style ?????????????????????JavaFX ???????????????????????? setStyle ??????????????????????????????
        this.chatsButton.setStyle("");
    }

    @FXML
    public void onActionContacts(ActionEvent actionEvent) {
        System.out.println("onClickContactsButton");

        FunctionsItem thisFunction = FunctionsItem.CONTACTS;
        // ????????????????????????????????????????????????????????????????????????
        if (this.whoIsBeingPressed() == thisFunction) {
            return;
        }
        this.updateIconsState(thisFunction, "/system/icons/mainface/contacts-pressed.png");
        FunctionsManager.switchFunctions(thisFunction);
    }

    @FXML
    public void onMouseEnteredContacts(MouseEvent mouseEvent) {
        System.out.println("onDragEnteredContacts");

        // ????????????????????????????????????????????????????????????????????????
        if (this.whoIsBeingPressed() == FunctionsItem.CONTACTS) {
            return;
        }
        this.contactsButton.setStyle("-fx-background-image: url('/system/icons/mainface/contacts-hover.png')");
    }

    @FXML
    public void onMouseExitedContacts(MouseEvent mouseEvent) {
        System.out.println("onDragExitedContacts");

        // ????????????????????????????????????????????????????????????????????????
        if (this.whoIsBeingPressed() == FunctionsItem.CONTACTS) {
            return;
        }

        // ??? Style ?????????????????????JavaFX ???????????????????????? setStyle ??????????????????????????????
        this.contactsButton.setStyle("");
    }

    @FXML
    public void onActionMore(ActionEvent event) {
        // ????????????
    }

    @FXML
    public void onMouseEnteredMore(MouseEvent mouseEvent) {
        // ????????????
    }

    @FXML
    public void onMouseExitedMore(MouseEvent mouseEvent) {
        // ????????????
    }

    /**
     * ?????? ENTER ?????????????????????????????????????????????????????? CTRL+ENTER ??????????????????
     *
     * @since 2021-11-22
     */
    @FXML
    public void onKeyPressedInputArea(KeyEvent keyEvent) {
        // ????????????????????????
        if (keyEvent.getCode() == KeyCode.ENTER) {
            // ????????????????????????????????????????????????????????????????????????
            var caretPosition = this.chatsTextInputArea.getCaretPosition();

            // ???????????????????????????????????? Control ???
            if (!keyEvent.isControlDown()) { // ?????????????????????????????? CTRL+ENTER??????????????????????????????????????????
                // ????????????????????????????????????????????????????????????
                var text = this.chatsTextInputArea.getText();
                // ??????????????????????????????
                var front = text.substring(0, caretPosition - 1);
                var end = text.substring(caretPosition);
                this.chatsTextInputArea.setText(front + end);
                this.onClickedChatsSend(null); // ????????????
            } else {
                // ???????????????????????????????????????????????????????????????
                var text = this.chatsTextInputArea.getText();
                // ???????????????????????????
                var front = text.substring(0, caretPosition);
                var end = text.substring(caretPosition);
                /**
                 * ?????????Windows ????????????????????? `\n\r`?????? TextArea ???????????????????????? `\r`???
                 * ????????????????????? Windows ??????????????????????????????????????? `\n\r`???
                 * ????????? TextArea ?????? `\n\r` ??????TextArea ?????????????????? `\r`???
                 * ??? TextArea ??????????????????????????????????????? `\r`
                 */
                this.chatsTextInputArea.setText(front + System.lineSeparator() + end); // ???????????????????????????
                // ????????????????????????
                this.chatsTextInputArea.positionCaret(caretPosition + 1);
            }
        }
    }

    @FXML
    public void onClickedChatsSend(MouseEvent mouseEvent) {
        this.chatsTextInputArea.requestFocus();

        var textEntered = this.chatsTextInputArea.getText();
        // ?????????????????????????????????????????????????????????????????????????????????????????? 2.5s ????????????
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

        // ??? center ????????????
        var onSendChatMsgEvent = new OnSendChatMsgEvent();
        onSendChatMsgEvent.setSessionId(sessionId)
                .setUserId(me.getId())
                .setContactCode(contactCode)
                .setMsgType(TEXT) // ?????????????????????????????????
                .setMsg(textEntered)
                .setTime(time);

        this.onSendChatMsg(onSendChatMsgEvent);

        // ?????? UI ??????
        var msg = new SingleMsg();
        msg.setUser(me)
                .setMsgType(TEXT)
                .setMsg(textEntered)
                .setTime(time);
        dialogContent.addDialogMsgIncrementally(msg);

        // ????????????
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

        // ????????????
    }

    @FXML
    public void onActionClose(ActionEvent event) {
        this.quit();
    }

}
