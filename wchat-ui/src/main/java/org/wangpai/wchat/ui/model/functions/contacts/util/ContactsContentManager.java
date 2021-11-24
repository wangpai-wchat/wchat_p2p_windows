package org.wangpai.wchat.ui.model.functions.contacts.util;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.Pane;
import org.wangpai.wchat.ui.model.functions.contacts.ContactsListItem;

public class ContactsContentManager {
    private static final Map<ContactsListItem, Pane> CONTENTS = new HashMap<>();

    public static void put(ContactsListItem key, Pane pane) {
        ContactsContentManager.CONTENTS.put(key, pane);
    }

    public static boolean containsKey(ContactsListItem key) {
        return ContactsContentManager.CONTENTS.containsKey(key);
    }

    public static void removeByKey(ContactsListItem key) {
        ContactsContentManager.CONTENTS.remove(key);
    }

    /**
     * @since 2021-11-3
     * @return target 是否包含在 ContactsContentManager.PANES 中。
     * 如果不包含，此方法直接返回 false，不做任何事情
     */
    public static boolean switchContent(Pane target) {
        var panes = ContactsContentManager.CONTENTS.values();
        if (!panes.contains(target)) {
            return false;
        }

        for (var pane : panes) {
            if (pane != target) {
                pane.setVisible(false);
            } else {
                pane.setVisible(true);
            }
        }
        return true;
    }
}
