package org.wangpai.wchat.persistence.complex.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.wangpai.wchat.persistence.origin.domain.User;

/**
 * @since 2022-1-8
 */
@Mapper
public interface RegistryDao {
    @Select(value = "SELECT value FROM Registry WHERE name = 'MY_ID'")
    String getMyId();

    @Select(value = "SELECT port FROM User WHERE id IN (SELECT value FROM Registry WHERE name = 'MY_ID')")
    String getMyServerPort();

    @Select(value = "SELECT * FROM User WHERE id IN (SELECT value FROM Registry WHERE name = 'MY_ID')")
    User getMe();
}
