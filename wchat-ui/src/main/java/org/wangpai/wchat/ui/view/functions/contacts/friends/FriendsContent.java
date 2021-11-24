package org.wangpai.wchat.ui.view.functions.contacts.friends;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.external.driver.event.OnUpdateFriendInfoEvent;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;
import org.wangpai.wchat.ui.model.universal.UiDatabase;
import org.wangpai.wchat.ui.model.universal.WchatUtil;
import org.wangpai.wchat.ui.view.base.FxController;
import org.wangpai.wchat.ui.view.functions.chats.ChatsList;
import org.wangpai.wchat.ui.view.functions.chats.SessionElement;

import static org.wangpai.wchat.ui.view.functions.contacts.friends.FriendsContent.FriendsContentField.IP;
import static org.wangpai.wchat.ui.view.functions.contacts.friends.FriendsContent.FriendsContentField.MY_CONTACT_CODE;
import static org.wangpai.wchat.ui.view.functions.contacts.friends.FriendsContent.FriendsContentField.NICKNAME;
import static org.wangpai.wchat.ui.view.functions.contacts.friends.FriendsContent.FriendsContentField.OTHER_PARTY_CONTACT_CODE;
import static org.wangpai.wchat.ui.view.functions.contacts.friends.FriendsContent.FriendsContentField.PORT;
import static org.wangpai.wchat.ui.view.functions.contacts.friends.FriendsContent.FriendsContentField.REMARKS;

@Accessors(chain = true)
public class FriendsContent implements FxController, Initializable {
    private static FriendsContent instance;

    @Setter
    @Getter
    private FriendsElement source;

    @FXML
    private AnchorPane friendsContent;

    private Label title; // 此字段不使用注解 @FXML

    @FXML
    private Button avatar;

    private byte[] avatarBytes;

    private String nicknameFromDb;

    @FXML
    private TextField nicknameTextField;

    @FXML
    private Button nicknameCancelButton;

    private String remarksFromDb;

    @FXML
    private TextField remarksTextField;

    @FXML
    private Button remarksCancelButton;

    private String ipFromDb;

    @FXML
    private TextField ipTextField;

    @FXML
    private Button ipCancelButton;

    private String portFromDb; // 端口号在本类不要定义为 int 类型

    @FXML
    private TextField portTextField;

    @FXML
    private Button portCancelButton;

    private String myContactCodeFromDb;

    @FXML
    private TextField myContactCodeTextField;

    @FXML
    private Button myContactCodeCancelButton;

    private String otherPartyContactCodeFromDb;

    @FXML
    private TextField otherPartyContactCodeTextField;

    @FXML
    private Button otherPartyContactCodeCancelButton;

    @FXML
    private Button sendMsgButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button discardButton;

    /**
     * value 值代表这个 Field 的值有没有被改变且没有保存。true 代表被改变且没保存。初始值为 false
     *
     * @since 2022-1-15
     */
    private final Map<FriendsContentField, Boolean> fieldStates = new HashMap<>(FriendsContentField.values().length);

    public FriendsContent setTitle(String newTitle) {
        this.title.setText(newTitle);
        return this;
    }

    public FriendsContent setAvatar(byte[] avatarData) {
        this.avatar.setGraphic(WchatUtil.byteArray2ImageView(avatarData, this.avatar.getPrefWidth()));
        this.avatarBytes = avatarData;
        return this;
    }

    public FriendsContent setNickName(String nickName) {
        var nickNameFix = nickName == null ? "" : nickName;
        this.fieldStates.put(NICKNAME, false);
        this.nicknameFromDb = nickNameFix;
        this.nicknameTextField.setText(nickNameFix);
        return this;
    }

    public FriendsContent setRemarks(String remarks) {
        var remarksFix = remarks == null ? "" : remarks;
        this.fieldStates.put(REMARKS, false);
        this.remarksFromDb = remarksFix;
        this.remarksTextField.setText(remarksFix);
        return this;
    }

    public FriendsContent setIp(String ip) {
        var ipFix = ip == null ? "" : ip;
        this.fieldStates.put(IP, false);
        this.ipFromDb = ipFix;
        this.ipTextField.setText(ipFix);
        return this;
    }

    public FriendsContent setPort(String port) {
        var portFix = port == null ? "" : port;
        this.fieldStates.put(PORT, false);
        this.portFromDb = portFix;
        this.portTextField.setText(portFix);
        return this;
    }

