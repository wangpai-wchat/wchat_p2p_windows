package org.wangpai.wchat.ui.model.functions.contacts.friends;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@ToString
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

    public String getName() {
        return this.getNickname();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        if (other instanceof User) {
            return this.equals((User) other);
        }

        return false;
    }

    public boolean equals(User other) {
        return Objects.equals(this.ip, other.ip) &&
                Objects.equals(this.port, other.port) &&
                Objects.equals(this.nickname, other.nickname);
    }
}
