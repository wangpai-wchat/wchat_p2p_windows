package org.wangpai.wchat.ui.view.functions.contacts.newfriends;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.wangpai.wchat.ui.model.functions.contacts.ContactsListItem;
import org.wangpai.wchat.ui.model.functions.contacts.util.ContactsContentManager;
import org.wangpai.wchat.ui.model.functions.contacts.util.ContactsUtil;
import org.wangpai.wchat.ui.model.universal.UiDatabase;
import org.wangpai.wchat.ui.model.universal.WchatUtil;
import org.wangpai.wchat.ui.view.base.FxController;
import org.wangpai.wchat.ui.view.base.UiAdjustable;

public class NewFriends implements FxController, ContactsListItem, UiAdjustable {
    @FXML
    private AnchorPane newFriends;

    @FXML
    private Label newFriendsTag;

    @FXML
    private ListView newFriendsList;

    @FXML
    private AnchorPane newFriendsElement;

    @FXML
    private Label avatar;

    @FXML
    private Label name;

    public NewFriends setAvatar(byte[] avatarData) {
        this.avatar.setGraphic(WchatUtil.byteArray2ImageView(avatarData, this.avatar.getPrefWidth()));
        return this;
    }

    public static NewFriends getInstance() {
        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(NewFriends.class.getResource("NewFriends.fxml"));

        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + NewFriends.class);
            exception.printStackTrace();
        }
        NewFriends fxmlController = fxmlLoader.getController();
        node.setUserData(fxmlController);
        return fxmlController;
    }

    @Override
    public Pane getComponent() {
        return this.newFriends;
    }

    @FXML
    public void onClickNewFriends(MouseEvent mouseEvent) {
        var content = NewFriendsContent.getInstance()
                .setSource(this)
                .setTitle(this.newFriendsTag.getText());
        // 注册 NewFriendsContent 组件
        if (!ContactsContentManager.containsKey(this)) {
            // 将 NewFriendsContent 添加在界面中
            UiDatabase.getElementById("contactsContent", AnchorPane.class)
                    .getChildren()
                    .add(content.getComponent());
            // 在 ContactsContentManager 中注册 NewFriendsContent 组件
            ContactsContentManager.put(this, content.getComponent());
        }

        UiDatabase.getElementById("contactsDefaultFace", AnchorPane.class).setVisible(false); // 移除默认空白面板
        ContactsUtil.cancelOtherSelection(this); // 去掉上一次的选中效果
        ContactsContentManager.switchContent(content.getComponent()); // 设置界面切换
        this.newFriendsElement.requestFocus(); // 设置焦点位置
    }

    @Override
    public boolean deselect() {
        this.newFriendsList.getSelectionModel().clearSelection();
        return true;
    }

    @Override
    public void afterUiShow() {
        this.autoAdjustNamePosition();
    }

    /**
     * 目前，此方法只会自动调整高度
     *
     * 此方法必须在 UI 界面显示后才能调用，否则将产生错误的效果或无效
     *
     * @since 2021-11-7
     */
    public void autoAdjustNamePosition() {
        var height = (this.newFriendsElement.getHeight() - this.name.getHeight()) / 2;

        this.name.setLayoutY(height);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var friendListHeight = 60;
        var friendsSize = this.newFriendsTag.getPrefHeight() + friendListHeight; // newFriendsTag 的高度加 newFriendsList 的高度
        final var extraHeight = 20;

        this.newFriends.setPrefHeight(friendsSize + extraHeight);
        /**
         * 额外增加 extraHeight 是为了调高 newFriendsList 的高度，
         * 否则 newFriendsList 中将出现多余的滑条
         */
        this.newFriendsList.setPrefHeight(friendListHeight + extraHeight);
    }
}
