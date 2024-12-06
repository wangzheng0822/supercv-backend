package com.xzgedu.supercv.user.domain;

import lombok.Data;

/**
 * 用于保存从微信获取到的用户信息
 * @author wangzheng
 */
@Data
public class WechatUser {
    private String unionId;
    private String openId;
    private String nickName;
    private String headImgUrl;
}