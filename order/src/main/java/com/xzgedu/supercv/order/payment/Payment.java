package com.xzgedu.supercv.order.payment;

import com.xzgedu.supercv.common.exception.PaymentException;
import com.xzgedu.supercv.order.domain.PaymentChannel;

import java.math.BigDecimal;

public interface Payment {

    String createPaymentQrUrl(PaymentChannel payChannel, String orderNo, BigDecimal amount,
                              String productName, String attach);
}