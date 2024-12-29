package com.xzgedu.supercv.oss.domain;

import lombok.Data;

@Data
public class StsToken {
    private Long uid;
    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private long expiration;
}
