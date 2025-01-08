package com.xzgedu.supercv.order.mapper;

import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.domain.OrderFilter;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into cv_order (order_no, uid, product_id, order_time, payment_amount, payment_no_3rd, " +
            "payment_channel_type, payment_channel_id, payment_status, payment_time, payment_url, " +
            "grant_status, grant_time, user_comment, admin_comment) " +
            "values (#{orderNo}, #{uid}, #{productId}, #{orderTime}, #{paymentAmount}, #{paymentNo3rd}, " +
            "#{paymentChannelType}, #{paymentChannelId}, #{paymentStatus}, #{paymentTime}, #{paymentUrl}, " +
            "#{grantStatus}, #{grantTime}, #{userComment}, #{adminComment})")
    int insertOrder(Order order);

    @Update("update cv_order set is_deleted=true where id=#{id}")
    int deleteOrder(@Param("id") long id);

    @Update("update cv_order set payment_status=#{paymentStatus}, payment_time=#{paymentTime} where id=#{id}")
    int updatePaymentStatus(@Param("id") long id,
                            @Param("paymentStatus") int paymentStatus,
                            @Param("paymentTime") Date paymentTime);

    @Update("update cv_order set payment_status=4 where id=#{id} and payment_status=1")
    int closeOrderIfNotPaid(@Param("id") long id);

    @Update("update cv_order set payment_url=#{paymentUrl} where id=#{id}")
    int updateQrUrl(@Param("id") long id, @Param("paymentUrl") String paymentUrl);

    @Update("update cv_order set payment_no_3rd=#{paymentNo3rd}, payment_time=#{paymentTime}," +
            "payment_status=#{paymentStatus} where order_no=#{orderNo} and payment_status=1")
    int updatePaidInfo(@Param("orderNo") String orderNo,
                       @Param("paymentNo3rd") String paymentNo3rd,
                       @Param("paymentTime") Date paymentTime,
                       @Param("paymentStatus") int paymentStatus);

    @Update("update cv_order set grant_status=#{grantStatus} where id=#{id}")
    int updateGrantStatus(@Param("id") long id,
                          @Param("grantStatus") int grantStatus);

    @Results(id = "Order", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "orderNo", column = "order_no"),
            @Result(property = "uid", column = "uid"),
            @Result(property = "productId", column = "product_id"),
            @Result(property = "orderTime", column = "order_time"),
            @Result(property = "paymentAmount", column = "payment_amount"),
            @Result(property = "paymentNo3rd", column = "payment_no_3rd"),
            @Result(property = "paymentChannelType", column = "payment_channel_type"),
            @Result(property = "paymentChannelId", column = "payment_channel_id"),
            @Result(property = "paymentStatus", column = "payment_status"),
            @Result(property = "paymentTime", column = "payment_time"),
            @Result(property = "paymentUrl", column = "payment_url"),
            @Result(property = "grantStatus", column = "grant_status"),
            @Result(property = "grantTime", column = "grant_time"),
            @Result(property = "userComment", column = "user_comment"),
            @Result(property = "adminComment", column = "admin_comment"),
    })
    @Select("select * from cv_order where id=#{id}")
    Order selectOrderById(@Param("id") long id);

    @ResultMap("Order")
    @Select("select * from cv_order where order_no=#{no}")
    Order selectOrderByNo(@Param("no") String no);

    @ResultMap("Order")
    @SelectProvider(value = OrderSql.class, method = "selectOrders")
    List<Order> selectOrders(@Param("orderFilter") OrderFilter orderFilter,
                             @Param("limitOffset") int limitOffset,
                             @Param("limitSize") int limitSize);

    @SelectProvider(value = OrderSql.class, method = "countOrders")
    int countOrders(@Param("orderFilter") OrderFilter orderFilter);

    @SelectProvider(value = OrderSql.class, method = "sumOrderAmount")
    BigDecimal sumOrderAmount(@Param("orderFilter") OrderFilter orderFilter);
}