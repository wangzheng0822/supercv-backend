package com.xzgedu.supercv.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xzgedu.supercv.common.anotation.ViewData;
import lombok.Data;

/**
 * 用户信息类
 * @author wangzheng
 */
@Data
public class User {
    private long id;
    private String telephone;

    @JsonIgnore
    private String unionId;
    @JsonIgnore
    private String openId;

    private String nickName;
    private String headImgUrl;

    @ViewData
    private boolean vip;
}