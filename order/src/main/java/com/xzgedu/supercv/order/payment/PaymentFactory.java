package com.xzgedu.supercv.order.payment;

import com.xzgedu.supercv.order.enums.PaymentChannelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {

    @Autowired
    private WeChatPayment weChatPayment;

    @Autowired
    private MockPayment mockPayment;

    public Payment getPayment(PaymentChannelType paymentChannelType) {
        if (paymentChannelType.equals(PaymentChannelType.WXPAY)) {
            return weChatPayment;
        } else if (paymentChannelType.equals(PaymentChannelType.MOCK_PAYMENT)) {
            return mockPayment;
        }
        return null;
    }
}
