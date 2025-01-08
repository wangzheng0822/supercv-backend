package com.xzgedu.supercv.order.repo;

import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.domain.OrderFilter;
import com.xzgedu.supercv.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public class OrderRepo {

    @Autowired
    private OrderMapper orderMapper;

    public boolean addOrder(Order order) {
        return orderMapper.insertOrder(order) == 1;
    }

    public boolean deleteOrder(long id) {
        return orderMapper.deleteOrder(id) == 1;
    }

    public boolean updatePaymentStatus(long id, int payStatus, Date payTime) {
        return orderMapper.updatePaymentStatus(id, payStatus, payTime) == 1;
    }

    public boolean closeOrderIfNotPaid(long id) {
        return orderMapper.closeOrderIfNotPaid(id) == 1;
    }

    public boolean updateQrUrl(long id, String url) {
        return orderMapper.updateQrUrl(id, url) == 1;
    }

    public boolean updatePaidInfo(String orderNo, String orderNo3nd, Date payTime, int payStatus) {
        return orderMapper.updatePaidInfo(orderNo, orderNo3nd, payTime, payStatus) == 1;
    }

    public boolean updateGrantStatus(long id, int grantStatus) {
        return orderMapper.updateGrantStatus(id, grantStatus) == 1;
    }

    public Order getOrderById(long id) {
        return orderMapper.selectOrderById(id);
    }

    public Order getOrderByNo(String no) {
        return orderMapper.selectOrderByNo(no);
    }

    public List<Order> getOrders(OrderFilter orderFilter, int limitOffset, int limitSize) {
        return orderMapper.selectOrders(orderFilter, limitOffset, limitSize);
    }

    public int countOrders(OrderFilter orderFilter) {
        return orderMapper.countOrders(orderFilter);
    }

    public BigDecimal sumOrderAmount(OrderFilter orderFilter) {
        return orderMapper.sumOrderAmount(orderFilter);
    }

}
