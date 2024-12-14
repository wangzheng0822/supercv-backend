package com.xzgedu.supercv.order.domain;

import com.xzgedu.supercv.common.anotation.ViewData;
import com.xzgedu.supercv.order.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long uid;

    private Long productId;
    @ViewData
    private String productName;

    private Date orderTime;

    private BigDecimal paymentAmount;
    private String paymentNo3rd;
    private Integer paymentChannelType;
    private Long paymentChannelId;
    @ViewData
    private String paymentChannelName;
    private int paymentStatus;
    private Date paymentTime;
    private String paymentUrl;

    private Integer grantStatus;
    private Date grantTime;

    private String userComment;
    private String adminComment;

    //订单处于未支付状态时，支持继续支付
    public String getPaymentUrl() {
        if (paymentStatus == PaymentStatus.PENDING.getValue()) {
            return paymentUrl;
        } else {
            return null;
        }
    }
}
