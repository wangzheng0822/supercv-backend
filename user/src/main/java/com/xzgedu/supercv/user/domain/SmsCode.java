package com.xzgedu.supercv.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * 短信验证码
 * @author wangzheng
 */
@Data
public class SmsCode {
    @JsonIgnore
    private long id;

    private String telephone;
    private String code;
    private boolean used;
    private Date sendTime;
}