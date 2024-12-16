package com.xzgedu.supercv.resume.mapper;

import com.xzgedu.supercv.resume.domain.Resume;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResumeMapper {
    @Results(id = "Resume", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "name", column = "name"),
            @Result(property = "templateId", column = "template_id"),
            @Result(property = "templateName", column = "template_name"),
            @Result(property = "templateCssName", column = "template_css_name"),
            @Result(property = "originalResumeUrl", column = "original_resume_url"),
            @Result(property = "thumbnailUrl", column = "thumbnail_url"),
            @Result(property = "pageMarginHorizontal", column = "page_margin_horizontal"),
            @Result(property = "pageMarginVertical", column = "page_margin_vertical"),
            @Result(property = "moduleMargin", column = "module_margin"),
            @Result(property = "themeColor", column = "theme_color"),
            @Result(property = "fontSize", column = "font_size"),
            @Result(property = "fontFamily", column = "font_family"),
            @Result(property = "lineHeight", column = "line_height"),
            @Result(property = "templateDemo", column = "is_template_demo"),
    })
    @Select("select resume.*, resume_template.name, resume_template.css_name from resume left join resume_template " +
            "on resume.template_id=resume_template.id where resume.id=#{id} and resume.is_deleted=false")
    Resume selectResumeById(@Param("id") long id);

    //分页获取Resume
    @Select("select * from resume where is_deleted=false limit #{limitOffset}, #{limitSize}")
    List<Resume> selectResumesPagination(@Param("limitOffset") int limitOffset, @Param("limitSize") int limitSize);

    @Select("select count(*) from resume where is_deleted=false")
    int countResumes();

    @ResultMap("Resume")
    @Select("select resume.*, resume_template.name, resume_template.css_name from resume left join resume_template " + "" +
            "on resume.template_id=resume_template.id where uid=#{uid} and resume.is_deleted=false " +
            "order by resume.update_time desc limit #{limitOffset}, #{limitSize}")
    List<Resume> selectResumesByUid(@Param("uid") long uid,
                                    @Param("limitOffset") int limitOffset,
                                    @Param("limitSize") int limitSize);

    @Select("select count(*) from resume where is_deleted=false and uid=#{uid}")
    int countResumesByUid(@Param("uid") long uid);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO resume (uid, name, template_id, original_resume_url, thumbnail_url, " +
            "page_margin_horizontal, page_margin_vertical, module_margin, theme_color, font_size, " +
            "font_family, line_height, is_template_demo) " +
            "VALUES (#{uid}, #{name}, #{templateId}, #{originalResumeUrl}, #{thumbnailUrl}, " +
            "#{pageMarginHorizontal}, #{pageMarginVertical}, #{moduleMargin}, #{themeColor}, #{fontSize}, " +
            "#{fontFamily}, #{lineHeight}, #{templateDemo})"
    )
    int insertResume(Resume resume);

    @Update("update resume set is_deleted=true where id=#{id}")
    int deleteResume(@Param("id") long id);

    @Update("UPDATE resume " +
            "SET uid = #{uid}, " +
            "    name = #{name}, " +
            "    template_id = #{templateId}, " +
            "    original_resume_url = #{originalResumeUrl}, " +
            "    thumbnail_url = #{thumbnailUrl}, " +
            "    page_margin_horizontal = #{pageMarginHorizontal}, " +
            "    page_margin_vertical = #{pageMarginVertical}, " +
            "    module_margin = #{moduleMargin}, " +
            "    theme_color = #{themeColor}, " +
            "    font_size = #{fontSize}, " +
            "    font_family = #{fontFamily}, " +
            "    line_height = #{lineHeight}, " +
            "    is_template_demo = #{templateDemo} " +
            "WHERE id = #{id}")
    int updateResume(Resume resume);
}
