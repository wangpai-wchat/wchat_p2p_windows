package org.wangpai.wchat.ui.model.external.driver.event;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.protocol.internal.server.ContactCode;
import org.wangpai.wchat.universal.protocol.internal.ui.MsgType;

/**
 * @since 2022-1-5
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class OnSendChatMsgEvent implements WchatEvent {
    private String sessionId;

    /**
     * 虽然发消息的一定是自己，看起来没有必要提供，但此处就提供 userId 对其它方法来说很方便
     *
     * @since 2022-1-13
     */
    private String userId;

    private ContactCode contactCode;

    private MsgType msgType;

    private Object msg;

    private LocalDateTime time;
}
