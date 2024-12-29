package com.xzgedu.supercv.oss.service;

import com.aliyun.sts20150401.Client;
import com.aliyun.sts20150401.models.AssumeRoleRequest;
import com.aliyun.sts20150401.models.AssumeRoleResponse;
import com.aliyun.teaopenapi.models.Config;
import com.xzgedu.supercv.oss.repo.StsToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
@Slf4j
public class StsClient {

    private Client client;

    public StsClient(@Value("${aliyun.ossAccessKeyId}") String accessKeyId,
                     @Value("${aliyun.ossAccessKeySecret}") String accessKeySecret) {
        Config config = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
        config.endpoint = "sts.cn-hangzhou.aliyuncs.com";
        try {
            this.client = new Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StsToken requestStsToken(Long uid) {
        AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest();
        assumeRoleRequest.setDurationSeconds(3600l);
        assumeRoleRequest.setRoleArn("acs:ram::1518650627705807:role/oss-write");
        if (uid != null) {
            assumeRoleRequest.setRoleSessionName(String.valueOf(uid));
        } else {
            assumeRoleRequest.setRoleSessionName("nobody");
        }
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            AssumeRoleResponse resp = client.assumeRoleWithOptions(assumeRoleRequest, runtime);
            StsToken stsToken = new StsToken();
            stsToken.setUid(uid);
            stsToken.setAccessKeyId(resp.getBody().getCredentials().getAccessKeyId());
            stsToken.setAccessKeySecret(resp.getBody().getCredentials().getAccessKeySecret());
            stsToken.setSecurityToken(resp.getBody().getCredentials().getSecurityToken());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            stsToken.setExpiration(sdf.parse(resp.getBody().getCredentials().getExpiration()).getTime());
            return stsToken;
        } catch (Exception _error) {
            log.error("", _error);
            throw new RuntimeException("Failed to get Sts token from ali for user: " + uid);
        }
    }
}
