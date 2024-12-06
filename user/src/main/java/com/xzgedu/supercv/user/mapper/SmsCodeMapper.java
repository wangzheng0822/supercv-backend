package com.xzgedu.supercv.user.mapper;

import com.xzgedu.supercv.user.domain.SmsCode;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SmsCodeMapper {

    @Results(id = "SmsCode", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "telephone", column = "telephone"),
            @Result(property = "code", column = "code"),
            @Result(property = "sendTime", column = "send_time"),
            @Result(property = "used", column = "is_used")
    })
    @Select("select * from sms_code where telephone=#{telephone}")
    SmsCode selectSmsCodeByTelephone(@Param("telephone") String telephone);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into sms_code(telephone, code, send_time, is_used)" +
            " values(#{telephone}, #{code}, #{sendTime}, #{used})")
    int insertSmsCode(SmsCode smsCode);

    @Update("update sms_code set code=#{code}, send_time=#{sendTime}, is_used=#{used}" +
            " where telephone=#{telephone}")
    int updateSmsCode(SmsCode smsCode);

    @Update("update sms_code set is_used=true where telephone=#{telephone}")
    int updateToBeUsed(@Param("telephone") String telephone);
}
