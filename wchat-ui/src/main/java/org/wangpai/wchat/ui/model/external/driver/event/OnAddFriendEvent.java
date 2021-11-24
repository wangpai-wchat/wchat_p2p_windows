package org.wangpai.wchat.ui.model.external.driver.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;
import org.wangpai.wchat.universal.driver.WchatEvent;

/**
 * @since 2022-1-16
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class OnAddFriendEvent implements WchatEvent {
    private User user;
}
