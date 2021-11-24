package org.wangpai.wchat.ui.model.functionbar;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.Pane;

public class FunctionsManager {
    private static final Map<FunctionsItem, Pane> FUNCTIONS_FACES = new HashMap<>();

    public static void put(FunctionsItem key, Pane pane) {
        FunctionsManager.FUNCTIONS_FACES.put(key, pane);
    }

    public static boolean containsKey(FunctionsItem key) {
        return FunctionsManager.FUNCTIONS_FACES.containsKey(key);
    }

    public static void removeByKey(FunctionsItem key) {
        FunctionsManager.FUNCTIONS_FACES.remove(key);
    }

    /**
     * 将 FunctionsManager.FUNCTIONS_FACES 中 thisPane 界面设置为可见，其它的界面设为隐藏
     *
     * @since 2021-11-3
     * @return thisPane 是否包含在 ContactsContentManager.PANES 中。
     * 如果不包含，此方法直接返回 false，不做任何事情
     * @Deprecated 2022-1-6 单单使用此方法会导致主界面的图标没有更新。
     * 更新图标需要 WchatMainFace 中的方法，因此本方法只能由 WchatMainFace 来使用
     */
    @Deprecated
    public static boolean switchFunctions(FunctionsItem thisPane) {
        var keys = FunctionsManager.FUNCTIONS_FACES.keySet();

        if (!keys.contains(thisPane)) {
            return false;
        }

        for (var pane : keys) {
            if (pane != thisPane) {
                FunctionsManager.FUNCTIONS_FACES.get(pane).setVisible(false);
            } else {
                FunctionsManager.FUNCTIONS_FACES.get(pane).setVisible(true);
            }
        }

        return true;
    }

    /**
     * 找到一个属性 visible 为 true 的界面并返回。
     *
     * 以下为极端情况，但不应该让它们发生
     * 1.如果没有找到，返回 null
     * 2.如果有多个属性 visible 为 true 的界面，只会返回找到的第一个
     *
     * @since 2021-11-14
     */
    @Deprecated
    public static FunctionsItem whoIsVisible() {
        var pairs = FunctionsManager.FUNCTIONS_FACES.entrySet();
        for (var pair : pairs) {
            if (pair.getValue().isVisible()) {
                return pair.getKey();
            }
        }
        return null;
    }

    /**
     * @since 2021-11-3
     * @return thisPane 是否包含在 ContactsContentManager.PANES 中。
     * 如果不包含，此方法直接返回 false，不做任何事情
     */
    public static boolean switchFunctions(Pane thisPane) {
        var panes = FunctionsManager.FUNCTIONS_FACES.values();
        if (!panes.contains(thisPane)) {
            return false;
        }

        for (var pane : panes) {
            if (pane != thisPane) {
                pane.setVisible(false);
            } else {
                pane.setVisible(true);
            }
        }
        return true;
    }
}
