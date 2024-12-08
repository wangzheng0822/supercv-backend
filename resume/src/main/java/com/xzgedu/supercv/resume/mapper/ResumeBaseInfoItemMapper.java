package com.xzgedu.supercv.resume.mapper;

import com.xzgedu.supercv.resume.domain.ResumeBaseInfoItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResumeBaseInfoItemMapper {

    @Results(id = "ResumeBaseInfoItem", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "baseInfoId", column = "baseinfo_id"),
            @Result(property = "key", column = "key"),
            @Result(property = "value", column = "value"),
            @Result(property = "sortValue", column = "sort_value")
    })

    @Select("SELECT * FROM resume_baseinfo_item WHERE baseinfo_id = #{baseInfoId}")
    List<ResumeBaseInfoItem> selectResumeBaseInfoItems(@Param("baseInfoId") long baseInfoId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO resume_baseinfo_item (baseinfo_id, key, value, sort_value) " +
            "VALUES (#{baseInfoId}, #{key}, #{value}, #{sortValue})")
    int insertResumeBaseInfoItem(ResumeBaseInfoItem item);

    @Update("UPDATE resume_baseinfo_item SET key = #{key}, value = #{value}, sort_value = #{sortValue} " +
            "WHERE id = #{id}")
    int updateResumeBaseInfoItem(ResumeBaseInfoItem item);

    @Delete("DELETE FROM resume_baseinfo_item WHERE id = #{id}")
    int deleteResumeBaseInfoItem(@Param("id") long id);
}
