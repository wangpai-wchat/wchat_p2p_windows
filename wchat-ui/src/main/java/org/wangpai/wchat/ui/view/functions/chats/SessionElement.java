package org.wangpai.wchat.ui.view.functions.chats;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.universal.UiDatabase;
import org.wangpai.wchat.ui.model.universal.WchatUtil;
import org.wangpai.wchat.ui.view.base.FxController;
import org.wangpai.wchat.universal.util.time.TimeUtil;

@Accessors(chain = true)
public class SessionElement implements Initializable, FxController {
    @FXML
    private AnchorPane sessionElement;

    @FXML
    private Label avatar;

    @FXML
    private Label name;

    @FXML
    private Label msgDigest;

    @FXML
    private Label time;

    @FXML
    private Label msgReminderNoCounter;

    @FXML
    private Label msgReminderWithCounter;

    @Setter
    @Getter
    private ChatsList source;

    @Setter
    @Getter
    private boolean selected;

    /**
     * 规定：此 id 与 FriendsElement 中的 id 相同
     */
    @Getter
    @Setter(AccessLevel.NONE) // 此字段禁止自动生成 setter 方法，因为已经有了同名的方法了
    private String id;

    @Getter
    @Setter
    private int msgReminderCounter;

    @Getter
    @Setter(AccessLevel.NONE) // 此字段禁止自动生成 setter 方法，因为已经有了同名的方法了
    private boolean disturb;

    public SessionElement setId(String id) {
        this.sessionElement.setId(id);
        this.id = id;
        return this;
    }

    public SessionElement setAvatar(byte[] avatarData) {
        this.avatar.setGraphic(WchatUtil.byteArray2ImageView(avatarData, this.avatar.getPrefWidth()));
        return this;
    }

    public SessionElement setName(String name) {
        this.name.setText(name);
        return this;
    }

    public SessionElement setMsgDigest(String msgDigest) {
        this.msgDigest.setText(msgDigest);
        return this;
    }

    public SessionElement setTime(LocalDateTime time) {
        var timeMsg = TimeUtil.formatLocalDateTime(time);
        this.time.setText(timeMsg);

        // 此组件 this.date 的右端点 x 坐标不能变化。此组件的左端点 x 坐标及宽度需要动态调整
        var rightCoordinateX = this.time.getLayoutX() + this.time.getPrefWidth();
        var width = timeMsg.length() * 7 + 5;
        this.time.setLayoutX(rightCoordinateX - width);
        this.time.setPrefWidth(width);

        return this;
    }

    /**
     * @param num
     * 正数表示显示数字红点
     * 负数表示不显示数字，只显示红点。
     * 0 表示不显示红点
     * @since 2021-11-15
     */
    public SessionElement setMsgReminder(int num) {
        this.msgReminderCounter = num;

        if (num == 0) { // 不显示红点
            this.msgReminderNoCounter.setVisible(false);
            this.msgReminderWithCounter.setVisible(false);
        } else if (this.disturb) {
            this.msgReminderWithCounter.setVisible(true);
            this.msgReminderNoCounter.setVisible(false);
            this.msgReminderWithCounter.setText(String.valueOf(Math.abs(num)));
        } else { // 只显示红点，不显示数字
            this.msgReminderNoCounter.setVisible(true);
            this.msgReminderWithCounter.setVisible(false);
        }

        return this;
    }

    public void setDisturb(boolean disturb) {
        this.disturb = disturb;
        this.setMsgReminder(this.msgReminderCounter); // 更新红点显示效果
    }

    public SessionElement setUserData(Object data) {
        this.sessionElement.setUserData(data);
        return this;
    }

    /**
     * @since 2021-10-26
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 暂时不需要实现
    }

    @Override
    public Pane getComponent() {
        return this.sessionElement;
    }

    public static SessionElement getInstance() {
        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(SessionElement.class.getResource("SessionElement.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + SessionElement.class);
            exception.printStackTrace();
        }
        SessionElement fxmlController = fxmlLoader.getController();
        node.setUserData(fxmlController);
        return fxmlController;
    }

    @FXML
    public void onClickSessionElement(MouseEvent mouseEvent) {
        this.setMsgReminder(0); // 去掉消息红点
        UiDatabase.getElementById("chatsDefaultFace", AnchorPane.class).setVisible(false); // 移除默认空白面板
        UiDatabase.getElementById("chatsTextInputArea", TextArea.class).requestFocus();
        this.getSource().updateSelectedState(this);

        DialogContent.getInstance()
                .bindContainer("chatsDialogContent")
                .setTitle(this.name.getText())
                .setSessionId(this.getId())
                .afterConfiguration();
    }
}
