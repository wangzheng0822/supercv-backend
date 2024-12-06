package com.xzgedu.supercv.user.service;

import com.alibaba.fastjson2.JSONObject;
import com.xzgedu.supercv.common.exception.FetchWxUserInfoFailedException;
import com.xzgedu.supercv.user.domain.WechatUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 同于实现微信登录之后微信信息获取
 * @author wangzheng
 */
@Slf4j
@Component
public class WechatClient {

    @Value("${wxOpen.appID}")
    private String appID;

    @Value("${wxOpen.appSecret}")
    private String appSecret;

    public WechatUser fetchWxUserInfo(String code) throws FetchWxUserInfoFailedException {
        try {
            String accessTokenUrl = generateAccessTokenUrl(code);
            String accessTokenResp = httpGet(accessTokenUrl);
            if (accessTokenResp == null || accessTokenResp.contains("errcode")) {
                throw new FetchWxUserInfoFailedException(
                        "Failed to fetch accessToken for code: " + code + ", wx error info: " + accessTokenResp);
            }
            JSONObject jsonObject = JSONObject.parseObject(accessTokenResp);
            String accessToken = jsonObject.getString("access_token");
            String openId = jsonObject.getString("openid");
            if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(openId)) {
                throw new FetchWxUserInfoFailedException("Failed to fetchWxUserInfo for code: " + code);
            }

            String wxUserInfoUrl = generateWxUserInfoUrl(accessToken, openId);
            String wxUserInfoResp = httpGet(wxUserInfoUrl);
            if (wxUserInfoResp == null || wxUserInfoResp.contains("errcode")) {
                throw new FetchWxUserInfoFailedException(
                        "Failed to fetch wxUserInfo for code: " + code + ", wx error info: " + wxUserInfoResp);
            }
            jsonObject = JSONObject.parseObject(wxUserInfoResp);
            WechatUser wxUser = new WechatUser();
            wxUser.setOpenId(jsonObject.getString("openid"));
            if (jsonObject.containsKey("unionid")) {
                wxUser.setUnionId(jsonObject.getString("unionid"));
            }
            wxUser.setNickName(jsonObject.getString("nickname"));
            wxUser.setHeadImgUrl(jsonObject.getString("headimgurl"));
            return wxUser;
        } catch (IOException e) {
            throw new FetchWxUserInfoFailedException("Failed to fetchWxUserInfo for code: " + code, e);
        }
    }

    private String generateAccessTokenUrl(String code) {
        return "https://api.weixin.qq.com/sns/oauth2/access_token"
                + "?appid=" + appID
                + "&secret=" + appSecret
                + "&code=" + code
                + "&grant_type=authorization_code";
    }

    private String generateWxUserInfoUrl(String accessToken, String openId) {
        return "https://api.weixin.qq.com/sns/userinfo"
                + "?access_token=" + accessToken
                + "&openid=" + openId;
    }

    private String httpGet(String url) throws IOException {
        // TODO: set timeout!
        OkHttpClient okHttpClient = new OkHttpClient();
        Request req = new Request.Builder().url(url).get().build();
        Response resp = okHttpClient.newCall(req).execute();
        return resp.body().string();
    }
}
