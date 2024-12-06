package com.xzgedu.supercv.user.service;

import com.xzgedu.supercv.common.exception.FetchWxUserInfoFailedException;
import com.xzgedu.supercv.user.domain.WechatUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WechatService {

    @Autowired
    private WechatClient wechatClient;

    public WechatUser fetchUserInfoFromWechat(String code) throws FetchWxUserInfoFailedException {
        return wechatClient.fetchWxUserInfo(code);
    }
}