package com.xzgedu.supercv.order.mapper;

import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.mapper.sql.OrderProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    @Results(id = "OrderMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "orderNo", column = "order_no"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "productId", column = "product_id"),
            @Result(property = "orderTime", column = "order_time"),
            @Result(property = "paymentNo3rd", column = "payment_no_3rd"),
            @Result(property = "paymentChannelType", column = "payment_channel_type"),
            @Result(property = "paymentChannelId", column = "payment_channel_id"),
            @Result(property = "paymentStatus", column = "payment_status"),
            @Result(property = "paymentTime", column = "payment_time"),
            @Result(property = "grantStatus", column = "grant_status"),
            @Result(property = "grantTime", column = "grant_time"),
            @Result(property = "userComment", column = "user_comment"),
            @Result(property = "adminComment", column = "admin_comment"),
            @Result(property = "isDeleted", column = "is_deleted")
    })
    @Select("select id, order_no, user_id, product_id, order_time, payment_status, grant_status, " +
            "payment_no_3rd, payment_channel_type, payment_channel_id, payment_time, grant_time, " +
            "user_comment, admin_comment, is_deleted " +
            "from `order` where id = #{id}")
    Order selectOrderById(@Param("id") long id);

    @ResultMap("OrderMap")
    @Select("select id, order_no, user_id, product_id, order_time, payment_status, grant_status, " +
            "payment_no_3rd, payment_channel_type, payment_channel_id, payment_time, grant_time, " +
            "user_comment, admin_comment, is_deleted " +
            "from `order` where order_no = #{orderNo}")
    Order selectOrderByOrderNo(@Param("orderNo") String orderNo);

    @ResultMap("OrderMap")
    @Select("select id, order_no, user_id, product_id, order_time, payment_status, grant_status, " +
            "payment_no_3rd, payment_channel_type, payment_channel_id, payment_time, grant_time, " +
            "user_comment, admin_comment " +
            "from `order` where user_id = #{userId} and is_deleted = 0 " +
            "order by order_time desc limit #{limitOffset}, #{limitSize}")
    List<Order> selectOrdersByUserId(@Param("userId") Long userId, @Param("limitOffset") int limitOffset, @Param("limitSize") int limitSize);

    @Select("select count(*) from `order` where user_id = #{userId} and is_deleted = 0 ")
    int countOrderByUserId(@Param("userId") Long userId);

    @ResultMap("OrderMap")
    @SelectProvider(type = OrderProvider.class, method = "getOrdersByCondition")
    List<Order> selectOrdersByCondition(@Param("params") Map<String, Object> params);

    @SelectProvider(type = OrderProvider.class, method = "countOrderByCondition")
    int countOrderByCondition(@Param("params") Map<String, Object> params);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into `order` (order_no, user_id, product_id, order_time, payment_status, grant_status) " +
            "values (#{orderNo}, #{userId}, #{productId}, #{orderTime}, #{paymentStatus}, #{grantStatus})")
    int insertOrder(Order order);

    @Update("update `order` set payment_status = 1, payment_no_3rd = #{paymentNo3rd}, payment_channel_type = #{paymentChannelType}, payment_channel_id = #{paymentChannelId}, payment_time = now() where id = #{id}")
    int updatePaymentStatusSuccess(@Param("id") Long id, @Param("paymentNo3rd") String paymentNo3rd, @Param("paymentChannelType") Integer paymentChannelType, @Param("paymentChannelId") Long paymentChannelId);

    @Update("update `order` set payment_status = #{paymentStatus} where id = #{id}")
    int updatePaymentStatusNotSuccess(@Param("id") Long id, @Param("paymentStatus") Integer paymentStatus);

    @Update("update `order` set grant_status = 1, grant_time = now() where id = #{id}")
    int updateGrantStatusSuccess(@Param("id") Long id);

    @Update("update `order` set grant_status = #{grantStatus} where id = #{id}")
    int updateGrantStatusNotSuccess(@Param("id") Long id, @Param("grantStatus") Integer grantStatus);

    @Update("update `order` set user_comment = #{userComment} where id = #{id}")
    int updateUserComment(@Param("id") Long id, @Param("userComment") String userComment);

    @Update("update `order` set admin_comment = #{adminComment} where id = #{id}")
    int updateAdminComment(@Param("id") Long id, @Param("adminComment") String adminComment);

    @Update("update `order` set is_deleted = 1 where id = #{id}")
    int updateLogicDeletion(@Param("id") long id);

    @Update("update `order` set payment_status = 2 where order_time < NOW() - INTERVAL 15 MINUTE AND payment_status = 0")
    int batchUpdatePaymentStatusOverTime();
}
