package org.wangpai.wchat.ui.model.external.hook;

import org.wangpai.wchat.universal.driver.WchatRequest;
import org.wangpai.wchat.universal.driver.WchatResponse;

/**
 * @since 2021-11-24
 */
public interface WchatUiQueryHooks {
    /**
     * @since 2022-1-9
     */
    WchatResponse getMe(WchatRequest request);

    /**
     * @since 2022-1-9
     */
    WchatResponse getFriends(WchatRequest request);

    /**
     * @since 2022-1-9
     */
    WchatResponse getSessionDialogData(WchatRequest request);

    /**
     * @since 2022-1-9
     */
    WchatResponse getAllSessionsDialogData(WchatRequest request);

    /**
     * @since 2022-1-9
     */
    WchatResponse getUserDetailsByUserId(WchatRequest request);
}
