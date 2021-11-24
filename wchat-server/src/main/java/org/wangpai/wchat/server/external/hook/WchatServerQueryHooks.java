package org.wangpai.wchat.server.external.hook;

import org.wangpai.wchat.universal.driver.WchatRequest;
import org.wangpai.wchat.universal.driver.WchatResponse;

public interface WchatServerQueryHooks {
    WchatResponse getServerPort(WchatRequest request);
}
