package org.wangpai.wchat.ui.model.functions.chats;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;
import org.wangpai.wchat.universal.protocol.internal.ui.MsgType;

@Accessors(chain = true)
@Getter
@Setter
@ToString
public class SingleMsg {
    private User user;

    private LocalDateTime time;

    private MsgType msgType;

    private Object msg;
}
