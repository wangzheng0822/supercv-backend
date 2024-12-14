package com.xzgedu.supercv.order.mapper;

import com.xzgedu.supercv.order.domain.PaymentChannel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaymentChannelMapper {

    @Results(id="PaymentChannel", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "type", column = "type"),
            @Result(property = "name", column = "name"),
            @Result(property = "appId", column = "app_id"),
            @Result(property = "mchId", column = "mch_id"),
            @Result(property = "secret", column = "secret"),
            @Result(property = "apiUrl", column = "api_url"),
            @Result(property = "callbackUrl", column = "callback_url"),
            @Result(property = "returnUrl", column = "return_url"),
            @Result(property = "enabled", column = "enabled")
    })
    @Select("select * from payment_channel where id=#{id}")
    PaymentChannel selectPaymentChannelById(@Param("id") long id);

    @ResultMap("PaymentChannel")
    @Select("select * from payment_channel")
    List<PaymentChannel> selectAllPaymentChannels();

    @ResultMap("PaymentChannel")
    @Select("select * from payment_channel where type=#{payChannelType} and enabled=true")
    List<PaymentChannel> selectEnabledPaymentChannelsByType(@Param("payChannelType") int payChannelType);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into payment_channel(type, name, app_id, mch_id, secret, api_url, callback_url, return_url, enabled) " +
            "values(#{type}, #{name}, #{appId}, #{mchId}, #{secret}, #{apiUrl}, #{callbackUrl}, #{returnUrl}, #{enabled})")
    int insertPaymentChannel(PaymentChannel payChannel);

    @Update("update payment_channel set type=#{type}, name=#{name}," +
            "app_id=#{appId}, mch_id=#{mchId}, secret=#{secret}, api_url=#{apiUrl}, callback_url=#{callbackUrl}," +
            "return_url=#{returnUrl}, enabled=#{enabled} where id=#{id}")
    int updatePaymentChannel(PaymentChannel payChannel);

    @Delete("delete from payment_channel where id=#{id}")
    int deletePaymentChannel(@Param("id") long id);

    @Select("select distinct type from payment_channel where enabled=true")
    List<Integer> selectEnabledPaymentChannelTypes();
}
