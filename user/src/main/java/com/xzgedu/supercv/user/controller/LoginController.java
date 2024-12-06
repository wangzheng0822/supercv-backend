package com.xzgedu.supercv.user.controller;

import com.xzgedu.supercv.common.exception.FetchWxUserInfoFailedException;
import com.xzgedu.supercv.common.exception.GenericBizException;
import com.xzgedu.supercv.common.utils.Assert;
import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.domain.WechatUser;
import com.xzgedu.supercv.user.service.AuthService;
import com.xzgedu.supercv.user.service.UserService;
import com.xzgedu.supercv.user.service.WechatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "微信登陆接口")
@Slf4j
@RestController
@RequestMapping("/v1/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private AuthService authService;

    @Operation(summary = "微信登陆回调")
    @GetMapping(path = "/wechat/callback")
    public AuthToken wechatLoginCallback(@RequestParam("code") String code,
                                         @RequestParam("key") String key)
            throws FetchWxUserInfoFailedException, GenericBizException {
        Assert.checkNotNull(code);
        Assert.checkNotNull(key);
        WechatUser wxUser = wechatService.fetchUserInfoFromWechat(code);
        if (wxUser == null) {
            throw new FetchWxUserInfoFailedException("Failed to fetch wx user info: "
                    + "[code=" + code + ", key" + key + "]");
        }

        User user = null;
        if (wxUser.getUnionId() != null) {
            user = userService.getUserByUnionId(wxUser.getUnionId());
        }
        if (user == null && wxUser.getOpenId() != null) {
            user = userService.getUserByOpenId(wxUser.getOpenId());
        }

        if (user == null) {
            user = new User();
            user.setUnionId(wxUser.getUnionId());
            user.setOpenId(wxUser.getOpenId());
            user.setNickName(wxUser.getNickName());
            user.setHeadImgUrl(wxUser.getHeadImgUrl());
            if (!userService.addUser(user)) {
                throw new GenericBizException("Failed to create user: " + user);
            }
        }

        return authService.createToken(user.getId());
    }
}