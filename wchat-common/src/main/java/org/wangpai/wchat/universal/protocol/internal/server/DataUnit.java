package org.wangpai.wchat.universal.protocol.internal.server;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class DataUnit {
    private Identifier identifier;

    private Object data;
}
