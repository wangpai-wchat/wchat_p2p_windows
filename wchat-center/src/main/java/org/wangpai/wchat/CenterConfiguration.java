package org.wangpai.wchat;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.wangpai.wchat.handler.server.event.ServerOnReceiveHandler;
import org.wangpai.wchat.handler.server.query.ServerGetServerPortHandler;
import org.wangpai.wchat.handler.ui.event.UiOnAddFriendHandler;
import org.wangpai.wchat.handler.ui.event.UiOnAddSessionHandler;
import org.wangpai.wchat.handler.ui.event.UiOnQuitHandler;
import org.wangpai.wchat.handler.ui.event.UiOnSendChatMsgHandler;
import org.wangpai.wchat.handler.ui.event.UiOnUpdateFriendInfoHandler;
import org.wangpai.wchat.handler.ui.query.UiGetAllSessionsDialogDataHandler;
import org.wangpai.wchat.handler.ui.query.UiGetFriendsHandler;
import org.wangpai.wchat.handler.ui.query.UiGetMeHandler;
import org.wangpai.wchat.handler.ui.query.UiGetSessionDialogDatahandler;
import org.wangpai.wchat.handler.ui.query.UiGetUserDetailsByUserIdHandler;
import org.wangpai.wchat.server.external.hook.WchatServerOnHooks;
import org.wangpai.wchat.server.external.hook.WchatServerQueryHooks;
import org.wangpai.wchat.ui.model.external.hook.WchatUiOnHooks;
import org.wangpai.wchat.ui.model.external.hook.WchatUiQueryHooks;
import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;
import org.wangpai.wchat.universal.driver.WchatRequest;
import org.wangpai.wchat.universal.driver.WchatResponse;

/**
 * 此类是为解决单例模式下的 Spring 循环依赖导致 Bean 创建失败而独立出来的类。
 * 因此，此类不能依赖有复杂关系的 Bean
 *
 * @since 2022-1-5
 */
@Getter
@Accessors(chain = true)
@Configuration
public class CenterConfiguration {
    @Autowired
    private UiOnSendChatMsgHandler uiOnSendChatMsgHandler;

    @Autowired
    private UiOnQuitHandler uiOnQuitHandler;

    @Autowired
    private UiOnAddFriendHandler uiOnAddFriendHandler;

    @Autowired
    private UiOnUpdateFriendInfoHandler uiOnUpdateFriendInfoHandler;

    @Autowired
    private UiOnAddSessionHandler uiOnAddSessionHandler;

    // ---------------------

    @Autowired
    private UiGetMeHandler uiGetMeHandler;

    @Autowired
    private UiGetFriendsHandler uiGetFriendsHandler;

    @Autowired
    private UiGetUserDetailsByUserIdHandler uiGetUserDetailsByUserIdHandler;

    @Autowired
    private UiGetAllSessionsDialogDataHandler uiGetAllSessionsDialogDataHandler;

    @Autowired
    private UiGetSessionDialogDatahandler uiGetSessionDialogDatahandler;

    // ---------------------

    @Autowired
    private ServerOnReceiveHandler serverOnReceiveHandler;

    // ---------------------

    @Autowired
    private ServerGetServerPortHandler serverGetServerPortHandler;

    @Bean(name = "wchatUiOnHooks")
    public WchatUiOnHooks wchatUiOnHooks() {
        return new WchatUiOnHooks() {
            @Override
            public WchatFeedback onSendChatMsg(WchatEvent event) {
                return CenterConfiguration.this.getUiOnSendChatMsgHandler().handle(event);
            }

            @Override
            public WchatFeedback onQuit(WchatEvent event) {
                return CenterConfiguration.this.getUiOnQuitHandler().handle(event);
            }

            @Override
            public WchatFeedback onAddFriend(WchatEvent event) {
                return CenterConfiguration.this.getUiOnAddFriendHandler().handle(event);
            }

            @Override
            public WchatFeedback onUpdateFriendInfo(WchatEvent event) {
                return CenterConfiguration.this.getUiOnUpdateFriendInfoHandler().handle(event);
            }

            @Override
            public WchatFeedback onAddSession(WchatEvent event) {
                return CenterConfiguration.this.getUiOnAddSessionHandler().handle(event);
            }

            @Override
            public WchatFeedback onDeleteSession(WchatEvent event) {
                System.out.println("--onDeleteSession--");
                return null;
            }

            @Override
            public WchatFeedback onSearchChats(WchatEvent event) {
                System.out.println("--onSearchChats--");
                return null;
            }

            @Override
            public WchatFeedback onSearchContacts(WchatEvent event) {
                System.out.println("--onSearchContacts--");
                return null;
            }

            @Override
            public WchatFeedback onUpdateAvatar(WchatEvent event) {
                System.out.println("--onUpdateAvatar--");
                return null;
            }
        };
    }

    @Bean(name = "wchatUiQueryHooks")
    public WchatUiQueryHooks wchatUiQueryHooks() {
        return new WchatUiQueryHooks() {
            @Override
            public WchatResponse getMe(WchatRequest request) {
                return CenterConfiguration.this.getUiGetMeHandler().query(request);
            }

            @Override
            public WchatResponse getFriends(WchatRequest request) {
                return CenterConfiguration.this.getUiGetFriendsHandler().query(request);
            }

            @Override
            public WchatResponse getAllSessionsDialogData(WchatRequest request) {
                return CenterConfiguration.this.getUiGetAllSessionsDialogDataHandler().query(request);
            }

            @Override
            public WchatResponse getSessionDialogData(WchatRequest request) {
                return CenterConfiguration.this.getUiGetSessionDialogDatahandler().query(request);
            }

            @Override
            public WchatResponse getUserDetailsByUserId(WchatRequest request) {
                return CenterConfiguration.this.getUiGetUserDetailsByUserIdHandler().query(request);
            }
        };
    }

    @Lazy
    @Bean
    public WchatServerOnHooks wchatServerOnHooks() {
        return new WchatServerOnHooks() {
            @Override
            public WchatFeedback onReceive(WchatEvent event) {
                return CenterConfiguration.this.getServerOnReceiveHandler().handle(event);
            }
        };
    }

    @Lazy
    @Bean
    public WchatServerQueryHooks wchatServerQueryHooks() {
        return new WchatServerQueryHooks() {
            @Override
            public WchatResponse getServerPort(WchatRequest request) {
                return CenterConfiguration.this.getServerGetServerPortHandler().query(request);
            }
        };
    }
}
