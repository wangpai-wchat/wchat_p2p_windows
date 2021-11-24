package org.wangpai.wchat.ui.model.external.driver.query;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;
import org.wangpai.wchat.universal.driver.WchatResponse;

@Setter
@Getter
@ToString
@Accessors(chain = true)
public class GetFriendsResponse implements WchatResponse {
    private List<User> users;
}

