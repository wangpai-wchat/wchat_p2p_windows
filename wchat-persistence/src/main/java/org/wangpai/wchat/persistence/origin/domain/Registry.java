package org.wangpai.wchat.persistence.origin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class Registry {
    private String name;

    private String value;
}
