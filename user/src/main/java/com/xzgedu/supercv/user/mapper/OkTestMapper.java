package com.xzgedu.supercv.user.mapper;

import com.xzgedu.supercv.user.domain.OkTest;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OkTestMapper {
    @Results(id="OkTest", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
    })
    @Select("select * from ok_test where id=#{id}")
    OkTest selectOkTest(@Param("id") long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into ok_test(name) values(#{name})")
    int insertOkTest(OkTest okTest);

    @Delete("delete from ok_test where id=#{id}")
    int deleteOkTest(@Param("id") long id);

    @Update("update ok_test set name=#{name} where id=#{id}")
    int updateOkTest(OkTest okTest);
}
