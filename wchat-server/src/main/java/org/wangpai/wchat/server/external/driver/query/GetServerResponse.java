package org.wangpai.wchat.server.external.driver.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.universal.driver.WchatResponse;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class GetServerResponse implements WchatResponse {
    private String port; // port 不要设置为整数类型，这会为编程带来很多麻烦
}
