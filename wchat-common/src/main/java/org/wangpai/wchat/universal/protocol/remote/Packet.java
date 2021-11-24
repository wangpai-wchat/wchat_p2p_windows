package org.wangpai.wchat.universal.protocol.remote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.universal.protocol.internal.server.ContactCode;
import org.wangpai.wchat.universal.protocol.internal.server.Identifier;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Packet {
    /**
     * 这个端口号指的是发送方（发送 Packet 对象的一方）的服务器的端口号
     *
     * @since 2022-1-6
     */
    private String port; // port 不要设置为整数类型，这会为编程带来很多麻烦

    /**
     * 发送方视角下的通信暗号
     *
     * @since 2022-1-6
     */
    private ContactCode contactCode;

    /**
     * 这指的是发送方当初生成所携带的数据的时间
     *
     * @since 2022-1-13
     */
    private LocalDateTime dataTime;

    /**
     * 这指的是 carrier 经过多级解码后，最终所对应的 Java 对象的类型。
     * 不是 carrier 使用的数据载体的类型（如 JSON、ProtoBuf 等）
     *
     * @since 2022-1-5
     */
    private Identifier carryingType;

    private String carrier;

    /**
     * 因为 get 方法会干扰 JackSon 的序列化和反序列化。注解 @JsonIgnore 可排除这种干扰
     *
     * @since 2022-1-6
     */
    @JsonIgnore
    public Class<?> getClassType() {
        return this.carryingType.getClassType();
    }
}
