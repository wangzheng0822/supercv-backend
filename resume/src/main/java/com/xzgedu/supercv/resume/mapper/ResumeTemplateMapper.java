package com.xzgedu.supercv.resume.mapper;

import com.xzgedu.supercv.resume.domain.ResumeTemplate;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResumeTemplateMapper {

    // 增加模板
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO resume_template (name, page_frame, page_style) VALUES (#{name}, #{pageFrame}, #{pageStyle})")
    int insertTemplate(ResumeTemplate template);

    // 删除模板
    @Update("UPDATE resume_template SET is_deleted = TRUE WHERE id = #{id}")
    int deleteTemplate(@Param("id") long id);

    // 更新模板
    @Update("UPDATE resume_template SET name = #{name}, page_frame=#{pageFrame}, page_style = #{pageStyle} " +
            "WHERE id = #{id}")
    int updateTemplate(ResumeTemplate template);

    // 更新示例简历
    @Update("UPDATE resume_template SET demo_resume_id = #{demoResumeId} WHERE id = #{id}")
    int updateDemoResumeId(@Param("id") long id, @Param("demoResumeId") long demoResumeId);

    // 根据ID查询模板
    @Results(id = "ResumeTemplateResult", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "pageFrame", column = "page_frame"),
            @Result(property = "pageStyle", column = "page_style"),
            @Result(property = "demoResumeId", column = "demo_resume_id"),
            @Result(property = "demoResumeThumbnailUrl", column = "demo_resume_thumbnail_url"),
    })
    @Select("SELECT rt.*, r.thumbnail_url as demo_resume_thumbnail_url " +
            "FROM resume_template rt " +
            "LEFT JOIN resume r ON rt.demo_resume_id = r.id " +
            "WHERE id = #{id} and is_deleted=false")
    ResumeTemplate selectTemplateById(Long id);

    // 分页查询模板
    @ResultMap("ResumeTemplateResult")
    @Select("SELECT rt.*, r.thumbnail_url as demo_resume_thumbnail_url " +
            "FROM resume_template rt " +
            "LEFT JOIN resume r ON rt.demo_resume_id = r.id " +
            "WHERE rt.is_deleted = FALSE " +
            "LIMIT #{limitOffset}, #{limitSize}")
    List<ResumeTemplate> selectTemplatesPagination(@Param("limitOffset") int limitOffset,
                                                   @Param("limitSize") int limitSize);

    @Select("select count(*) from resume_template where is_deleted=false")
    int countTemplates();
}