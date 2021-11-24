package org.wangpai.wchat.ui.model.functions.contacts.util;

import lombok.Setter;
import org.wangpai.wchat.ui.view.functions.contacts.ContactsList;
import org.wangpai.wchat.ui.model.functions.contacts.ContactsListItem;

public class ContactsUtil {
    @Setter
    private static ContactsList contactsList;

    public static void cancelOtherSelection(ContactsListItem thisItem) {
        ContactsUtil.contactsList.cancelOtherSelection(thisItem);
    }
}
