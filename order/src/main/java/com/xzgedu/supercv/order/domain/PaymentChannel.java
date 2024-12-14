package com.xzgedu.supercv.order.domain;

import lombok.Data;

@Data
public class PaymentChannel {
    private Long id;
    private int type; //1-微信支付 2-支付宝
    private String name;
    private String appId;
    private String mchId;
    private String secret;
    private String apiUrl;
    private String callbackUrl;
    private String returnUrl;
    private boolean enabled;
}