package com.xzgedu.supercv.order.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xzgedu.supercv.common.anotation.ViewData;
import lombok.Data;
import java.util.Date;

/**
 * 用户信息类
 *
 * @author pikady
 */
@Data
public class Order {
    private long id;

    private String orderNo;

    private Long userId;

    @ViewData
    private String userName;

    private Long productId;

    @ViewData
    private String productName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderEndTime;

    private String paymentNo3rd;

    private Integer paymentChannelType;

    private Long paymentChannelId;

    @ViewData
    private String paymentChannelName;

    private Integer paymentStatus;

    private Date paymentTime;

    private Integer grantStatus;

    private Date grantTime;

    private String userComment;

    private String adminComment;

    @JsonIgnore
    private Integer isDeleted;
}
