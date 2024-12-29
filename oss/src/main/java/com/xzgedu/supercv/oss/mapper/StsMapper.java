package com.xzgedu.supercv.oss.mapper;

import com.xzgedu.supercv.oss.repo.StsToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StsMapper {

    @Results(id="stsToken", value = {
            @Result(property = "uid", column = "uid"),
            @Result(property = "accessKeyId", column = "access_key_id"),
            @Result(property = "accessKeySecret", column = "access_key_secret"),
            @Result(property = "securityToken", column = "security_token"),
            @Result(property = "expiration",column = "expiration")
    })
    @Select("select * from oss_sts where uid=#{uid}")
    StsToken getStsToken(@Param("uid") long uid);

    @Insert("insert into oss_sts(uid, access_key_id, access_key_secret, security_token, expiration) " +
            "values(#{uid}, #{accessKeyId}, #{accessKeySecret}, #{securityToken}, #{expiration})")
    int insertStsToken(StsToken stsToken);

    @Update("update oss_sts set access_key_id=#{accessKeyId}, access_key_secret=#{accessKeySecret}, " +
            "security_token=#{securityToken}, expiration=#{expiration} where uid=#{uid}")
    int updateStsToken(StsToken stsToken);

    @Delete("delete from oss_sts where uid=#{uid}")
    int deleteStsToken(@Param("uid") long uid);
}
