package org.wangpai.wchat.ui.view.mainface;

import org.wangpai.wchat.ui.model.external.handler.WchatUiHandlers;
import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;
import org.wangpai.wchat.universal.driver.WchatRequest;
import org.wangpai.wchat.universal.driver.WchatResponse;
import org.wangpai.wchat.universal.handler.EventHandler;
import org.wangpai.wchat.universal.handler.QueryHandler;

/**
 * 对内提供的 API，供主界面之外的组件使用
 *
 * @since 2021-10-29
 */
public abstract class MainFace extends WchatUiHandlers {
    public WchatFeedback onSendChatMsg(WchatEvent event) {
        return this.getOnSendChatMsgHandler().handle(event);
    }

    public MainFace setOnSendChatMsg(EventHandler handler) {
        this.setOnSendChatMsgHandler(handler);
        return this;
    }

    public WchatFeedback onQuit(WchatEvent event) {
        return this.getOnQuitHandler().handle(event);
    }

    public MainFace setOnQuit(EventHandler handler) {
        this.setOnQuitHandler(handler);
        return this;
    }

    public WchatFeedback onUpdateFriendInfo(WchatEvent event) {
        return this.getOnUpdateFriendInfoHandler().handle(event);
    }

    public MainFace setUpdateFriendInfo(EventHandler handler) {
        this.setOnUpdateFriendInfoHandler(handler);
        return this;
    }

    public WchatFeedback onAddFriend(WchatEvent event) {
        return this.getOnAddFriendHandler().handle(event);
    }

    public MainFace setOnAddFriend(EventHandler handler) {
        this.setOnAddFriendHandler(handler);
        return this;
    }

    public WchatFeedback onAddSession(WchatEvent event) {
        return this.getOnAddSessionHandler().handle(event);
    }

    public MainFace setOnAddSession(EventHandler handler) {
        this.setOnAddSessionHandler(handler);
        return this;
    }

    public WchatFeedback onDeleteSession(WchatEvent event) {
        return this.getOnDeleteSessionHandler().handle(event);
    }

    public MainFace setOnDeleteSession(EventHandler handler) {
        this.setOnDeleteSessionHandler(handler);
        return this;
    }

    public WchatFeedback onSearchChats(WchatEvent event) {
        return this.getOnSearchChatsHandler().handle(event);
    }

    public MainFace setOnSearchChats(EventHandler handler) {
        this.setOnSearchChatsHandler(handler);
        return this;
    }

    public WchatFeedback onSearchContacts(WchatEvent event) {
        return this.getOnSearchContactsHandler().handle(event);
    }

    public MainFace setOnSearchContacts(EventHandler handler) {
        this.setOnSearchContactsHandler(handler);
        return this;
    }

    public WchatFeedback onUpdateAvatar(WchatEvent event) {
        return this.getOnUpdateAvatarHandler().handle(event);
    }

    public MainFace setonUpdateAvatar(EventHandler handler) {
        this.setOnUpdateAvatarHandler(handler);
        return this;
    }

    /*************************************/

    public WchatResponse getMe(WchatRequest request) {
        return this.getQueryMeHandler().query(request);
    }

    public MainFace setGetMeWay(QueryHandler handler) {
        this.setQueryMeHandler(handler);
        return this;
    }

    public WchatResponse getFriends(WchatRequest request) {
        return this.getQueryFriendsHandler().query(request);
    }

    public MainFace setGetFriendsWay(QueryHandler handler) {
        this.setQueryFriendsHandler(handler);
        return this;
    }

    public WchatResponse getSessionDialogData(WchatRequest request) {
        return this.getQuerySessionDialogDataHandler().query(request);
    }

    public MainFace setGetSessionDialogDataWay(QueryHandler handler) {
        this.setQuerySessionDialogDataHandler(handler);
        return this;
    }

    public WchatResponse getAllSessionsDialogData(WchatRequest request) {
        return this.getQueryAllSessionsDialogDataHandler().query(request);
    }

    public MainFace setGetAllSessionsDialogDataWay(QueryHandler handler) {
        this.setQueryAllSessionsDialogDataHandler(handler);
        return this;
    }

    public WchatResponse getUserDetailsByUserId(WchatRequest request) {
        return this.getQueryUserDetailsByUserIdHandler().query(request);
    }

    public MainFace setGetUserDetailsByUserIdWay(QueryHandler handler) {
        this.setQueryUserDetailsByUserIdHandler(handler);
        return this;
    }
}
