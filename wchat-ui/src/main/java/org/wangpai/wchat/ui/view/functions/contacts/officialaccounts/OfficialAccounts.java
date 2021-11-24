package org.wangpai.wchat.ui.view.functions.contacts.officialaccounts;

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
import org.wangpai.wchat.ui.model.functions.contacts.util.ContactsUtil;
import org.wangpai.wchat.ui.model.universal.UiDatabase;
import org.wangpai.wchat.ui.model.universal.WchatUtil;
import org.wangpai.wchat.ui.view.base.FxController;
import org.wangpai.wchat.ui.view.base.UiAdjustable;

public class OfficialAccounts implements FxController, ContactsListItem, UiAdjustable {
    @FXML
    private AnchorPane officialAccounts;

    @FXML
    private Label officialAccountsTag;

    @FXML
    private ListView officialAccountsList;

    @FXML
    private AnchorPane officialAccountsElement;

    @FXML
    private Label avatar;

    @FXML
    private Label name;

    public OfficialAccounts setAvatar(byte[] avatarData) {
        this.avatar.setGraphic(WchatUtil.byteArray2ImageView(avatarData, this.avatar.getPrefWidth()));
        return this;
    }

    public static OfficialAccounts getInstance() {
        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(OfficialAccounts.class.getResource("OfficialAccounts.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + OfficialAccounts.class);
            exception.printStackTrace();
        }
        OfficialAccounts fxmlController = fxmlLoader.getController();
        node.setUserData(fxmlController);
        return fxmlController;
    }

    @Override
    public Pane getComponent() {
        return this.officialAccounts;
    }

    public void onClickOfficialAccounts(MouseEvent mouseEvent) {
        UiDatabase.getElementById("contactsDefaultFace", AnchorPane.class).setVisible(false); // 移除默认空白面板
        ContactsUtil.cancelOtherSelection(this);

        System.out.println("OfficialAccounts clicked.");
        // 敬请期待
    }

    @Override
    public boolean deselect() {
        this.officialAccountsList.getSelectionModel().clearSelection();
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
        var height = (this.officialAccountsElement.getHeight() - this.name.getHeight()) / 2;

        this.name.setLayoutY(height);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var friendListHeight = 60;
        var friendsSize = this.officialAccountsTag.getPrefHeight() + friendListHeight; // Tag 的高度加 ListView 的高度
        final var extraHeight = 20;

        this.officialAccounts.setPrefHeight(friendsSize + extraHeight);
        /**
         * 额外增加 extraHeight 是为了调高 ListView 的高度，
         * 否则 ListView 中将出现多余的滑条
         */
        this.officialAccountsList.setPrefHeight(friendListHeight + extraHeight);
    }
}
