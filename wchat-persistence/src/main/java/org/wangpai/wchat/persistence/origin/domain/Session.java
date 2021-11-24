package org.wangpai.wchat.persistence.origin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.functions.chats.SessionMode;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Session {
    private String id;

    private SessionMode mode;

    private String userId;

    private boolean disturb;
    
    private int msgReminderCounter;
}
