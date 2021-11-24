package org.wangpai.wchat.persistence.origin.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.universal.protocol.internal.ui.MsgType;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SessionsDialogData {
    private String id;

    private String userId;

    private String sessionId;

    private LocalDateTime time;

    private MsgType msgType;

    private String textMsg;

    @ToString.Exclude
    private byte[] binaryMsg; // 调用 toString 时，不打印此项，因为这无法打印，强行打印会占据巨大的文字输出面积
}
