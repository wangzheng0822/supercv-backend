package com.xzgedu.supercv.admin.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface AdminMapper {
    @Select("select uid from admin where uid=#{uid}")
    Long selectAdminByUid(@Param("uid") long uid);

    @Insert("insert into admin(uid) values(#{uid})")
    int insertAdmin(@Param("uid") long uid);

    @Delete("delete from admin where uid=#{uid}")
    int deleteAdmin(@Param("uid") long uid);
}
