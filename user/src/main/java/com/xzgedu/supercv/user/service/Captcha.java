package com.xzgedu.supercv.user.service;

import com.tencentcloudapi.captcha.v20190722.CaptchaClient;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultRequest;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Captcha安全验证码
 * @author wangzheng
 */
@Slf4j
@Component
public class Captcha {

    @Value("${tencent.secretId}")
    private String secretId;

    @Value("${tencent.secretKey}")
    private String secretKey;

    @Value("${captcha.captchaAppId}")
    private String captchaAppId;

    @Value("${captcha.appSecretKey}")
    private String appSecretKey;

    public boolean verifyTicket(String ticket, String randStr, String userIp) {
        Credential credential = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("captcha.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        CaptchaClient client = new CaptchaClient(credential, "", clientProfile);
        DescribeCaptchaResultRequest req = new DescribeCaptchaResultRequest();
        req.setCaptchaType(9L);
        req.setCaptchaAppId(Long.parseLong(captchaAppId));
        req.setAppSecretKey(appSecretKey);
        req.setTicket(ticket);
        req.setRandstr(randStr);
        req.setUserIp(userIp);
        try {
            DescribeCaptchaResultResponse resp = client.DescribeCaptchaResult(req);
            return resp.getCaptchaCode() == 1;
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("Captcha failed.", e);
        }
    }
}
