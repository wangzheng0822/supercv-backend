package com.xzgedu.supercv.order.controller;

import com.xzgedu.supercv.order.service.PaymentChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "支付渠道")
@RequestMapping("/v1/payment")
@RestController
public class PaymentController {

    @Autowired
    private PaymentChannelService paymentChannelService;

    @Operation(summary = "获取所有启用的支付渠道类型")
    @GetMapping("/channel/type/enabled")
    public List<Integer> getEnabledPaymentChannelTypes() {
        return paymentChannelService.getEnabledPaymentChannelTypes();
    }
}
