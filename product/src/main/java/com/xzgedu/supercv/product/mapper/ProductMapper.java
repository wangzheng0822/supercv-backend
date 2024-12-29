package com.xzgedu.supercv.product.mapper;

import com.xzgedu.supercv.product.domain.Product;
import org.apache.ibatis.annotations.*;

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
            @Result(property = "sortValue", column = "sort_value")
    })
    @Select("select * from product where id=#{id}")
    Product selectProductById(@Param("id") long id);

    @ResultMap("Product")
    @Select("select * from product where is_deleted=false order by sort_value asc")
    List<Product> getAllProducts();

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into product(name, original_price, discount_price, duration_days, ai_analysis_num, ai_optimization_num, sort_value)" +
            "values(#{name},#{originalPrice},#{discountPrice},#{durationDays},#{aiAnalysisNum},#{aiOptimizationNum}, #{sortValue})")
    int insertProduct(Product product);

    @Update("update product set name=#{name}" +
            ",original_price=#{originalPrice}" +
            ",discount_price=#{discountPrice}" +
            ",duration_days=#{durationDays}" +
            ",ai_analysis_num=#{aiAnalysisNum}" +
            ",ai_optimization_num=#{aiOptimizationNum} " +
            ",sort_value=#{sortValue} " +
            "where id=#{id}")
    int updateProduct(Product product);

    @Update("update product set is_deleted=true where id=#{id}")
    int deleteProduct(@Param("id") long id);
}
