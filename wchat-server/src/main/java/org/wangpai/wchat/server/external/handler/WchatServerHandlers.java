package org.wangpai.wchat.server.external.handler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.wangpai.wchat.server.external.hook.WchatServerOnHooks;
import org.wangpai.wchat.server.external.hook.WchatServerQueryHooks;
import org.wangpai.wchat.universal.handler.EventHandler;
import org.wangpai.wchat.universal.handler.QueryHandler;

@Setter(AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
@Accessors(chain = true)
public abstract class WchatServerHandlers {
    /*-----------------------------------*/

    private EventHandler onReceiveHandler;

    /*************************************/

    /*-----------------------------------*/

    private QueryHandler queryServerPortHandler;

    /*************************************/

    public WchatServerHandlers setWchatServerOnHooks(WchatServerOnHooks onHooks) {
        this.onReceiveHandler = onHooks::onReceive;

        return this;
    }

    public WchatServerHandlers setWchatServerQueryHooks(WchatServerQueryHooks queryHooks) {
        this.queryServerPortHandler = queryHooks::getServerPort;

        return this;
    }
}
