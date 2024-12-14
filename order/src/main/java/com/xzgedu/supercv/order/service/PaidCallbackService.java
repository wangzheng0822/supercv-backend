package com.xzgedu.supercv.order.service;

import com.xzgedu.supercv.common.exception.OrderNotFoundException;
import com.xzgedu.supercv.common.exception.PaymentException;
import com.xzgedu.supercv.order.domain.Order;
import com.xzgedu.supercv.order.enums.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class PaidCallbackService {

    @Autowired
    private OrderService orderService;

    // 支付回调，需要幂等
    @Transactional(rollbackFor = Exception.class)
    public void completePayment(String orderNo,
                                String orderNo3rd,
                                Date paymentTime,
                                PaymentStatus paymentStatus)
            throws OrderNotFoundException, PaymentException {
        Order order = orderService.getOrderByNo(orderNo);
        if (order == null) {
            throw new OrderNotFoundException("Failed to find order by order no: " + orderNo);
        }

        //使用数据库锁做幂等，paymentStatus==待支付，才可以更新状态（失败、成功）
        if (!orderService.updatePaidInfo(orderNo, orderNo3rd, paymentTime, paymentStatus.getValue())) {
            throw new PaymentException("Failed to update paid info for order: " + order.getId());
        }

        //支付成功，开通权限
        if (paymentStatus.equals(PaymentStatus.PAID)) {
            orderService.grant(order.getUid(), order.getId(), order.getProductId());
        }

        //支付失败
        if (paymentStatus.equals(PaymentStatus.FAILED)) {
            if (!orderService.updatePaymentStatus(order.getId(), PaymentStatus.FAILED.getValue())) {
                throw new PaymentException("Failed to update payment status for order: " + order.getId());
            }
        }
    }
}
