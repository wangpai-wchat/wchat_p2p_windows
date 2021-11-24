package org.wangpai.wchat.ui.model.functions.chats;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@ToString
public class SingleSessionDialogData {
    private String sessionId;

    @ToString.Exclude
    private byte[] avatar; // 调用 toString 时，不打印此项，因为这无法打印，强行打印会占据巨大的文字输出面积

    private String name; // 对方的名称

    private int msgReminderCounter;

    private boolean disturb; // 是否允许打扰

    private List<SingleMsg> dialogRecords; // 规定：越新的数据的 list 序号越小
}
