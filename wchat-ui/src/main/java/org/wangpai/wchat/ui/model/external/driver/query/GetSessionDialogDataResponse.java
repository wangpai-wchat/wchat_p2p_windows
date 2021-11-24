package org.wangpai.wchat.ui.model.external.driver.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.functions.chats.SingleSessionDialogData;
import org.wangpai.wchat.universal.driver.WchatResponse;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class GetSessionDialogDataResponse implements WchatResponse {
    private SingleSessionDialogData dialogData;
}
