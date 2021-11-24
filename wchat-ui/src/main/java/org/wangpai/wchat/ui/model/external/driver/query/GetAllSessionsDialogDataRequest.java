package org.wangpai.wchat.ui.model.external.driver.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.universal.driver.WchatRequest;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class GetAllSessionsDialogDataRequest implements WchatRequest {
    private int begin = 0;

    private int unit = 100;
}
