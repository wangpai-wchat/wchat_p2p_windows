package org.wangpai.wchat.ui.model.universal;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.wangpai.wchat.ui.view.base.UiAdjustable;
import org.wangpai.wchat.ui.view.mainface.MainFace;
import org.wangpai.wchat.ui.view.mainface.WchatMainFace;

/**
 * @since 2021-11-5
 */
public class UiDatabase {
    @Setter
    private static Parent root;

    @Setter(AccessLevel.NONE) // 此字段禁止自动生成 setter 方法，因为已经有了同名的方法了
    @Getter
    private static MainFace externalApi;

    @Setter(AccessLevel.PRIVATE)
    @Getter
    private static WchatMainFace mainFace;

    public static void setExternalApi(MainFace externalApi) {
        UiDatabase.externalApi = externalApi;
        UiDatabase.mainFace = (WchatMainFace) externalApi;
    }

    @Getter
    private static List<UiAdjustable> aftermaths = new LinkedList<>();

    public static void afterUiShow() {
        for (var aftermath : UiDatabase.aftermaths) {
            aftermath.afterUiShow();
        }
    }

    /**
     * 强制主界面元素的更新
     *
     * 暂时的实现算法是，将使用初始界面来挡住
     *
     * @since 2021-11-24
     */
    public static void forceUpdateMainFace() {
        UiDatabase.getElementById("chatsDefaultFace", AnchorPane.class).setVisible(true);
        UiDatabase.getElementById("contactsDefaultFace", AnchorPane.class).setVisible(true);
    }

    /**
     * 通过路径读取图片文件
     *
     * @param path 以 resource 的路径为基准，不需要以 / 为开头
     * @since 2021-11-24
     */
    public static byte[] readImage(String path) {
        /**
         * 方法 getResourceAsStream 的路径是以资源目录 resources 为基准的，
         * 且不受模块的限制。这于 xxx.class 中 xxx 是哪个模块的哪个类无关
         */
        var imageStream = UiDatabase.class.getClassLoader()
                .getResourceAsStream(path);
        if (imageStream == null) {
            System.out.println("--readImage 为 null--：" + path);
        }

        byte[] bytes = new byte[0];
        try {
            bytes = WchatUtil.inputStream2byteArray(imageStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * 慎用此方法
     *
     * @since 2021-11-5
     */
    public static <T> T getElementById(String id, Class<T> realType) {
        return (T) UiDatabase.getElementById(id);
    }

    /**
     * 慎用此方法
     *
     * @since 2021-11-5
     */
    public static Node getElementById(String id) {
        return UiDatabase.lookup("#" + id);
    }

    /**
     * 慎用此方法
     *
     * @since 2021-11-5
     */
    public static Set<Node> getElementByIds(String id) {
        return UiDatabase.lookupAll("#" + id);
    }

    /**
     * 慎用此方法
     *
     * @since 2021-11-5
     */
    public static Node lookup(String selector) {
        return UiDatabase.root.lookup(selector);
    }

    /**
     * 慎用此方法
     *
     * @since 2021-11-5
     */
    public static <T> T lookup(String selector, Class<T> realType) {
        return (T) UiDatabase.lookup(selector);
    }

    /**
     * 慎用此方法
     *
     * @since 2021-11-5
     */
    public static Set<Node> lookupAll(String id) {
        return UiDatabase.root.lookupAll(id);
    }
}
