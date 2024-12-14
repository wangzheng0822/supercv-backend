package com.xzgedu.supercv.order.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OrderFilter {
    private String orderNo;
    private Long productId;
    private Long paymentChannelId;
    private Integer paymentStatus;
    private Integer grantStatus;
    private Long uid;
    private Date startTime;
    private Date endTime;
}
