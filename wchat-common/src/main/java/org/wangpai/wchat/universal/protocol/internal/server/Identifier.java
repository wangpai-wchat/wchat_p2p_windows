package org.wangpai.wchat.universal.protocol.internal.server;

import lombok.Getter;
import org.wangpai.wchat.universal.protocol.internal.ui.SingleTextMsg;

public enum Identifier {
    CHAT_MSG_TEXT(String.class),

    SINGLE_TEXT_MSG(SingleTextMsg.class);

    @Getter
    private final Class<?> classType;

    Identifier(Class<?> classType) {
        this.classType = classType;
    }

    @Override
    public String toString() {
        return String.valueOf(this.classType);
    }
}
