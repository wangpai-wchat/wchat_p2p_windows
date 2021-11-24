package org.wangpai.wchat.persistence.origin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangpai.wchat.persistence.origin.domain.SessionsDialogData;

@Mapper
public interface SessionsDialogDataMapper {
    /**
     * 如果没有搜索到，此方法会返回 null，而不是抛出异常
     *
     * @since 2021-12-30
     */
    @Select(value = "SELECT * FROM SessionsDialogData WHERE id = #{id}")
    SessionsDialogData search(String id);

    @Insert(value = "INSERT INTO SessionsDialogData (id,userId,sessionId,time,msgType,textMsg,binaryMsg) " +
            "VALUES (#{id},#{userId},#{sessionId},#{time},#{msgType},#{textMsg},#{binaryMsg})")
    int save(SessionsDialogData singleDialogData);

    @Update(value = "UPDATE SessionsDialogData SET " +
            "id=#{singleDialogData.id},userId=#{singleDialogData.userId},sessionId=#{singleDialogData.sessionId}" +
            "time=#{singleDialogData.time},msgType=#{singleDialogData.msgType}," +
            "textMsg=#{singleDialogData.textMsg},binaryMsg=#{singleDialogData.binaryMsg} " +
            "WHERE id=#{id}")
    int updateById(@Param("singleDialogData") SessionsDialogData singleDialogData, @Param("id") String id);

    /**
     * 如果实际没有删除到任何数据，此方法会返回 0，而不是抛出异常
     *
     * @since 2021-12-30
     */
    @Delete(value = "DELETE FROM SessionsDialogData WHERE id = #{id}")
    int delete(String id);

    /**
     * 如果没有搜索到，此方法会返回 null，而不是抛出异常
     *
     * 查询最近的所有 session 的消息记录
     *
     * 行号从 1 开始。此查询将选取没有 LIMIT 语句时的第 [begin + 1, begin + unit] 行的数据
     *
     * @since 2021-12-30
     * @lastModified 2022-1-8
     */
    @Select(value = "SELECT * FROM SessionsDialogData ORDER BY time DESC LIMIT #{begin}, #{unit}")
    List<SessionsDialogData> getAllSessionsDialogData(int begin, int unit);

    /**
     * 如果没有搜索到，此方法会返回 null，而不是抛出异常
     *
     * 查询 sessionId 为 {#sessionId} 的那个 session 的最近消息记录
     *
     * 行号从 1 开始。此查询将选取没有 LIMIT 语句时的第 [begin + 1, begin + unit] 行的数据
     *
     * @since 2022-1-8
     */
    @Select(value = "SELECT * FROM SessionsDialogData WHERE sessionId={#sessionId} " +
            "ORDER BY time DESC LIMIT #{begin}, #{unit}")
    SessionsDialogData getSessionDialogData(String sessionId, int begin, int unit);
}
