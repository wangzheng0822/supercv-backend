package com.xzgedu.supercv.product.mapper;

import com.xzgedu.supercv.product.domain.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Results(id = "Product", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "originalPrice", column = "original_price"),
            @Result(property = "discountPrice", column = "discount_price"),
            @Result(property = "durationDays", column = "duration_days"),
            @Result(property = "aiAnalysisNum", column = "ai_analysis_num"),
            @Result(property = "aiOptimizationNum", column = "ai_optimization_num"),
            @Result(property = "isDeleted", column = "is_deleted"),
    })
    @Select("select * from product where id=#{id}")
    Product selectProductId(@Param("id") long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into product(name, original_price, discount_price, duration_days, ai_analysis_num, ai_optimization_num)" +
            "values(#{name},#{originalPrice},#{discountPrice},#{durationDays},#{aiAnalysisNum},#{aiOptimizationNum})")
    int insertProduct(Product product);

    @ResultMap("Product")
    @Select("select * from product order by create_time desc limit #{limitOffset}, #{limitSize}")
    List<Product> listProduct(
            @Param("limitOffset") int limitOffset,
            @Param("limitSize") int limitSize);

    @Update("update product set name=#{name}" +
            ",original_price=#{originalPrice}" +
            ",discount_price=#{discountPrice}" +
            ",duration_days=#{durationDays}" +
            ",ai_analysis_num=#{aiAnalysisNum}" +
            ",ai_optimization_num=#{aiOptimizationNum} " +
            "where id=#{id}")
    int updateProduct(Product product);


    @Update("update product set is_deleted=#{isDeleted} where id=#{id}")
    int deleteProduct(@Param("id") long id,
                      @Param("isDeleted") boolean isDeleted);


    @Select("select count(*) from product where name=#{name}")
    int countProductsByName(@Param("name") String name);

    @ResultMap("Product")
    @Select("select * from product where name=#{name}")
    Product selectProductByName(@Param("name") String name);

}
