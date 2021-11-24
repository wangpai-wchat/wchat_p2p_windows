package org.wangpai.wchat.ui.model.external.handler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.external.hook.WchatUiOnHooks;
import org.wangpai.wchat.ui.model.external.hook.WchatUiQueryHooks;
import org.wangpai.wchat.universal.handler.EventHandler;
import org.wangpai.wchat.universal.handler.QueryHandler;

/**
 * @since 2021-10-30
 */
@Setter(AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
@Accessors(chain = true)
public abstract class WchatUiHandlers {
    /*-----------------------------------*/

    private EventHandler onSendChatMsgHandler;

    private EventHandler onQuitHandler;

    private EventHandler onAddFriendHandler;

    private EventHandler onUpdateFriendInfoHandler;

    private EventHandler onAddSessionHandler;

    private EventHandler onDeleteSessionHandler;

    private EventHandler onSearchChatsHandler;

    private EventHandler onSearchContactsHandler;

    private EventHandler onUpdateAvatarHandler;

    /*************************************/

    /*-----------------------------------*/

    private QueryHandler queryMeHandler;

    private QueryHandler queryFriendsHandler;

    private QueryHandler querySessionDialogDataHandler;

    private QueryHandler queryAllSessionsDialogDataHandler;

    private QueryHandler queryUserDetailsByUserIdHandler;

    /*************************************/

    public WchatUiHandlers setWchatUiOnHooks(WchatUiOnHooks hooks) {
        this.onSendChatMsgHandler = hooks::onSendChatMsg;
        this.onQuitHandler = hooks::onQuit;
        this.onAddFriendHandler = hooks::onAddFriend;
        this.onUpdateFriendInfoHandler = hooks::onUpdateFriendInfo;
        this.onAddSessionHandler = hooks::onAddSession;
        this.onDeleteSessionHandler = hooks::onDeleteSession;
        this.onSearchChatsHandler = hooks::onSearchChats;
        this.onSearchContactsHandler = hooks::onSearchContacts;
        this.onUpdateAvatarHandler = hooks::onUpdateAvatar;

        return this;
    }

    public WchatUiHandlers setWchatUiQueryHooks(WchatUiQueryHooks hooks) {
        this.queryMeHandler = hooks::getMe;
        this.queryFriendsHandler = hooks::getFriends;
        this.querySessionDialogDataHandler = hooks::getSessionDialogData;
        this.queryAllSessionsDialogDataHandler = hooks::getAllSessionsDialogData;
        this.queryUserDetailsByUserIdHandler = hooks::getUserDetailsByUserId;

        return this;
    }
}
