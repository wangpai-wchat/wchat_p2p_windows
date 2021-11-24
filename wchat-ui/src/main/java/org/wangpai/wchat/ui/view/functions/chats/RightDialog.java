package org.wangpai.wchat.ui.view.functions.chats;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import org.wangpai.wchat.ui.model.functions.chats.MsgDialog;
import org.wangpai.wchat.ui.model.universal.WchatUtil;
import org.wangpai.wchat.universal.protocol.internal.ui.MsgType;
import org.wangpai.wchat.universal.util.time.TimeUtil;

public class RightDialog implements MsgDialog {
    @FXML
    private AnchorPane rightDialog;

    @FXML
    public Label time;

    @FXML
    private Label avatar;

    @FXML
    private TextArea msg;

    @Override
    public MsgDialog setTime(LocalDateTime time) {
        var timeMsg = TimeUtil.formatLocalDateTime(time);
        this.time.setText(timeMsg);

        // 此组件的 X 坐标应使之位于中间，宽度随时间文本动态调整，但高度应不变
        var centerCoordinateX = this.rightDialog.getPrefWidth() / 2;
        var width = timeMsg.length() * 7 + 5;
        this.time.setLayoutX(centerCoordinateX - width * 1.0 / 2);
        this.time.setPrefWidth(width);

        return this;
    }

    @Override
    public RightDialog setAvatar(byte[] avatarData) {
        this.avatar.setGraphic(WchatUtil.byteArray2ImageView(avatarData, this.avatar.getPrefWidth()));
        return this;
    }

    @Override
    public RightDialog setMsg(Object msg, MsgType msgType) {
        switch (msgType) {
            case TEXT -> {
                var text = (String) msg;
                var size = WchatUtil.calculateDialogSize(text, new Font("微软雅黑", 16),
                        345, 15, 24, 4);
                this.setRightDialogPrefSize(size[0], size[1]);
                this.msg.setText(text);
            }

            default -> {
                // 敬请期待
            }
        }

        return this;
    }

    /**
     * 设置文本框的文本推荐尺寸，同时根据相应调整其父容器的尺寸
     * 如果尺寸太小或太大，将自动调整为 0 或 父容器的尺寸
     *
     * @since 2021-11-20
     * @lastModified 2021-11-22
     */
    public RightDialog setRightDialogPrefSize(double prefWidth, double prefHeight) {
        var size = this.correctTextAreaSize(prefWidth, prefHeight);
        var width = size[0];
        var height = size[1];
        var extension = 10;
        var textAreaRightCoordinateX = this.msg.getLayoutX() + this.msg.getPrefWidth();
        var parentHeight = this.rightDialog.getPrefHeight() - this.msg.getPrefHeight() + prefHeight + extension;

        // 文本框的宽度在调整时，应保持其右上端点 X 坐标应保持不变
        this.msg.setLayoutX(textAreaRightCoordinateX - width);
        this.msg.setPrefSize(width, height);
        // rightDialog 只应该调整高度，宽度不应该变化
        this.rightDialog.setPrefHeight(parentHeight);

        return this;
    }

    /**
     * 纠正宽度与高度值
     *
     * @since 2021-11-22
     */
    private double[] correctTextAreaSize(double prefWidth, double prefHeight) {
        var size = new double[2];
        var parentWidth = this.rightDialog.getPrefWidth();
        var width = Math.min(prefWidth, parentWidth); // 如果 width 太大，自动调整为父容器的 width
        size[0] = width < 0 ? 0 : width; // 如果 width 大小，将调整为 0
        size[1] = prefHeight < 0 ? 0 : prefHeight; // 如果 height 大小，将调整为 0

        return size;
    }

    public static RightDialog getInstance() {
        /**
         * 注意：此路径是以 resources 下 XXX.class 的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(RightDialog.class.getResource("RightDialog.fxml"));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + RightDialog.class);
            exception.printStackTrace();
        }
        RightDialog fxmlController = fxmlLoader.getController();
        node.setUserData(fxmlController);
        return fxmlController;
    }

    @Override
    public Pane getComponent() {
        return this.rightDialog;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 暂时不需要实现
    }

    @FXML
    public void onClickRightDialog(MouseEvent mouseEvent) {
        System.out.println("onClickRightDialog：");
    }
}
