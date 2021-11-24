package org.wangpai.wchat.universal.protocol.internal.server;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class ContactCode {
    /**
     * 我的通信暗号。我指的是发送 ContactCode 对象的一方
     *
     * @since 2022-1-6
     */
    private String myContactCode;

    /**
     * 对方的通信暗号。对方指的是接收 ContactCode 对象的一方，
     *
     * @since 2022-1-6
     */
    private String otherPartyContactCode;
}
