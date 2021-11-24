package org.wangpai.wchat.ui.view.functions.contacts.newfriends;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;
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
import org.wangpai.wchat.ui.model.external.driver.event.OnAddFriendEvent;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;
import org.wangpai.wchat.ui.model.universal.UiDatabase;
import org.wangpai.wchat.ui.model.universal.WchatUtil;
import org.wangpai.wchat.ui.view.base.FxController;
import org.wangpai.wchat.ui.view.functions.contacts.friends.Friends;
import org.wangpai.wchat.universal.util.id.IdUtil;

import static org.wangpai.wchat.ui.model.universal.ReadMode.RECURSIVE;

@Accessors(chain = true)
public class NewFriendsContent implements FxController, Initializable {
    private static NewFriendsContent content;

    @Setter
    @Getter
    private NewFriends source;

    @FXML
    private AnchorPane newFriendsContent;

    private Label title; // 此字段不使用注解 @FXML

    @FXML
    private Button avatar;

    private byte[] avatarBytes;

    @FXML
    private TextField nicknameTextField;

    @FXML
    private TextField remarksTextField;

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private TextField myContactCodeTextField;

    @FXML
    private TextField otherPartyContactCodeTextField;

    private static InputStream[] defaultAvatarStreams = null;

    public NewFriendsContent setTitle(String newTitle) {
        this.title.setText(newTitle);
        return this;
    }

    public NewFriendsContent setAvatar(byte[] avatarData) {
        this.avatarBytes = avatarData;
        this.avatar.setGraphic(WchatUtil.byteArray2ImageView(avatarData, this.avatar.getPrefWidth()));
        return this;
    }

    public NewFriendsContent setNickName(String nickName) {
        this.nicknameTextField.setText(nickName);
        return this;
    }

    public NewFriendsContent setRemarks(String remarks) {
        this.remarksTextField.setText(remarks);
        return this;
    }

    public NewFriendsContent setIp(String ip) {
        this.ipTextField.setText(ip);
        return this;
    }

    public NewFriendsContent setPort(String port) {
        this.portTextField.setText(port);
        return this;
    }

    public NewFriendsContent setMyContactCode(String myContactCode) {
        this.myContactCodeTextField.setText(myContactCode);
        return this;
    }

    public NewFriendsContent setOtherPartyContactCode(String otherPartyContactCode) {
        this.otherPartyContactCodeTextField.setText(otherPartyContactCode);
        return this;
    }

    @Override
    public Pane getComponent() {
        return this.newFriendsContent;
    }

    /**
     * 返回的是单例对象
     *
     * @since 2021-11-5
     */
    public static NewFriendsContent getInstance() {
        if (NewFriendsContent.content != null) {
            return NewFriendsContent.content;
        }

        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(NewFriendsContent.class.getResource("NewFriendsContent.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + NewFriendsContent.class);
            exception.printStackTrace();
        }
        NewFriendsContent.content = fxmlLoader.getController();
        node.setUserData(NewFriendsContent.content);
        return NewFriendsContent.content;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 绑定标题组件
        this.title = UiDatabase.getElementById("contactsTitle", Label.class);
        this.setAvatar(getRandomImage());
    }

    @FXML
    public void onActionNewFriendsContentAvatar(ActionEvent actionEvent) {
        this.setAvatar(getRandomImage());
    }

    @FXML
    public void onActionAddButton(ActionEvent actionEvent) {
        String userId = IdUtil.idGenerator();
        String ip = this.ipTextField.getText();
        String port = this.portTextField.getText();
        String nickname = this.nicknameTextField.getText();
        String remarks = this.remarksTextField.getText();
        String myContactCode = this.myContactCodeTextField.getText();
        String otherPartyContactCode = this.otherPartyContactCodeTextField.getText();

        ip = ip == null ? "" : ip;
        port = port == null ? "" : port;
        nickname = nickname == null ? "" : nickname;
        remarks = remarks == null ? "" : remarks;
        myContactCode = myContactCode == null ? "" : myContactCode;
        otherPartyContactCode = otherPartyContactCode == null ? "" : otherPartyContactCode;

        var user = new User()
                .setId(userId)
                .setIp(ip)
                .setPort(port)
                .setNickname(nickname)
                .setAvatar(this.avatarBytes) // 因为之前设置了默认头像，所以头像不会为非法值
                .setRemarks(remarks)
                .setMyContactCode(myContactCode)
                .setOtherPartyContactCode(otherPartyContactCode);
        var event = new OnAddFriendEvent().setUser(user);
        UiDatabase.getExternalApi().onAddFriend(event);

        var friends = Friends.getExistingInstance();
        friends.refreshFriendsList(); // 刷新联系人列表
        var friendsElement = friends.getElementByUserId(userId);
        friendsElement.onClickFriendsElement(null); // 跳转到该联系人详细信息界面
    }

    @FXML
    public void onActionClearButton(ActionEvent actionEvent) {
        this.clearContent();
    }

    /**
     * 去掉头像按钮中的背景图片
     *
     * @since 2022-1-15
     */
    public NewFriendsContent removeAvatarImage() {
        this.avatar.setGraphic(null);
        return this;
    }

    /**
     * @since 2022-1-15
     */
    public NewFriendsContent clearContent() {
        return this.setTitle("")
                .removeAvatarImage()
                .setNickName("")
                .setRemarks("")
                .setIp("")
                .setPort("")
                .setMyContactCode("")
                .setOtherPartyContactCode("");
    }

    public static byte[] getRandomImage() {
        try {
            if (defaultAvatarStreams == null) {
                defaultAvatarStreams = WchatUtil.readAllResFiles("img/defaultavatar", RECURSIVE);
            }
            int randomNum = new Random().nextInt(defaultAvatarStreams.length); // nextInt 返回一个 [0, length) 之间的整数
            var inputStream = defaultAvatarStreams[randomNum];
            var avatarBytes = WchatUtil.inputStream2byteArray(inputStream);
            // 重制流。如果不这样做，下次对流的读取将失败
            defaultAvatarStreams[randomNum] = WchatUtil.byteArray2InputStream(avatarBytes);
            return avatarBytes;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
