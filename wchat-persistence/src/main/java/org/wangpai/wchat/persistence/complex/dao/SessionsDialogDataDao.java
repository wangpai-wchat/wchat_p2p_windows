package org.wangpai.wchat.persistence.complex.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.wangpai.wchat.persistence.complex.daodomain.SingleDialogData;
import org.wangpai.wchat.ui.model.external.driver.query.GetSessionDialogDataRequest;

/**
 * @since 2022-1-9
 */
@Mapper
public interface SessionsDialogDataDao {
    /**
     * 注意：此处 SQL 的多表连接中，是表 SessionsDialogData 分别与 Session、User 相连，
     * 而不是先将 User 与 Session 相连，然后与 SessionsDialogData 相连
     *
     * @since 2022-1-10
     */
    @Select(value = "SELECT *, SessionsDialogData.id AS sessionsDialogDataId, " +
            "Session.id AS sessionId, User.id AS userId " +
            "FROM SessionsDialogData, Session, User " +
            "WHERE SessionsDialogData.sessionId=Session.id " +
            "AND SessionsDialogData.userId=User.id " +
            "AND SessionsDialogData.sessionId=#{sessionId} " +
            "ORDER BY SessionsDialogData.time DESC LIMIT #{begin}, #{unit}")
    List<SingleDialogData> getSessionDialogData(GetSessionDialogDataRequest request);
}









