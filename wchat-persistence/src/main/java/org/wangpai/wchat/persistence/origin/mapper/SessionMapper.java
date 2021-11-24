package org.wangpai.wchat.persistence.origin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangpai.wchat.persistence.origin.domain.Session;

@Mapper
public interface SessionMapper {
    /**
     * 如果没有搜索到，此方法会返回 null，而不是抛出异常
     *
     * @since 2021-12-30
     */
    @Select(value = "SELECT * FROM session WHERE id = #{id}")
    Session search(String id);

    @Insert(value = "INSERT INTO session (id, mode, userId, msgReminderCounter, disturb) " +
            "VALUES (#{id}, #{mode}, #{userId}, #{msgReminderCounter}, #{disturb})")
    int save(Session session);

    @Update(value = "UPDATE session SET " +
            "id=#{session.id}, mode=#{session.mode},userId=#{session.userId}," +
            "msgReminderCounter=#{session.msgReminderCounter},disturb=#{session.disturb} " +
            "WHERE id=#{id}")
    int updateById(@Param("session") Session session, @Param("id") String id);

    /**
     * 如果实际没有删除到任何数据，此方法会返回 0，而不是抛出异常
     *
     * @since 2021-12-28
     */
    @Delete(value = "DELETE FROM session WHERE id = #{id}")
    int delete(String id);

    @Select(value = "SELECT * FROM Session")
    List<Session> getSessions();
}
