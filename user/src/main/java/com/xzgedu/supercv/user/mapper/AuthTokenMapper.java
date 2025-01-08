package com.xzgedu.supercv.user.mapper;

import com.xzgedu.supercv.user.domain.AuthToken;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface AuthTokenMapper {

    @Results(id = "AuthToken", value = {
            @Result(property = "token", column = "token"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "expireTime", column = "expire_time")
    })
    @Select("select * from auth_token where token=#{token}")
    AuthToken selectAuthToken(@Param("token") String token);

    @Insert("insert into auth_token(uid, token, expire_time) values(#{uid}, #{token}, #{expireTime})")
    int insertAuthToken(AuthToken authToken);

    @Delete("delete from auth_token where token=#{token}")
    int deleteToken(@Param("token") String token);

    @Update("update auth_token set expire_time=#{expireTime} where token=#{token}")
    int updateTokenExpireTime(@Param("token") String token, @Param("expireTime") Date expireTime);
}