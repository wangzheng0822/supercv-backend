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

    public Order getOrderById(long id) {
        return orderMapper.selectOrderById(id);
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

    public boolean updatePaymentStatusSuccess(Long id, String paymentNo3rd, Integer paymentChannelType, Long paymentChannelId) {
        return orderMapper.updatePaymentStatusSuccess(id, paymentNo3rd, paymentChannelType, paymentChannelId) == 1;
    }

    public boolean updatePaymentStatusNotSuccess(Long id, Integer paymentStatus) {
        return orderMapper.updatePaymentStatusNotSuccess(id, paymentStatus) == 1;
    }

    public boolean updateGrantStatusSuccess(Long id) {
        return orderMapper.updateGrantStatusSuccess(id) == 1;
    }

    public boolean updateGrantStatusNotSuccess(Long id, Integer grantStatus) {
        return orderMapper.updateGrantStatusNotSuccess(id, grantStatus) == 1;
    }

    public boolean updateUserComment(Long id, String userComment) {
        return orderMapper.updateUserComment(id, userComment) == 1;
    }

    public boolean updateAdminComment(Long id, String adminComment) {
        return orderMapper.updateAdminComment(id, adminComment) == 1;
    }

    public boolean updateLogicDeletion(long id) {
        return orderMapper.updateLogicDeletion(id) == 1;
    }

    public int batchUpdatePaymentStatusOverTime() {
        return orderMapper.batchUpdatePaymentStatusOverTime();
    }
}
