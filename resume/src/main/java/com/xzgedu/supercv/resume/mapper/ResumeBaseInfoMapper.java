package com.xzgedu.supercv.resume.mapper;

import com.xzgedu.supercv.resume.domain.ResumeBaseInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ResumeBaseInfoMapper {

    @Results(id = "ResumeBaseInfo", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "resumeId", column = "resume_id"),
            @Result(property = "headImgUrl", column = "head_img_url"),
            @Result(property = "headImgEnabled", column = "is_head_img_enabled"),
            @Result(property = "headImgLayout", column = "head_img_layout"),
            @Result(property = "itemLayout", column = "item_layout"),
            @Result(property = "enabled", column = "is_enabled"),
    })
    @Select("SELECT * FROM resume_base_info WHERE id = #{id}")
    ResumeBaseInfo selectResumeBaseInfoById(@Param("id") long id);

    @ResultMap("ResumeBaseInfo")
    @Select("SELECT * FROM resume_base_info WHERE resume_id = #{resumeId}")
    ResumeBaseInfo selectResumeBaseInfoByResumeId(@Param("resumeId") long resumeId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO resume_base_info (resume_id, head_img_url, is_head_img_enabled, head_img_layout, item_layout, is_enabled) " +
            "VALUES (#{resumeId}, #{headImgUrl}, #{headImgEnabled}, #{headImgLayout}, #{itemLayout}, #{enabled})")
    int insertResumeBaseInfo(ResumeBaseInfo baseInfo);

    @Update("UPDATE resume_base_info SET resume_id = #{resumeId}, head_img_url = #{headImgUrl}, " +
            "is_head_img_enabled = #{headImgEnabled}, head_img_layout = #{headImgLayout}, item_layout = #{itemLayout}, " +
            "is_enabled = #{enabled} WHERE id = #{id}")
    int updateResumeBaseInfo(ResumeBaseInfo baseInfo);
}
