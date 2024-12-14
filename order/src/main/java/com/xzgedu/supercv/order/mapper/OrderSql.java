package com.xzgedu.supercv.order.mapper;

import com.xzgedu.supercv.order.domain.OrderFilter;

import java.text.SimpleDateFormat;
import java.util.Map;

public class OrderSql {
    public String selectOrders(Map<String, Object> params) {
        OrderFilter orderFilter = (OrderFilter) params.get("orderFilter");
        StringBuilder sql = new StringBuilder("select * from cv_order where 1=1");
        joinWhereClause(sql, orderFilter);
        sql.append(" order by create_time desc limit #{limitOffset}, #{limitSize}");
        return sql.toString();
    }

    public String countOrders(Map<String, Object> params) {
        OrderFilter orderFilter = (OrderFilter) params.get("orderFilter");
        StringBuilder sql = new StringBuilder("select count(*) from cv_order where 1=1");
        joinWhereClause(sql, orderFilter);
        return sql.toString();
    }

    public String sumOrderAmount(Map<String, Object> params) {
        OrderFilter orderFilter = (OrderFilter) params.get("orderFilter");
        StringBuilder sql = new StringBuilder("select IFNULL(SUM(payment_amount), 0) from cv_order where 1=1");
        joinWhereClause(sql, orderFilter);
        return sql.toString();
    }

    private void joinWhereClause(StringBuilder sql, OrderFilter orderFilter) {
        if (orderFilter.getOrderNo() != null) {
            sql.append(" and order_no='" + orderFilter.getOrderNo() + "'");
        }
        if (orderFilter.getProductId() != null) {
            sql.append(" and product_id=" + orderFilter.getProductId());
        }
        if (orderFilter.getPaymentChannelId() != null) {
            sql.append(" and payment_channel_id=" + orderFilter.getPaymentChannelId());
        }
        if (orderFilter.getPaymentStatus() != null) {
            sql.append(" and payment_status=" + orderFilter.getPaymentStatus());
        }
        if (orderFilter.getGrantStatus() != null) {
            sql.append(" and grant_status=" + orderFilter.getGrantStatus());
        }
        if (orderFilter.getUid() != null) {
            sql.append(" and uid=" + orderFilter.getUid());
        }
        if (orderFilter.getStartTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sql.append(" and create_time>='" + sdf.format(orderFilter.getStartTime()) + "'");
        }
        if (orderFilter.getEndTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sql.append(" and create_time<='" + sdf.format(orderFilter.getEndTime()) + "'");
        }
    }
}
