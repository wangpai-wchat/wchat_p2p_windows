package org.wangpai.wchat.universal.handler;

import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.driver.WchatFeedback;

/**
 * @since 2021-12-31
 */
@FunctionalInterface
public interface EventHandler extends Handler {
    /**
     * @return 返回事件的处理结果。这个返回值不是不是必需的。如果不需要返回值，应返回 null，而不是 this
     * @since 2021-12-31
     */
    WchatFeedback handle(WchatEvent event);
}
