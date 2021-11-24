package org.wangpai.wchat.ui.model.external.driver.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.functions.chats.SessionMode;
import org.wangpai.wchat.universal.driver.WchatEvent;

/**
 * @since 2022-1-17
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class OnAddSessionEvent implements WchatEvent {
    private String sessionId;

    private SessionMode mode;

    private String userId;

    private int msgReminderCounter;

    private boolean disturb;
}
