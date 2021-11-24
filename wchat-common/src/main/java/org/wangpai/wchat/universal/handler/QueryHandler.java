package org.wangpai.wchat.universal.handler;

import org.wangpai.wchat.universal.driver.WchatRequest;
import org.wangpai.wchat.universal.driver.WchatResponse;

/**
 * 要求：本处理器 QueryHandler 对数据库来说是只读的，它一定不会涉及对数据的更新
 *
 * @since 2021-11-24
 */
@FunctionalInterface
public interface QueryHandler extends Handler {
    WchatResponse query(WchatRequest request);
}
