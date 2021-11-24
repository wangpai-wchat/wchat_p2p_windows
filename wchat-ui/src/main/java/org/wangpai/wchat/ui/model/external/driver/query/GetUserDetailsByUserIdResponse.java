package org.wangpai.wchat.ui.model.external.driver.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.functions.chats.SessionMode;
import org.wangpai.wchat.universal.driver.WchatResponse;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class GetUserDetailsByUserIdResponse implements WchatResponse {
    private String userId;

    private String ip;

    private String port; // port 不要设置为整数类型，这会为编程带来很多麻烦

    private String nickname;

    @ToString.Exclude
    private byte[] avatar; // 调用 toString 时，不打印此项，因为这无法打印，强行打印会占据巨大的文字输出面积

    private String remarks;

    private String sessionId;

    private SessionMode sessionMode;

    private int msgReminderCounter;

    private boolean disturb;

    private String myContactCode;

    private String otherPartyContactCode;
}
