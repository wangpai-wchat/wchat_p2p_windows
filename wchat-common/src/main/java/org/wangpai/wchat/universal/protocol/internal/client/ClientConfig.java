package org.wangpai.wchat.universal.protocol.internal.client;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @since 2022-1-12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public final class ClientConfig {
    private String otherPartyIp;

    private String otherPartyPort;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        if (other instanceof ClientConfig clientConfig) {
            return Objects.equals(this.otherPartyIp, clientConfig.otherPartyIp)
                    && Objects.equals(this.otherPartyPort, clientConfig.otherPartyPort);
        } else {
            return false;
        }
    }

    /**
     * 如果不重写方法 hashCode，HashMap 将认为每个本类对象都是不同的对象。
     * 换句话说，本方法必须保证，当方法 equals 判定两对象相等时，它们的本方法的调用结果也要相等
     *
     * @since 2022-1-12
     */
    @Override
    public int hashCode() {
        return (this.otherPartyIp.hashCode() + this.otherPartyPort.hashCode()) / 2;
    }
}
