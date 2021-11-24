package org.wangpai.wchat.universal.protocol.internal.ui;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SingleTextMsg {
    private String sessionId;

    private String userId;

    private LocalDateTime time;

    private MsgType msgType;

    private Object msg;
}
