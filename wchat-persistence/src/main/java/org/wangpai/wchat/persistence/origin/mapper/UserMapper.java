package org.wangpai.wchat.persistence.origin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangpai.wchat.persistence.origin.domain.User;

/**
 * @since 2021-12-28
 */
@Mapper
public interface UserMapper {
    /**
     * 如果没有搜索到，此方法会返回 null，而不是抛出异常
     *
     * @since 2021-12-28
     */
    @Select(value = "SELECT * FROM contacts WHERE id = #{id}")
    User search(String id);

    @Insert(value = "INSERT INTO user (id, ip, port, nickname, avatar, remarks, myContactCode, otherPartyContactCode) " +
            "VALUES (#{id},#{ip},#{port},#{nickname},#{avatar},#{remarks}, #{myContactCode}, #{otherPartyContactCode})")
    int save(User user);

    @Update(value = "UPDATE user SET " +
            "id=#{user.id}, ip=#{user.ip}, port=#{user.port}," +
            "nickname=#{user.nickname}, avatar=#{user.avatar}, remarks=#{user.remarks}," +
            "myContactCode=#{user.myContactCode}, otherPartyContactCode=#{user.otherPartyContactCode} " +
            "WHERE id=#{id}")
    int updateById(@Param("user") User user, @Param("id") String id);

    /**
     * 如果实际没有删除到任何数据，此方法会返回 0，而不是抛出异常
     *
     * @since 2021-12-28
     */
    @Delete(value = "DELETE FROM User WHERE id = #{id}")
    int delete(String id);

    @Select(value = "SELECT * FROM User")
    List<User> getContacts();
}
