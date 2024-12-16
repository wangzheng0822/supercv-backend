package com.xzgedu.supercv.article.mapper;

import com.xzgedu.supercv.article.domain.ArticleContent;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ArticleContentMapper {

    @Results(id = "ArticleContent", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "content", column = "content")
    })
    @Select("SELECT * FROM article_content WHERE id = #{id}")
    ArticleContent selectArticleContentById(@Param("id") long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO article_content (content) VALUES (#{content})")
    int insertArticleContent(ArticleContent articleContent);

    @Update("UPDATE article_content SET content = #{content} WHERE id = #{id}")
    int updateArticleContent(ArticleContent articleContent);

    @Delete("DELETE FROM article_content WHERE id = #{id}")
    int deleteArticleContent(@Param("id") long id);
}