package org.wangpai.wchat.persistence.complex.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.wangpai.wchat.ui.model.external.driver.query.GetUserDetailsByUserIdResponse;
import org.wangpai.wchat.ui.model.functions.contacts.friends.User;
import org.wangpai.wchat.universal.protocol.internal.client.ClientConfig;

/**
 * @since 2022-1-8
 */
@Mapper
public interface UserDao {
    /**
     * 此查询使用了外连接以 User 中的信息为主
     *
     * @since 2022-1-9
     */
    @Select(value = "SELECT User.id userId, ip, port, nickname, avatar, remarks, " +
            "myContactCode, otherPartyContactCode," +
            "Session.id sessionId, Session.mode sessionMode, msgReminderCounter, disturb " +
            "FROM User LEFT JOIN Session ON User.id = Session.userId " +
            "WHERE User.id=#{userId}")
    GetUserDetailsByUserIdResponse getUserDetailsByUserId(String userId);

    /**
     * 注意：此方法的返回值是 UI User，不是数据库 User
     *
     * @since 2022-1-17
     */
    @Select(value = "SELECT * FROM User WHERE ip=#{otherPartyIp} AND port=#{otherPartyPort}")
    User getUserByClientConfig(ClientConfig clientConfig);
}
