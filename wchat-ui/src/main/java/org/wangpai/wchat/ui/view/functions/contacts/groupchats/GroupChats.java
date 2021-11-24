package org.wangpai.wchat.ui.view.functions.contacts.groupchats;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.wangpai.wchat.ui.model.functions.contacts.ContactsListItem;
import org.wangpai.wchat.ui.view.base.FxController;

public class GroupChats implements FxController, ContactsListItem {
    @FXML
    private AnchorPane groupChats;

    @FXML
    private ListView groupChatsList;

    public static GroupChats getInstance() {
        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(GroupChats.class.getResource("GroupChats.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + GroupChats.class);
            exception.printStackTrace();
        }
        GroupChats fxmlController = fxmlLoader.getController();
        node.setUserData(fxmlController);
        return fxmlController;
    }

    @Override
    public Pane getComponent() {
        return this.groupChats;
    }

    @Override
    public boolean deselect() {
        this.groupChatsList.getSelectionModel().clearSelection();
        return true;
    }
}
