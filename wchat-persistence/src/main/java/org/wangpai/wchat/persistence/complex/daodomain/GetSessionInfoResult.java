package org.wangpai.wchat.persistence.complex.daodomain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.functions.chats.SessionMode;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class GetSessionInfoResult {
    private String sessionId;

    private SessionMode mode;

    private int msgReminderCounter;

    private boolean disturb; // 是否允许打扰

    //---------------

    private String userId;

    private String ip;

    private String port;

    private String nickname;

    @ToString.Exclude
    private byte[] avatar; // 调用 toString 时，不打印此项，因为这无法打印，强行打印会占据巨大的文字输出面积

    private String remarks;

    private String myContactCode;

    private String otherPartyContactCode;
}
