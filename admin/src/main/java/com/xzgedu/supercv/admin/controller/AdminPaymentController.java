package com.xzgedu.supercv.admin.controller;

import com.xzgedu.supercv.common.exception.GenericBizException;
import com.xzgedu.supercv.order.domain.PaymentChannel;
import com.xzgedu.supercv.order.service.PaymentChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="支付渠道管理")
@RequestMapping("/admin/payment")
@RestController
public class AdminPaymentController {

    @Autowired
    private PaymentChannelService paymentChannelService;

    @Operation(summary = "获取所有支付渠道")
    @GetMapping("/channel/list")
    public List<PaymentChannel> getAllPaymentChannels() {
        return paymentChannelService.getAllPaymentChannels();
    }

    @Operation(summary = "添加支付渠道")
    @PostMapping("/channel/add")
    public PaymentChannel addPaymentChannel(@RequestParam("type") Integer type,
                                            @RequestParam("name") String name,
                                            @RequestParam(value = "app_id", required = false) String appId,
                                            @RequestParam(value = "mch_id", required = false) String mchId,
                                            @RequestParam(value = "secret", required = false) String secret,
                                            @RequestParam(value = "api_url", required = false) String apiUrl,
                                            @RequestParam(value = "callback_url", required = false) String callbackUrl,
                                            @RequestParam(value = "return_url", required = false) String returnUrl,
                                            @RequestParam(value = "enabled", required = false) Boolean enabled) throws GenericBizException {
        if (enabled == null) enabled = false;
        PaymentChannel paymentChannel = new PaymentChannel();
        paymentChannel.setType(type);
        paymentChannel.setName(name);
        paymentChannel.setAppId(appId);
        paymentChannel.setMchId(mchId);
        paymentChannel.setSecret(secret);
        paymentChannel.setApiUrl(apiUrl);
        paymentChannel.setCallbackUrl(callbackUrl);
        paymentChannel.setReturnUrl(returnUrl);
        paymentChannel.setEnabled(enabled);
        if (paymentChannelService.addPaymentChannel(paymentChannel)) return paymentChannel;
        throw new GenericBizException("Failed to add payment channel: " + paymentChannel.toString());
    }

    @Operation(summary = "更新支付渠道")
    @PostMapping("/channel/update")
    public void updatePaymentChannel(@RequestParam("id") long id,
                                     @RequestParam("type") Integer type,
                                     @RequestParam("name") String name,
                                     @RequestParam(value = "app_id", required = false) String appId,
                                     @RequestParam(value = "mch_id", required = false) String mchId,
                                     @RequestParam(value = "secret", required = false) String secret,
                                     @RequestParam(value = "api_url", required = false) String apiUrl,
                                     @RequestParam(value = "callback_url", required = false) String callbackUrl,
                                     @RequestParam(value = "return_url", required = false) String returnUrl,
                                     @RequestParam(value = "enabled", required = false) Boolean enabled) throws GenericBizException {
        if (enabled == null) enabled = false;
        PaymentChannel paymentChannel = new PaymentChannel();
        paymentChannel.setId(id);
        paymentChannel.setType(type);
        paymentChannel.setName(name);
        paymentChannel.setAppId(appId);
        paymentChannel.setMchId(mchId);
        paymentChannel.setSecret(secret);
        paymentChannel.setApiUrl(apiUrl);
        paymentChannel.setCallbackUrl(callbackUrl);
        paymentChannel.setReturnUrl(returnUrl);
        paymentChannel.setEnabled(enabled);
        if (paymentChannelService.updatePaymentChannel(paymentChannel)) return;
        throw new GenericBizException("Failed to update payment channel: " + paymentChannel.toString());
    }

    @Operation(summary = "删除支付渠道")
    @PostMapping("/channel/delete")
    public void deletePaymentChannel(@RequestParam("id") long id) throws GenericBizException {
        if (paymentChannelService.deletePaymentChannel(id)) return;
        throw new GenericBizException("Failed to delete payment channel: " + id);
    }
}
