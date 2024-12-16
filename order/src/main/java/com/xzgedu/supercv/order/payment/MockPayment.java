package com.xzgedu.supercv.order.payment;

import com.xzgedu.supercv.order.domain.PaymentChannel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MockPayment implements Payment{

    private static final String MOCK_PAYMENT_QR_URL = "https://static.supercv.cn/image/default_wxpay_qr.png";

    @Override
    public String createPaymentQrUrl(PaymentChannel payChannel, String orderNo, BigDecimal amount, String productName, String attach) {
        return MOCK_PAYMENT_QR_URL;
    }
}
