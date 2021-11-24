package org.wangpai.wchat.ui.view.functions.contacts.friends;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.external.driver.query.GetUserDetailsByUserIdRequest;
import org.wangpai.wchat.ui.model.external.driver.query.GetUserDetailsByUserIdResponse;
import org.wangpai.wchat.ui.model.functions.contacts.util.ContactsContentManager;
import org.wangpai.wchat.ui.model.functions.contacts.util.ContactsUtil;
import org.wangpai.wchat.ui.model.universal.UiDatabase;
import org.wangpai.wchat.ui.model.universal.WchatUtil;
import org.wangpai.wchat.ui.view.base.FxController;
import org.wangpai.wchat.ui.view.base.UiAdjustable;

@Accessors(chain = true)
public class FriendsElement implements FxController, UiAdjustable {
    @FXML
    private AnchorPane friendsElement;

    @FXML
    private Label avatar;

    private byte[] avatarBytes;

    @FXML
    private Label name;

    @Setter
    @Getter
    private Friends source;

    /**
     * 规定：此 id 与 SessionElement 中的 id 相同
     */
    @Getter
    @Setter(AccessLevel.NONE) // 此字段禁止自动生成 setter 方法，因为已经有了同名的方法了
    private String id;

    public FriendsElement setAvatar(byte[] avatarData) {
        this.avatarBytes = avatarData;
        this.avatar.setGraphic(WchatUtil.byteArray2ImageView(avatarData, this.avatar.getPrefWidth()));
        return this;
    }

    public FriendsElement setName(String name) {
        this.name.setText(name);
        return this;
    }

    public FriendsElement setId(String id) {
        this.friendsElement.setId(id);
        this.id = id;
        return this;
    }

    public FriendsElement setUserData(Object data) {
        this.friendsElement.setUserData(data);
        return this;
    }

    public static FriendsElement getInstance() {
        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(FriendsElement.class.getResource("FriendsElement.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + FriendsElement.class);
            exception.printStackTrace();
        }
        FriendsElement fxmlController = fxmlLoader.getController();
        node.setUserData(fxmlController);
        return fxmlController;
    }

    @Override
    public Pane getComponent() {
        return this.friendsElement;
    }

    @FXML
    public void onClickFriendsElement(MouseEvent mouseEvent) {
        var content = FriendsContent.getInstance().setSource(this);
        // 注册 FriendsContent 组件
        if (!ContactsContentManager.containsKey(this.source)) {
            // 将 FriendsContent 添加在界面中
            UiDatabase.getElementById("contactsContent", AnchorPane.class)
                    .getChildren()
                    .add(content.getComponent());
            // 在 ContactsContentManager 中注册 FriendsContent 组件
            ContactsContentManager.put(this.source, content.getComponent());
        }

        var request = new GetUserDetailsByUserIdRequest().setUserId(this.id);
        var response = (GetUserDetailsByUserIdResponse) UiDatabase.getExternalApi().getUserDetailsByUserId(request);
        if (response == null) {
            // 本 if 分支说明尚未建立与该联系人的会话。 // 2022年1月16日：已更改其它地方的 API，现在此 if 分支并不会达到，现此代码备用
            content.clearContent()
                    .setTitle(this.name.getText())
                    .setAvatar(this.avatarBytes)
                    .setNickName(this.name.getText());
        } else {
            content.clearContent()
                    .setTitle(this.name.getText())
                    .setAvatar(response.getAvatar())
                    .setNickName(response.getNickname())
                    .setRemarks(response.getRemarks())
                    .setIp(response.getIp())
                    .setPort(String.valueOf(response.getPort()))
                    .setMyContactCode(response.getMyContactCode())
                    .setOtherPartyContactCode(response.getOtherPartyContactCode());
        }

        UiDatabase.getElementById("contactsDefaultFace", AnchorPane.class).setVisible(false); // 移除默认空白面板
        ContactsUtil.cancelOtherSelection(this.source); // 去掉上一次的选中效果
        this.source.setElementSelectedEffect(this); // 设置本次的选中效果
        ContactsContentManager.switchContent(content.getComponent()); // 设置界面切换
        this.friendsElement.requestFocus(); // 设置焦点位置
    }

    /**
     * 目前，此方法只会自动调整高度
     *
     * 此方法必须在 UI 界面显示后才能调用，否则将产生错误的效果或无效
     *
     * @since 2021-11-7
     */
    public void autoAdjustNamePosition() {
        var height = (this.friendsElement.getHeight() - this.name.getHeight()) / 2;

        this.name.setLayoutY(height);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UiDatabase.getAftermaths().add(this);
    }

    @Override
    public void afterUiShow() {
        this.autoAdjustNamePosition();
    }
}
