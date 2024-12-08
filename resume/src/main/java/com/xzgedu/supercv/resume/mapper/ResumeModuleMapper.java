package com.xzgedu.supercv.resume.mapper;

import com.xzgedu.supercv.resume.domain.ResumeModule;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResumeModuleMapper {

    @Results(id = "ResumeModule", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "resumeId", column = "resume_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "sortValue", column = "sort_value"),
            @Result(property = "defaultModule", column = "is_default"),
            @Result(property = "enabled", column = "is_enabled")
    })
    @Select("SELECT * FROM resume_module WHERE id = #{id}")
    ResumeModule selectResumeModuleById(@Param("id") long id);

    @Select("SELECT * FROM resume_module WHERE resume_id = #{resumeId}")
    List<ResumeModule> selectResumeModulesByResumeId(@Param("resumeId") long resumeId);

    @Insert("INSERT INTO resume_module (resume_id, title, sort_value, is_default, is_enabled) " +
            "VALUES (#{resumeId}, #{title}, #{sortValue}, #{defaultModule}, #{enabled})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertResumeModule(ResumeModule module);

    @Update("UPDATE resume_module SET title = #{title}, sort_value = #{sortValue}, " +
            "is_default = #{defaultModule}, is_enabled = #{enabled} WHERE id = #{id}")
    int updateResumeModule(ResumeModule module);

    @Delete("DELETE FROM resume_module WHERE id = #{id} and is_default=false")
    int deleteResumeModule(@Param("id") long id);
}
