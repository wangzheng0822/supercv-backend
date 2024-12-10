package com.xzgedu.supercv.order.mapper.sql;

import com.xzgedu.supercv.order.domain.Order;
import java.util.Map;
import java.util.Date;

public class OrderProvider {
    public String getOrdersByCondition(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("select * from `order` where 1=1 ");

        Map<String, Object> paramMap = (Map<String, Object>) params.get("params");
        // 添加过滤条件
        sb.append(addFilterConditions(paramMap));
        //添加排序条件
        sb.append(" order by order_time desc");
        //添加分页条件
        int limitOffset = (int) paramMap.get("limitOffset");
        int limitSize = (int) paramMap.get("limitSize");
        if (limitOffset != 0 || limitSize != 0) {
            sb.append(" limit #{params.limitOffset}, #{params.limitSize}");
        }
        return sb.toString();
    }

    public String countOrderByCondition(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("select count(*) from `order` where 1=1 ");
        Map<String, Object> paramMap = (Map<String, Object>) params.get("params");
        sb.append(addFilterConditions(paramMap));
        return sb.toString();
    }

    private String addFilterConditions(Map<String, Object> paramMap) {
        StringBuilder sb = new StringBuilder();
        //添加用户id搜索条件
        Object userId = paramMap.get("userId");
        if (userId != null) {
            sb.append(" and user_id = #{params.userId}");
        }
        //添加订单号搜索条件
        Object orderNo = paramMap.get("orderNo");
        if (orderNo != null) {
            sb.append(" and order_no = #{params.orderNo}");
        }
        //添加产品id搜索条件
        Object productId = paramMap.get("productId");
        if (productId != null) {
            sb.append(" and product_id = #{params.productId}");
        }
        //添加支付渠道类型搜索条件
        Object paymentChannelType = paramMap.get("paymentChannelType");
        if (paymentChannelType != null) {
            sb.append(" and payment_channel_type = #{params.paymentChannelType}");
        }
        //添加支付状态状态搜索条件
        Object paymentStatus = paramMap.get("paymentStatus");
        if (paymentStatus != null) {
            sb.append(" and payment_status = #{params.paymentStatus}");
        }
        //添加权益授予状态搜索条件
        Object grantStatus = paramMap.get("grantStatus");
        if (grantStatus != null) {
            sb.append(" and grant_status = #{params.grantStatus}");
        }
        //添加订单时间搜索条件
        Object orderStartTime = paramMap.get("orderStartTime");
        Object orderEndTime = paramMap.get("orderEndTime");
        if (orderStartTime != null) {
            sb.append(" and order_time >= #{params.orderStartTime}");
        }
        if (orderEndTime != null) {
            sb.append(" and order_time <= #{params.orderEndTime}");
        }
        //添加逻辑删除字段
        sb.append(" and is_deleted = 0");

        return sb.toString();
    }
}