package com.xzgedu.supercv.order.service;

import com.alibaba.fastjson2.util.DateUtils;
import com.xzgedu.supercv.common.exception.GenericBizException;
import com.xzgedu.supercv.common.utils.StringRandom;
import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.mapper.OrderMapper;
import com.xzgedu.supercv.order.repo.OrderRepo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.assertj.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;

    // 支付状态
    Integer PAY_PENDING = 0;
    Integer PAY_SUCCESS = 1;
    Integer PAY_OVERTIME = 2;
    Integer PAY_FAILED = 3;

    // 权益状态
    Integer GRANT_PENDING = 0;
    Integer GRANT_SUCCESS = 1;
    Integer GRANT_FAILED = 2;

    // 支付渠道
    Integer PAY_CHANNEL_TYPE_WECHAT = 0;
    Integer PAY_CHANNEL_TYPE_ALIPAY = 1;

    public String generateOrderNo(Long userId) {
        // 1. 获取当前时间并格式化为10位(年用两位)
        LocalDateTime now = LocalDateTime.now();
        String timeStr = now.format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));

        // 2. 处理用户ID后4位，不足补0
        String userIdStr = String.format("%04d", userId % 10000);

        // 3. 生成4位随机数
        String randomStr = StringRandom.genStrByDigit(4);

        // 4. 组合订单号
        return timeStr + userIdStr + randomStr;
    }

    public Order getOrderById(long id) {
        return orderRepo.getOrderById(id);
    }

    public Order getOrderByOrderNo(String orderNo) {
        return orderRepo.getOrderByOrderNo(orderNo);
    }

    public List<Order> listOrdersByUserId(Long userId, int limitOffset, int limitSize) {
        return orderRepo.listOrdersByUserId(userId, limitOffset, limitSize);
    }

    public int countOrderByUserid(Long userId) {
        return orderRepo.countOrderByUserId(userId);
    }

    public int countOrderByCondition(Map<String, Object> params) {
        return orderRepo.countOrderByCondition(params);
    }

    public List<Order> listOrdersByCondition(Map<String, Object> params) {
        return orderRepo.listOrdersByCondition(params);
    }

    public boolean addOrder(Order order) {
        order.setOrderTime(DateUtil.now());
        order.setPaymentStatus(PAY_PENDING);
        order.setGrantStatus(GRANT_PENDING);
        return orderRepo.addOrder(order);
    }

    public boolean updatePaymentStatus(
        Long id, 
        Integer paymentStatus, 
        String paymentNo3rd, 
        Integer paymentChannelType, 
        Long paymentChannelId) {
        if (Objects.isNull(id) || Objects.isNull(paymentStatus)) {
            return false;
        }

        // 支付成功时需要更新支付相关信息
        if (PAY_SUCCESS.equals(paymentStatus)) {
            return orderRepo.updatePaymentStatusSuccess(
                    id,
                    paymentNo3rd,
                    paymentChannelType,
                    paymentChannelId
            );
        }

        // 其他状态只需更新支付状态
        return orderRepo.updatePaymentStatusNotSuccess(id, paymentStatus);
    }

    public boolean updateGrantStatus(Long id, Integer grantStatus) {
        if (Objects.isNull(grantStatus)) {
            return false;
        }
        if (GRANT_SUCCESS.equals(grantStatus)) {
            return orderRepo.updateGrantStatusSuccess(id);
        }
        return orderRepo.updateGrantStatusNotSuccess(id, grantStatus);
    }

    public boolean updateUserComment(Long id, String userComment) {
        return orderRepo.updateUserComment(id, userComment);
    }

    public boolean updateAdminComment(Long id, String adminComment) {
        return orderRepo.updateAdminComment(id, adminComment);
    }

    public boolean updateLogicDeletion(long id) {
        return orderRepo.updateLogicDeletion(id);
    }

    public int batchUpdatePaymentStatusOverTime() throws GenericBizException{
        try {
            return orderRepo.batchUpdatePaymentStatusOverTime();
        } catch (Exception e) {
            throw new GenericBizException("批量更新超时订单失败: " + e.getMessage());
        }
    }

    //TODO为订单列表填充产品名称
    public List<Order> batchFillInProductName(List<Order> orders) {
        return orders;
    }
}
