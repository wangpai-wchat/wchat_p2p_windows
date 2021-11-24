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
public class GetUserDetailsByUserIdRequest implements WchatRequest {
    private String userId;
}
