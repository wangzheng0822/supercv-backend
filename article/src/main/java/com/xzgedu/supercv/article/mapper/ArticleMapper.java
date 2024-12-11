package com.xzgedu.supercv.article.mapper;

import com.xzgedu.supercv.article.domain.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Results(id = "Article", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "cateType", column = "cate_type"),
            @Result(property = "title", column = "title"),
            @Result(property = "subTitle", column = "sub_title"),
            @Result(property = "snippet", column = "snippet"),
            @Result(property = "coverImg", column = "cover_img"),
            @Result(property = "contentId", column = "content_id")
    })

    @Select("select * from article where cate_type=#{cateType} order by create_time desc limit #{limitOffset}, #{limitSize}")
    List<Article> listArticles(@Param("cateType") String cateType,
                               @Param("limitOffset") int limitOffset,
                               @Param("limitSize") int limitSize);
    @ResultMap("Article")
    @Select("SELECT * FROM article WHERE id = #{id}")
    Article selectArticleById(@Param("id") long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO article (uid, cate_type, title, sub_title, snippet, cover_img, content_id) " +
            "VALUES (#{uid}, #{cateType}, #{title}, #{subTitle}, #{snippet}, #{coverImg}, #{contentId})")
    int insertArticle(Article article);

    @Update("UPDATE article SET title = #{title}, sub_title = #{subTitle}, snippet = #{snippet}, " +
            "cover_img = #{coverImg}, content_id = #{contentId} WHERE id = #{id}")
    int updateArticle(Article article);

    @Delete("DELETE FROM article WHERE id = #{id}")
    int deleteArticle(@Param("id") long id);
}