package org.wangpai.wchat.ui.view.wrapper;

import java.util.Set;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * @since 2021-10-24
 */
public class SceneWrapper extends Scene {
    @Getter
    final private Stage stage;

    private double dx;
    private double dy;

    public SceneWrapper(Parent root, Stage stage) {
        super(root);
        this.stage = stage;
        this.setMouseDragged();
    }

    public <T> T getElementById(String id, Class<T> realType) {
        return (T) this.getElementById(id);
    }

    public Node getElementById(String id) {
        return this.lookup("#" + id);
    }

    public Set<Node> getElementByIds(String id) {
        return this.lookupAll("#" + id);
    }

    @Override
    public Node lookup(String selector) {
        return super.lookup(selector);
    }

    public <T> T lookup(String selector, Class<T> realType) {
        return (T) this.lookup(selector);
    }

    public Set<Node> lookupAll(String id) {
        return super.getRoot().lookupAll(id);
    }

    /**
     * 设置鼠标拖拽功能
     *
     * @since 2021-10-24
     */
    public void setMouseDragged() {
        super.getRoot().setOnMousePressed(event -> {
            this.dx = this.stage.getX() - event.getScreenX();
            this.dy = this.stage.getY() - event.getScreenY();
            super.getRoot().setCursor(Cursor.CLOSED_HAND);
        });
        super.getRoot().setOnMouseDragged(event -> {
            this.stage.setX(event.getScreenX() + this.dx);
            this.stage.setY(event.getScreenY() + this.dy);
        });
        super.getRoot().setOnMouseReleased(event -> {
            super.getRoot().setCursor(Cursor.DEFAULT);
        });
    }
}
