package org.wangpai.wchat.ui.view.functions.contacts.friends;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.external.driver.query.GetFriendsResponse;
import org.wangpai.wchat.ui.model.functions.contacts.ContactsListItem;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;
import org.wangpai.wchat.ui.model.universal.UiDatabase;
import org.wangpai.wchat.ui.view.base.FxController;

@Accessors(chain = true)
public class Friends implements FxController, ContactsListItem {
    private static Friends instance;

    @FXML
    private AnchorPane friends;

    @FXML
    private Label friendsTag;

    @FXML
    private ListView friendsList;

    @Getter
    private List<User> users;

    public void addFriend(String id, byte[] avatar, String name) {
        var friendsElement = FriendsElement.getInstance();
        friendsElement.setSource(this)
                .setId(id)
                .setAvatar(avatar)
                .setName(name);

        var items = this.friendsList.getItems();
        items.add(friendsElement.getComponent());
        var friendListHeight = 60 * items.size();
        var friendsSize = this.friendsTag.getPrefHeight() + friendListHeight; // friendsTag 的高度加 friendsList 的高度
        final var extraHeight = 20;

        this.friends.setPrefHeight(friendsSize + extraHeight);
        /**
         * 额外增加 extraHeight 是为了调高 friendsList 的高度，
         * 否则 friendsList 中将出现多余的滑条
         */
        this.friendsList.setPrefHeight(friendListHeight + extraHeight);
    }

    @Override
    public Pane getComponent() {
        return this.friends;
    }

    /**
     * 返回的是单例对象
     *
     * @since 2021-11-24
     * @lastModified 2022-1-3
     */
    public static Friends getInstance() {
        if (instance != null) {
            return instance;
        }

        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(Friends.class.getResource("Friends.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + Friends.class);
            exception.printStackTrace();
        }
        instance = fxmlLoader.getController();
        node.setUserData(instance);
        return instance;
    }

    /**
     * 当需要本类对象的代码并不想初始化本类对象时，可以调用本方法
     *
     * @since 2022-1-3
     */
    public static Friends getExistingInstance() {
        return instance;
    }

    @Override
    public boolean deselect() {
        this.friendsList.getSelectionModel().clearSelection();
        return true;
    }

    /**
     * 设置 Friends 列表选中 friendsElement 的效果。
     * 如果 Friends 列表中没有 friendsElement，什么也不做，返回 false
     *
     * @since 2022-1-6
     */
    public boolean setElementSelectedEffect(FriendsElement friendsElement) {
        if (!this.friendsList.getItems().contains(friendsElement.getComponent())) {
            return false;
        }

        this.friendsList.getSelectionModel().select(friendsElement.getComponent());

        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.refreshFriendsList();
    }

    /**
     * 刷新联系人列表
     *
     * @since 2022-1-16
     */
    public Friends refreshFriendsList() {
        this.clearFriendsList(); // 先尝试清空列表

        var response = (GetFriendsResponse) UiDatabase.getExternalApi().getFriends(null);
        this.users = response.getUsers();
        if (this.users != null) {
            for (var user : this.users) {
                this.addFriend(user.getId(), user.getAvatar(), user.getName());
            }
        }

        var friendsContent = FriendsContent.getExistingInstance();
        if (friendsContent != null) {
            friendsContent.clearContent(); // 清空联系人信息界面的内容
        }

        return this;
    }

    /**
     * 清空联系人列表。如果列表已经是空的，这不会引发异常
     *
     * @since 2022-1-16
     */
    public Friends clearFriendsList() {
        this.friendsList.getItems().clear();
        return this;
    }

    /**
     * 按照 User 中的 userId 来在 Friends 中查找对应的 User。如果没有找到，将返回 null
     *
     * @since 2021-11-17
     */
    public User searchUserByUserId(String userId) {
        for (var user : this.users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 通过 userId 来从 Friends 中寻找子组件
     *
     * @return 如果找到了，返回该组件。如果 userId 不正确，返回 null
     * @since 2022-1-6
     */
    public FriendsElement getElementByUserId(String userId) {
        var chatsListItems = this.friendsList.getItems();
        FriendsElement friendsElement = null;
        for (var item : chatsListItems) {
            friendsElement = ((FriendsElement) ((Node) item).getUserData());
            if (friendsElement.getId().equals(userId)) {
                return friendsElement;
            }
        }

        return null;
    }
}
