package com.xzgedu.supercv.user.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.xzgedu.supercv.common.exception.SendSmsCodeFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 用于短信验证码发送
 * @author wangzheng
 */
@Slf4j
@Component
public class SmsClient {

    @Value("${sms.sign_name}")
    private String signName;

    @Value("${sms.template_code}")
    private String templateCode;

    private Client client;

    public SmsClient(@Value("${aliyun.accessKeyId}") String accessKeyId,
                     @Value("${aliyun.accessKeySecret}") String accessKeySecret) throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint("dysmsapi.aliyuncs.com");
        this.client = new Client(config);
    }

    public void sendSmsCode(String telephone, String code) throws SendSmsCodeFailedException {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setPhoneNumbers(telephone)
                .setTemplateParam("{\"code\":\"" + code + "\"}");
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
            log.debug("Sms send response status code: " + response.getStatusCode());
        } catch (Exception e) {
            throw new SendSmsCodeFailedException("Send sms code failed: " + telephone, e);
        }
    }
}
