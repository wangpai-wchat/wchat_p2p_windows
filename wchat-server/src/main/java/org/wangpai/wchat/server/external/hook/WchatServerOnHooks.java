package org.wangpai.wchat.server.external.hook;

import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;

public interface WchatServerOnHooks {
    WchatFeedback onReceive(WchatEvent event);
}
