package com.xzgedu.supercv.resume.mapper;

import com.xzgedu.supercv.resume.domain.ResumeModuleItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResumeModuleItemMapper {

    @Results(id = "ResumeModuleItem", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "resumeId", column = "resume_id"),
            @Result(property = "moduleId", column = "module_id"),
            @Result(property = "titleMajor", column = "title_major"),
            @Result(property = "titleMinor", column = "title_minor"),
            @Result(property = "titleDate", column = "title_date"),
            @Result(property = "titleSortType", column = "title_sort_type"),
            @Result(property = "titleMajorEnabled", column = "is_title_major_enabled"),
            @Result(property = "titleMinorEnabled", column = "is_title_minor_enabled"),
            @Result(property = "titleDateEnabled", column = "is_title_date_enabled"),
            @Result(property = "content", column = "content"),
            @Result(property = "sortValue", column = "sort_value")
    })
    @Select("SELECT * FROM resume_module_item WHERE module_id = #{moduleId}")
    List<ResumeModuleItem> selectResumeModuleItemsByModuleId(@Param("moduleId") long moduleId);

    @Insert("INSERT INTO resume_module_item (resume_id, module_id, title_major, title_minor, title_date, " +
            "title_sort_type, is_title_major_enabled, is_title_minor_enabled, is_title_date_enabled, content, sort_value) " +
            "VALUES (#{resumeId}, #{moduleId}, #{titleMajor}, #{titleMinor}, #{titleDate}, " +
            "#{titleSortType}, #{titleMajorEnabled}, #{titleMinorEnabled}, #{titleDateEnabled}, #{content}, #{sortValue})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertResumeModuleItem(ResumeModuleItem moduleItem);

    @Update("UPDATE resume_module_item " +
            "SET title_major = #{titleMajor}, title_minor = #{titleMinor}, title_date = #{titleDate}, " +
            "title_sort_type = #{titleSortType}, is_title_major_enabled = #{titleMajorEnabled}, " +
            "is_title_minor_enabled = #{titleMinorEnabled}, is_title_date_enabled = #{titleDateEnabled}, " +
            "content = #{content}, sort_value = #{sortValue} WHERE id = #{id}")
    int updateResumeModuleItem(ResumeModuleItem moduleItem);

    @Delete("DELETE FROM resume_module_item WHERE id = #{id}")
    int deleteResumeModuleItem(@Param("id") long id);
}
