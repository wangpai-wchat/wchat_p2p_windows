package org.wangpai.wchat.ui.model.functions.chats;

import java.time.LocalDateTime;
import org.wangpai.wchat.ui.view.base.FxController;
import org.wangpai.wchat.universal.protocol.internal.ui.MsgType;

/**
 * @since 2021-11-24
 */
public interface MsgDialog extends FxController {
    MsgDialog setTime(LocalDateTime time);

    MsgDialog setAvatar(byte[] avatar);

    /**
     * 有的类需要单独设置名称，有的不需要
     *
     * @since 2021-11-16
     */
    default MsgDialog setName(String name) {
        return this;
    }

    /**
     * @since 2021-11-24
     */
    MsgDialog setMsg(Object msg, MsgType msgType);

}
