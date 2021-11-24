package org.wangpai.wchat.ui.view.functions.contacts;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import org.wangpai.wchat.ui.model.functions.contacts.ContactsListItem;
import org.wangpai.wchat.ui.model.functions.contacts.util.ContactsUtil;
import org.wangpai.wchat.ui.view.base.FxController;
import org.wangpai.wchat.ui.view.functions.contacts.friends.Friends;
import org.wangpai.wchat.ui.view.functions.contacts.groupchats.GroupChats;
import org.wangpai.wchat.ui.view.functions.contacts.newfriends.NewFriends;
import org.wangpai.wchat.ui.view.functions.contacts.officialaccounts.OfficialAccounts;

public class ContactsList implements FxController, Initializable {
    @FXML
    private ListView contactsList;

    private List<ContactsListItem> itemList = new ArrayList<>(4);

    private NewFriends newFriends;

    private OfficialAccounts officialAccounts;

    private GroupChats groupChats;

    private Friends friends;

    public static ContactsList getInstance() {
        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(ContactsList.class.getResource("ContactsList.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + ContactsList.class);
            exception.printStackTrace();
        }
        ContactsList contactsList = fxmlLoader.getController();
        ContactsUtil.setContactsList(contactsList);
        node.setUserData(contactsList);
        return contactsList;
    }

    @Override
    public ListView getComponent() {
        return this.contactsList;
    }

    public void cancelOtherSelection(ContactsListItem thisItem) {
        for (var item : this.itemList) {
            if (item != thisItem) {
                item.deselect();
            }
        }
    }

    @Override
    public ContactsList afterConfiguration(Object... paras) {
        this.initNewFriends(); // 敬请期待
        this.initOfficialAccounts(); // 敬请期待
        this.initGroupChats(); // 敬请期待
        this.initFriends();

        return this;
    }

    private void initNewFriends() {
        this.newFriends = NewFriends.getInstance();
        this.contactsList.getItems().add(this.newFriends.getComponent());
        this.itemList.add(this.newFriends);
    }

    private void initOfficialAccounts() {
        this.officialAccounts = OfficialAccounts.getInstance();
        this.contactsList.getItems().add(this.officialAccounts.getComponent());
        this.itemList.add(this.officialAccounts);
    }

    private void initGroupChats() {
        // 敬请期待
    }

    private void initFriends() {
        this.friends = Friends.getInstance();
        this.contactsList.getItems().add(this.friends.getComponent());
        this.itemList.add(this.friends);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 暂时不需要实现
    }
}
