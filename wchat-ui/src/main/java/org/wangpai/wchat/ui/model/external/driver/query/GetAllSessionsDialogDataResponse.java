package org.wangpai.wchat.ui.model.external.driver.query;

import java.util.List;
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
public class GetAllSessionsDialogDataResponse implements WchatResponse {
    private List<SingleSessionDialogData> dialogDataList;
}
