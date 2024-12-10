package com.xzgedu.supercv.order.repo;

import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepo {
    @Autowired
    private OrderMapper orderMapper;

    public Order getOrderById(long oid) {
        return orderMapper.selectOrderById(oid);
    }

    public Order getOrderByOrderNo(String orderNo) {
        return orderMapper.selectOrderByOrderNo(orderNo);
    }

    public List<Order> listOrdersByUserId(Long userId, int limitOffset, int limitSize) {
        return orderMapper.selectOrdersByUserId(userId, limitOffset, limitSize);
    }

    public int countOrderByUserId(Long userId) {
        return orderMapper.countOrderByUserId(userId);
    }

    public List<Order> listOrdersByCondition(Map<String, Object> params) {
        return orderMapper.selectOrdersByCondition(params);
    }

    public int countOrderByCondition(Map<String, Object> params) {
        return orderMapper.countOrderByCondition(params);
    }

    public boolean addOrder(Order order) {
        return orderMapper.insertOrder(order) == 1;
    }

    public boolean updatePaymentStatusSuccess(String orderNo, String paymentNo3rd, Integer paymentChannelType, Long paymentChannelId) {
        return orderMapper.updatePaymentStatusSuccess(orderNo, paymentNo3rd, paymentChannelType, paymentChannelId) == 1;
    }

    public boolean updatePaymentStatusNotSuccess(String orderNo, Integer paymentStatus) {
        return orderMapper.updatePaymentStatusNotSuccess(orderNo, paymentStatus) == 1;
    }

    public boolean updateGrantStatusSuccess(String orderNo) {
        return orderMapper.updateGrantStatusSuccess(orderNo) == 1;
    }

    public boolean updateGrantStatusNotSuccess(String orderNo, Integer grantStatus) {
        return orderMapper.updateGrantStatusNotSuccess(orderNo, grantStatus) == 1;
    }

    public boolean updateUserComment(String orderNo, String userComment) {
        return orderMapper.updateUserComment(orderNo, userComment) == 1;
    }

    public boolean updateAdminComment(String orderNo, String adminComment) {
        return orderMapper.updateAdminComment(orderNo, adminComment) == 1;
    }

    public boolean updateLogicDeletion(long oid) {
        return orderMapper.updateLogicDeletion(oid) == 1;
    }

    public int batchUpdatePaymentStatusOverTime() {
        return orderMapper.batchUpdatePaymentStatusOverTime();
    }
}
