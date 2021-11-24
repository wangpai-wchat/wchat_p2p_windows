package org.wangpai.wchat.handler.ui.query;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wangpai.wchat.persistence.origin.mapper.UserMapper;
import org.wangpai.wchat.persistence.util.Converter;
import org.wangpai.wchat.ui.model.external.driver.query.GetFriendsResponse;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;
import org.wangpai.wchat.universal.driver.WchatRequest;
import org.wangpai.wchat.universal.driver.WchatResponse;
import org.wangpai.wchat.universal.handler.QueryHandler;

@Service
public class UiGetFriendsHandler implements QueryHandler {
    @Autowired
    UserMapper userMapper;

    @Override
    public WchatResponse query(WchatRequest request) {
        var dbUsers = this.userMapper.getContacts();
        var uiUsers = new ArrayList<User>(dbUsers.size());
        // 进行从数据库 User 到 UI User 之间的适配。浅复制
        for (var dbUser : dbUsers) {
            uiUsers.add(Converter.dbUser2UiUser(dbUser));
        }

        return new GetFriendsResponse().setUsers(uiUsers);
    }
}
