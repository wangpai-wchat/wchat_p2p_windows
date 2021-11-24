package org.wangpai.wchat.ui.view.mainface;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.wangpai.wchat.ui.model.external.hook.WchatUiOnHooks;
import org.wangpai.wchat.ui.model.external.hook.WchatUiQueryHooks;
import org.wangpai.wchat.ui.model.universal.UiDatabase;
import org.wangpai.wchat.ui.view.wrapper.SceneWrapper;
import org.wangpai.wchat.universal.spring.SpringContext;

/**
 * @since 2022-1-4
 */
public class MainFaceApplication extends Application {
    @Override
    public void start(Stage stage) {
        var springContext = SpringContext.getSpringContext();
        var onHooks = springContext.getBean(WchatUiOnHooks.class);
        var queryHooks = springContext.getBean(WchatUiQueryHooks.class);

        var wchatMainFace = WchatMainFace.getInstance()
                .afterConfiguration(onHooks, queryHooks)
                .getComponent();

        Scene scene = new SceneWrapper(wchatMainFace, stage);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

        UiDatabase.afterUiShow();

        stage.setTitle("wchat");
        /**
         * 注意：不要使用 “new ImageIcon(String path)” 来获取图片。
         * 因为它的读取路径是以 System.getProperty("user.dir") 为基路径，
         * 而这个路径是本工程的绝对路径，还不是本模块的绝对路径！
         * 此外，这个路径也会随模块打成 JAR 包而改变
         *
         * 方法 getResourceAsStream 的路径是以资源目录 resources 为基准的，
         * 且不受模块的限制。这于 xxx.class 中 xxx 是哪个模块的哪个类无关
         */
        var imageString = MainFaceApplication.class.getClassLoader()
                .getResource("system/icons/mainface/wchat.png").toExternalForm();
        stage.getIcons().add(new Image(imageString, 200, 200,
                true, true, false));

        stage.setOnCloseRequest(event -> {
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
