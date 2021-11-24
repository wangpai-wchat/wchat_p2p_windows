package org.wangpai.wchat.persistence.complex.daodomain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class GetSessionIdResult {
    private String sessionId;

    private String userId;
}
