package org.wangpai.wchat.server;

import org.wangpai.wchat.server.external.handler.WchatServerHandlers;
import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;
import org.wangpai.wchat.universal.driver.WchatRequest;
import org.wangpai.wchat.universal.driver.WchatResponse;
import org.wangpai.wchat.universal.handler.EventHandler;
import org.wangpai.wchat.universal.handler.QueryHandler;

/**
 * @since 2022-1-1
 */
public abstract class OnServerAction extends WchatServerHandlers {
    public WchatFeedback onReceive(WchatEvent event) {
        return this.getOnReceiveHandler().handle(event);
    }

    public OnServerAction setOnReceive(EventHandler handler) {
        this.setOnReceiveHandler(handler);
        return this;
    }

    public WchatResponse getServerPort(WchatRequest request) {
        return this.getQueryServerPortHandler().query(request);
    }

    public OnServerAction setGetServerPortWay(QueryHandler handler) {
        this.setQueryServerPortHandler(handler);
        return this;
    }
}
