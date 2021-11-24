package org.wangpai.wchat.ui.view.functions.chats;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.wangpai.wchat.ui.model.functions.chats.MsgDialog;
import org.wangpai.wchat.ui.model.universal.WchatUtil;
import org.wangpai.wchat.universal.protocol.internal.ui.MsgType;
import org.wangpai.wchat.universal.util.time.TimeUtil;

public class LeftDialogWithName implements MsgDialog {
    @FXML
    private AnchorPane LeftDialogWithName;

    @FXML
    public Label time;

    @FXML
    private Label avatar;

    @FXML
    private Label name;

    @FXML
    private TextArea msg;

    @Override
    public MsgDialog setTime(LocalDateTime time) {
        var timeMsg = TimeUtil.formatLocalDateTime(time);
        this.time.setText(timeMsg);

        // 此组件的 X 坐标应使之位于中间，宽度随时间文本动态调整，但高度应不变
        var centerCoordinateX = this.LeftDialogWithName.getPrefWidth() / 2;
        var width = timeMsg.length() * 7 + 5;
        this.time.setLayoutX(centerCoordinateX - width * 1.0 / 2);
        this.time.setPrefWidth(width);

        return this;
    }

    @Override
    public LeftDialogWithName setAvatar(byte[] avatarData) {
        this.avatar.setGraphic(WchatUtil.byteArray2ImageView(avatarData, this.avatar.getPrefWidth()));
        return this;
    }

    @Override
    public LeftDialogWithName setName(String name) {
        this.name.setText(name);
        return this;
    }

    @Override
    public LeftDialogWithName setMsg(Object msg, MsgType msgType) {
        switch (msgType) {

            case TEXT -> {
                var text = (String) msg;
                this.msg.setText(text);
            }
            default -> {
                // 敬请期待
            }
        }
        return this;
    }


    public static LeftDialogWithName getInstance() {
        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(LeftDialogWithName.class.getResource("LeftDialog.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + LeftDialogWithName.class);
            exception.printStackTrace();
        }
        LeftDialogWithName fxmlController = fxmlLoader.getController();
        node.setUserData(fxmlController);
        return fxmlController;
    }

    @Override
    public Pane getComponent() {
        return this.LeftDialogWithName;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 暂时不需要实现
    }
}