    public FriendsContent setMyContactCode(String myContactCode) {
        var myContactCodeFix = myContactCode == null ? "" : myContactCode;
        this.fieldStates.put(MY_CONTACT_CODE, false);
        this.myContactCodeFromDb = myContactCodeFix;
        this.myContactCodeTextField.setText(myContactCodeFix);
        return this;
    }

    public FriendsContent setOtherPartyContactCode(String otherPartyContactCode) {
        var otherPartyContactCodeFix = otherPartyContactCode == null ? "" : otherPartyContactCode;
        this.fieldStates.put(OTHER_PARTY_CONTACT_CODE, false);
        this.otherPartyContactCodeFromDb = otherPartyContactCodeFix;
        this.otherPartyContactCodeTextField.setText(otherPartyContactCodeFix);
        return this;
    }

    /**
     * 去掉头像按钮中的背景图片
     *
     * @since 2022-1-15
     */
    public FriendsContent removeAvatarImage() {
        this.avatar.setGraphic(null);
        return this;
    }

    /**
     * @since 2022-1-15
     */
    public FriendsContent clearContent() {
        return this.setTitle("")
                .removeAvatarImage()
                .setNickName("")
                .setRemarks("")
                .setIp("")
                .setPort("")
                .setMyContactCode("")
                .setOtherPartyContactCode("");
    }

    /**
     * @since 2022-1-16
     */
    public FriendsContent recoverUserInfo() {
        return this.setNickName(this.nicknameFromDb)
                .setRemarks(this.remarksFromDb)
                .setIp(this.ipFromDb)
                .setPort(this.portFromDb)
                .setMyContactCode(this.myContactCodeFromDb)
                .setOtherPartyContactCode(this.otherPartyContactCodeFromDb);
    }

    @Override
    public Pane getComponent() {
        return this.friendsContent;
    }

