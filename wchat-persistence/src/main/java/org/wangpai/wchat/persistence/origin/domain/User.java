package org.wangpai.wchat.persistence.origin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class User {
    private String id;

    private String ip;

    private String port; // port 不要设置为整数类型，这会为编程带来很多麻烦

    private String nickname;

    @ToString.Exclude
    private byte[] avatar; // 调用 toString 时，不打印此项，因为这无法打印，强行打印会占据巨大的文字输出面积

    private String remarks;

    private String myContactCode;

    private String otherPartyContactCode;
}