package org.wangpai.wchat.persistence.complex.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wangpai.wchat.persistence.origin.mapper.UserMapper;
import org.wangpai.wchat.persistence.util.Converter;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;

@Repository
public class UserRepository {
    @Autowired
    private UserMapper userMapper;

    /**
     * 注意：此方法处理的 User 是 UI User，不是数据库的 User
     *
     * @since 2022-1-16
     */
    public int save(User uiUser) {
        return this.userMapper.save(Converter.uiUser2DbUser(uiUser));
    }

    /**
     * 注意：此方法处理的 User 是 UI User，不是数据库的 User
     *
     * @since 2022-1-16
     */
    public int updateById(User uiUser, String id) {
        return this.userMapper.updateById(Converter.uiUser2DbUser(uiUser), id);
    }

}