    /**
     * 返回的是单例对象
     *
     * @since 2021-11-5
     */
    public static FriendsContent getInstance() {
        if (FriendsContent.instance != null) {
            return FriendsContent.instance;
        }

        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(FriendsContent.class.getResource("FriendsContent.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + FriendsContent.class);
            exception.printStackTrace();
        }
        FriendsContent.instance = fxmlLoader.getController();
        node.setUserData(FriendsContent.instance);
        return FriendsContent.instance;
    }

    /**
     * 当需要本类对象的代码并不想初始化本类对象时，可以调用本方法
     *
     * @since 2021-11-28
     */
    public static FriendsContent getExistingInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fieldStates.put(NICKNAME, false);
        this.fieldStates.put(REMARKS, false);
        this.fieldStates.put(IP, false);
        this.fieldStates.put(PORT, false);
        this.fieldStates.put(MY_CONTACT_CODE, false);
        this.fieldStates.put(OTHER_PARTY_CONTACT_CODE, false);

        // 绑定标题组件
        this.title = UiDatabase.getElementById("contactsTitle", Label.class);

        this.nicknameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(newValue, this.nicknameFromDb)) {
                this.fieldStates.put(NICKNAME, true);
                this.nicknameCancelButton.setVisible(true);
                this.saveButton.setVisible(true);
                this.discardButton.setVisible(true);
                this.sendMsgButton.setVisible(false);
            } else {
                this.fieldStates.put(NICKNAME, false);
                this.nicknameCancelButton.setVisible(false);
                if (!this.hasChanges()) {
                    this.saveButton.setVisible(false);
                    this.discardButton.setVisible(false);
                    this.sendMsgButton.setVisible(true);
                }
            }
        });

        this.remarksTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(newValue, this.remarksFromDb)) {
                this.fieldStates.put(REMARKS, true);
                this.remarksCancelButton.setVisible(true);
                this.saveButton.setVisible(true);
                this.discardButton.setVisible(true);
                this.sendMsgButton.setVisible(false);
            } else {
                this.fieldStates.put(REMARKS, false);
                this.remarksCancelButton.setVisible(false);
                if (!this.hasChanges()) {
                    this.saveButton.setVisible(false);
                    this.discardButton.setVisible(false);
                    this.sendMsgButton.setVisible(true);
                }
            }
        });

        this.ipTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(newValue, this.ipFromDb)) {
                this.fieldStates.put(IP, true);
                this.ipCancelButton.setVisible(true);
                this.saveButton.setVisible(true);
                this.discardButton.setVisible(true);
                this.sendMsgButton.setVisible(false);
            } else {
                this.fieldStates.put(IP, false);
                this.ipCancelButton.setVisible(false);
                if (!this.hasChanges()) {
                    this.saveButton.setVisible(false);
                    this.discardButton.setVisible(false);
                    this.sendMsgButton.setVisible(true);
                }
            }
        });

        this.portTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(newValue, this.portFromDb)) {
                this.fieldStates.put(PORT, true);
                this.portCancelButton.setVisible(true);
                this.saveButton.setVisible(true);
                this.discardButton.setVisible(true);
                this.sendMsgButton.setVisible(false);
            } else {
                this.fieldStates.put(PORT, false);
                this.portCancelButton.setVisible(false);
                if (!this.hasChanges()) {
                    this.saveButton.setVisible(false);
                    this.discardButton.setVisible(false);
                    this.sendMsgButton.setVisible(true);
                }
            }
        });

        this.myContactCodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(newValue, this.myContactCodeFromDb)) {
                this.fieldStates.put(MY_CONTACT_CODE, true);
                this.myContactCodeCancelButton.setVisible(true);
                this.saveButton.setVisible(true);
                this.discardButton.setVisible(true);
                this.sendMsgButton.setVisible(false);
            } else {
                this.fieldStates.put(MY_CONTACT_CODE, false);
                this.myContactCodeCancelButton.setVisible(false);
                if (!this.hasChanges()) {
                    this.saveButton.setVisible(false);
                    this.discardButton.setVisible(false);
                    this.sendMsgButton.setVisible(true);
                }
            }
        });

        this.otherPartyContactCodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(newValue, this.otherPartyContactCodeFromDb)) {
                this.fieldStates.put(OTHER_PARTY_CONTACT_CODE, true);
                this.otherPartyContactCodeCancelButton.setVisible(true);
                this.saveButton.setVisible(true);
                this.discardButton.setVisible(true);
                this.sendMsgButton.setVisible(false);
            } else {
                this.fieldStates.put(OTHER_PARTY_CONTACT_CODE, false);
                this.otherPartyContactCodeCancelButton.setVisible(false);
                if (!this.hasChanges()) {
                    this.saveButton.setVisible(false);
                    this.discardButton.setVisible(false);
                    this.sendMsgButton.setVisible(true);
                }
            }
        });
    }

    @FXML
    public void onActionSendButton() {
        var sessionId = this.getSource().getId();
        SessionElement sessionElement = ChatsList.getExistingInstance().getElementBySessionId(sessionId);
        if (sessionElement == null) { // 如果没有找到，则新建一个 SessionElement
            var chatsList = ChatsList.getExistingInstance();
            sessionElement = chatsList.addDefaultSession(sessionId);
        }

        UiDatabase.getMainFace().onActionChats(null);
        sessionElement.onClickSessionElement(null);
    }

    @FXML
    public void onActionFriendsContentAvatar(ActionEvent actionEvent) {
        System.out.println("---onActionFriendsContentAvatar---");
    }

    @FXML
    public void onActionNicknameCancelButton(ActionEvent actionEvent) {
        this.nicknameTextField.setText(this.nicknameFromDb);
        this.nicknameCancelButton.setVisible(false);
        /**
         * 设置焦点。如果不这么做，焦点将会转移到下一个文本框中。
         * 因为前面令按钮消失之前，焦点还在按钮上，此时由于焦点失去目标，将会自动转移
         */
        this.nicknameTextField.requestFocus();
    }

    @FXML
    public void onActionRemarksCancelButton(ActionEvent actionEvent) {
        this.remarksTextField.setText(this.remarksFromDb);
        this.remarksCancelButton.setVisible(false);
        /**
         * 设置焦点。如果不这么做，焦点将会转移到下一个文本框中。
         * 因为前面令按钮消失之前，焦点还在按钮上，此时由于焦点失去目标，将会自动转移
         */
        this.remarksTextField.requestFocus();
    }

    @FXML
    public void onActionIpCancelButton(ActionEvent actionEvent) {
        this.ipTextField.setText(this.ipFromDb);
        this.ipCancelButton.setVisible(false);
        /**
         * 设置焦点。如果不这么做，焦点将会转移到下一个文本框中。
         * 因为前面令按钮消失之前，焦点还在按钮上，此时由于焦点失去目标，将会自动转移
         */
        this.ipTextField.requestFocus();
    }

    @FXML
    public void onActionPortCancelButton(ActionEvent actionEvent) {
        this.portTextField.setText(String.valueOf(this.portFromDb));
        this.portCancelButton.setVisible(false);
        /**
         * 设置焦点。如果不这么做，焦点将会转移到下一个文本框中。
         * 因为前面令按钮消失之前，焦点还在按钮上，此时由于焦点失去目标，将会自动转移
         */
        this.portTextField.requestFocus();
    }

    @FXML
    public void onActionMyContactCodeCancelButton(ActionEvent actionEvent) {
        this.myContactCodeTextField.setText(this.myContactCodeFromDb);
        this.myContactCodeCancelButton.setVisible(false);
        /**
         * 设置焦点。如果不这么做，焦点将会转移到下一个文本框中。
         * 因为前面令按钮消失之前，焦点还在按钮上，此时由于焦点失去目标，将会自动转移
         */
        this.myContactCodeTextField.requestFocus();
    }

    @FXML
    public void onActionOtherPartyContactCodeCancelButton(ActionEvent actionEvent) {
        this.otherPartyContactCodeTextField.setText(this.otherPartyContactCodeFromDb);
        this.otherPartyContactCodeCancelButton.setVisible(false);
        /**
         * 设置焦点。如果不这么做，焦点将会转移到下一个文本框中。
         * 因为前面令按钮消失之前，焦点还在按钮上，此时由于焦点失去目标，将会自动转移
         */
        this.otherPartyContactCodeCancelButton.requestFocus();
    }

    @FXML
    public void onActionSaveButton(ActionEvent actionEvent) {
        String userId = this.getSource().getId();
        String ip = this.ipTextField.getText();
        String port = this.portTextField.getText();
        String nickname = this.nicknameTextField.getText();
        String remarks = this.remarksTextField.getText();
        String myContactCode = this.myContactCodeTextField.getText();
        String otherPartyContactCode = this.otherPartyContactCodeTextField.getText();

        var user = new User()
                .setId(userId)
                .setIp(ip)
                .setPort(port)
                .setNickname(nickname)
                .setAvatar(this.avatarBytes)
                .setRemarks(remarks)
                .setMyContactCode(myContactCode)
                .setOtherPartyContactCode(otherPartyContactCode);
        var event = new OnUpdateFriendInfoEvent()
                .setUserId(userId)
                .setUser(user);
        UiDatabase.getExternalApi().onUpdateFriendInfo(event);

        // 更新组件文本
        this.setNickName(nickname)
                .setRemarks(remarks)
                .setIp(ip)
                .setPort(port)
                .setMyContactCode(myContactCode)
                .setOtherPartyContactCode(otherPartyContactCode);
        // 去掉所有文本框的取消按钮
        this.nicknameCancelButton.setVisible(false);
        this.remarksCancelButton.setVisible(false);
        this.ipCancelButton.setVisible(false);
        this.portCancelButton.setVisible(false);
        this.myContactCodeCancelButton.setVisible(false);
        this.otherPartyContactCodeCancelButton.setVisible(false);

        this.saveButton.setVisible(false);
        this.discardButton.setVisible(false);
        this.sendMsgButton.setVisible(true);

        /**
         * 设置焦点。如果不这么做，焦点会自动转移到下一个可获得焦点组件中。
         * 因为前面令按钮消失之前，焦点还在按钮上，此时由于焦点失去目标，将会自动转移
         */
        this.getComponent().requestFocus();
    }

    @FXML
    public void onActionDiscardButton(ActionEvent actionEvent) {
        this.recoverUserInfo();

        this.saveButton.setVisible(false);
        this.discardButton.setVisible(false);
        this.sendMsgButton.setVisible(true);

        /**
         * 设置焦点。如果不这么做，焦点会自动转移到下一个可获得焦点组件中。
         * 因为前面令按钮消失之前，焦点还在按钮上，此时由于焦点失去目标，将会自动转移
         */
        this.getComponent().requestFocus();
    }

    /**
     * 判断有没有 Field 被改变。
     * 如果有一个被改变，就认为有被改变的。如果都没有被改变，就认为没有被改变的
     *
     * @since 2022-1-15
     */
    public boolean hasChanges() {
        for (var isChanged : this.fieldStates.values()) {
            // 如果有一个被改变，就认为有被改变的
            if (isChanged) {
                return true;
            }
        }
        // 如果都没有被改变，就认为没有被改变的
        return false;
    }

    enum FriendsContentField {
        NICKNAME,
        REMARKS,
        IP,
        PORT,
        MY_CONTACT_CODE,
        OTHER_PARTY_CONTACT_CODE
    }
}
