package com.xzgedu.supercv.order.payment;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.xzgedu.supercv.order.domain.PaymentChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class WeChatPayment implements Payment {

    private String appId;

    private String merchantId;

    public RSAAutoCertificateConfig config;

    private NativePayService service;

    public WeChatPayment(@Value("${wxPay.privateKeyPath}") String privateKeyPath,
                         @Value("${wxPay.appId}") String appId,
                         @Value("${wxPay.merchantId}") String merchantId,
                         @Value("${wxPay.apiV3Key}") String apiV3Key,
                         @Value("${wxPay.merchantSerialNumber}") String merchantSerialNumber) {
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        this.appId = appId;
        this.merchantId = merchantId;
        try {
            this.config = new RSAAutoCertificateConfig.Builder()
                    .merchantId(merchantId)
                    .privateKeyFromPath(privateKeyPath)
                    .merchantSerialNumber(merchantSerialNumber)
                    .apiV3Key(apiV3Key)
                    .build();
            this.service = new NativePayService.Builder().config(this.config).build();
        } catch (Exception e) {
            log.error("Failed to initialize WeChatPayment.");
        }
    }

    @Override
    public String createPaymentQrUrl(PaymentChannel payChannel, String orderNo, BigDecimal amount,
                                     String productName, String attach) {
        com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest request =
                new com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest();
        com.wechat.pay.java.service.payments.nativepay.model.Amount amountBo =
                new com.wechat.pay.java.service.payments.nativepay.model.Amount();
        amountBo.setTotal(amount.multiply(new BigDecimal("100")).intValue()); //AmountBo中的total的单位是分
        request.setAmount(amountBo);
        request.setAppid(this.appId);
        request.setMchid(this.merchantId);
        request.setDescription(productName);
        request.setNotifyUrl(payChannel.getCallbackUrl());
        request.setOutTradeNo(orderNo);
        com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse response = service.prepay(request);
        return response.getCodeUrl();
    }
}
