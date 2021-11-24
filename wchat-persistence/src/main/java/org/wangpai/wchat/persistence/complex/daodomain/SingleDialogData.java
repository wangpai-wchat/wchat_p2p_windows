package org.wangpai.wchat.persistence.complex.daodomain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.functions.chats.SessionMode;
import org.wangpai.wchat.universal.protocol.internal.ui.MsgType;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SingleDialogData {
    private String sessionsDialogDataId;

    private String sessionId;

    private String userId;

    private LocalDateTime time;

    private MsgType msgType;

    private String textMsg;

    @ToString.Exclude
    private byte[] binaryMsg; // 调用 toString 时，不打印此项，因为这无法打印，强行打印会占据巨大的文字输出面积

    //----------------

    private SessionMode mode;

    private int msgReminderCounter;

    private boolean disturb; // 是否允许打扰

    //---------------

    private String ip;

    private String port;

    private String nickname;

    @ToString.Exclude
    private byte[] avatar; // 调用 toString 时，不打印此项，因为这无法打印，强行打印会占据巨大的文字输出面积

    private String remarks;

    private String myContactCode;

    private String otherPartyContactCode;
}
