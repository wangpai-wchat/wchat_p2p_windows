package org.wangpai.wchat.server.external.driver.event;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.universal.driver.WchatEvent;
import org.wangpai.wchat.universal.protocol.internal.client.ClientConfig;
import org.wangpai.wchat.universal.protocol.internal.server.ContactCode;
import org.wangpai.wchat.universal.protocol.internal.server.DataUnit;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class OnReceiveEvent implements WchatEvent {
    private DataUnit dataUnit;

    private LocalDateTime dateTime;

    private ContactCode contactCode;

    private ClientConfig clientConfig;
}
