package org.wangpai.wchat.ui.view.base;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

public interface FxController extends Initializable {
    /**
     * 约定：此方法返回一个与 FXML 绑定的 controller。
     * 该 controller 中会有一个字段指向此 FXML 生成的 JavaFX 组件，
     * 而此 JavaFX 组件的方法 setUserData 会绑定该 controller。
     * 因此该 controller 与此 JavaFX 组件完成了双向绑定
     *
     * @since 2021-11-5
     * @lastModified 2021-11-16
     */
    static FxController getInstance(String fxmlPath, Class clazz) {
        /**
         * 注意：此路径是以 resources 下 clazz 对应的类所在 模块 及 包 中的文件路径为相对路径。
         * 例如，如果类 XXX 所在模块的包为 xxx，此相对路径的基路径为该模块 resources/xxx/
         */
        FXMLLoader fxmlLoader = new FXMLLoader(clazz.getResource(fxmlPath));
        Node node = null;
        try {
            node = fxmlLoader.load(); // 如果不先调用方法 load，则不能使用方法 getController
        } catch (Exception exception) {
            System.out.println("fxmlLoader.load() 调用失败...." + FxController.class);
            exception.printStackTrace();
        }
        FxController fxmlController = fxmlLoader.getController();
        node.setUserData(fxmlController);
        return fxmlController;
    }

    Node getComponent();

    @Override
    default void initialize(URL location, ResourceBundle resources) {
        // 选实现类按需重写
    }

    /**
     * 此方法需要在确定本 controller 的所有必要字段都初始化之后才手动调用
     */
    default FxController afterConfiguration(Object... paras) {
        return this;
    }
}
