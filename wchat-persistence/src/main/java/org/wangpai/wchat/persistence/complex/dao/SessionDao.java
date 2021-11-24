package org.wangpai.wchat.persistence.complex.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.wangpai.wchat.persistence.complex.daodomain.GetSessionIdResult;
import org.wangpai.wchat.persistence.complex.daodomain.GetSessionInfoResult;
import org.wangpai.wchat.universal.protocol.internal.client.ClientConfig;

/**
 * @since 2022-1-5
 */
@Mapper
public interface SessionDao {
    /**
     * 此接口是用于获取会话中对方的信息，不能用于查询关于自己的信息
     *
     * @since 2022-1-5
     */
    @Select(value = "SELECT User.ip otherPartyIp,User.port otherPartyPort " +
            "FROM Session INNER JOIN User ON Session.userId = User.id " +
            "WHERE Session.id=#{sessionId}")
    ClientConfig getClientConfig(String sessionId);

    /**
     * 此接口是用于获取会话中对方的信息，不能用于查询关于自己的信息
     *
     * 这里使用了内连接
     *
     * @since 2022-1-5
     */
    @Select(value = "SELECT Session.id sessionId, Session.userId userId " +
            "FROM Session INNER JOIN User ON Session.userId = User.id " +
            "WHERE User.ip=#{otherPartyIp} AND User.port=#{otherPartyPort}")
    GetSessionIdResult getSessionId(ClientConfig clientConfig);

    /**
     * 得到所有 Session 的信息。本方法使用的是内连接
     *
     * @since 2022-1-10
     */
    @Select(value = "SELECT *, Session.id AS sessionId, User.id AS userId " +
            "FROM Session INNER JOIN User ON Session.userId = User.id")
    List<GetSessionInfoResult> getAllSessionsInfo();

    /**
     * 得到所有 Session 的信息。本方法使用的是内连接
     *
     * @since 2022-1-10
     */
    @Select(value = "SELECT *, Session.id AS sessionId, User.id AS userId " +
            "FROM Session INNER JOIN User ON Session.userId = User.id " +
            "WHERE Session.id=#{sessionId}")
    GetSessionInfoResult getSessionInfo(String sessionId);
}
