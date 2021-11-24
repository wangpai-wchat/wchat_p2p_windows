package org.wangpai.wchat.persistence.origin.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.wangpai.wchat.persistence.origin.domain.Registry;

/**
 * @since 2022-1-8
 */
@Mapper
public interface RegistryMapper {
    @Select(value = "SELECT * FROM Registry WHERE name = #{name}")
    Registry getValue(String name);

    @Insert(value = "INSERT INTO Registry (name,value) VALUES (#{name},#{value})")
    int save(Registry map);

    @Update(value = "UPDATE Registry SET name=#{map.name}, value=#{map.value} WHERE name=#{name}")
    int updateByName(@Param("map") Registry map, @Param("name") String name);

    @Delete(value = "DELETE FROM Registry WHERE name = #{name}")
    int deleteByName(String name);
}
