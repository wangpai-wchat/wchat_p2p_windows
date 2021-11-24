package org.wangpai.wchat.ui.model.external.hook;

import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;

/**
 * @since 2021-11-24
 */
public interface WchatUiOnHooks {
    /**
     * @since 2021-11-24
     */
    WchatFeedback onSendChatMsg(WchatEvent event);

    /**
     * @since 2021-11-24
     */
    WchatFeedback onQuit(WchatEvent event);

    /**
     * @since 2021-11-24
     */
    WchatFeedback onAddFriend(WchatEvent event);

    /**
     * @since 2022-1-16
     */
    WchatFeedback onUpdateFriendInfo(WchatEvent event);

    /**
     * @since 2022-1-17
     */
    WchatFeedback onAddSession(WchatEvent event);

    /**
     * @since 2021-11-24
     */
    WchatFeedback onDeleteSession(WchatEvent event);

    /**
     * @since 2021-11-24
     */
    WchatFeedback onSearchChats(WchatEvent event);

    /**
     * @since 2021-11-24
     */
    WchatFeedback onSearchContacts(WchatEvent event);

    /**
     * @since 2021-11-24
     */
    WchatFeedback onUpdateAvatar(WchatEvent event);
}
