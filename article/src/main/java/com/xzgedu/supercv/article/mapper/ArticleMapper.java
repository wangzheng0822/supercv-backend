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
            @Result(property = "contentId", column = "content_id"),
            @Result(property = "free", column = "is_free")
    })
    @Select("select * from article where cate_type=#{cateType} " +
            "order by create_time desc limit #{limitOffset}, #{limitSize}")
    List<Article> selectArticlesByCateType(@Param("cateType") int cateType,
                                           @Param("limitOffset") int limitOffset,
                                           @Param("limitSize") int limitSize);

    @Select("SELECT count(*) from article where cate_type=#{cateType}")
    int countArticlesByCateType(@Param("cateType") int cateType);

    @ResultMap("Article")
    @Select("SELECT * FROM article WHERE id = #{id}")
    Article selectArticleById(@Param("id") long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO article (uid, cate_type, title, sub_title, snippet, cover_img, content_id, is_free) " +
            "VALUES (#{uid}, #{cateType}, #{title}, #{subTitle}, #{snippet}, #{coverImg}, #{contentId}, #{free})")
    int insertArticle(Article article);

    @Update("UPDATE article SET title = #{title}, sub_title = #{subTitle}, snippet = #{snippet}, " +
            "cover_img = #{coverImg}, content_id = #{contentId}, is_free=#{free} WHERE id = #{id}")
    int updateArticle(Article article);

    @Update("UPDATE article SET cover_img = #{coverImg} WHERE id = #{id}")
    int updateCoverImg(@Param("id") long id, @Param("coverImg") String coverImg);

    @Delete("DELETE FROM article WHERE id = #{id}")
    int deleteArticle(@Param("id") long id);
}