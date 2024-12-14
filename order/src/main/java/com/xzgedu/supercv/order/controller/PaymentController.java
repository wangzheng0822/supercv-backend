package com.xzgedu.supercv.order.controller;

import com.xzgedu.supercv.order.service.PaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v1/payment")
@RestController
public class PaymentController {

    @Autowired
    private PaymentChannelService paymentChannelService;

    @GetMapping("/channel/type/enabled")
    public List<Integer> getEnabledPaymentChannelTypes() {
        return paymentChannelService.getEnabledPaymentChannelTypes();
    }
}
