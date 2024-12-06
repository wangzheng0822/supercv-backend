package com.xzgedu.supercv.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * 用户登陆认证Token
 * @author wangzheng
 */
@Data
public class AuthToken {
    @JsonIgnore
    private long id;

    private String token;
    private long uid;
    private Date expireTime;
}