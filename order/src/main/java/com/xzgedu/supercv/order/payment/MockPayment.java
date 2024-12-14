package com.xzgedu.supercv.order.payment;

import com.xzgedu.supercv.order.domain.PaymentChannel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MockPayment implements Payment{

    //TODO:填写二维码地址
    private static final String MOCK_PAYMENT_QR_URL = "";

    @Override
    public String createPaymentQrUrl(PaymentChannel payChannel, String orderNo, BigDecimal amount, String productName, String attach) {
        return MOCK_PAYMENT_QR_URL;
    }
}
