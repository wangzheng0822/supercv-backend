package com.xzgedu.supercv.user.controller;

import com.xzgedu.supercv.common.exception.BizException;
import com.xzgedu.supercv.common.exception.ErrorCode;
import com.xzgedu.supercv.common.exception.GenericBizException;
import com.xzgedu.supercv.common.utils.Assert;
import com.xzgedu.supercv.common.utils.StringRandom;
import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.service.AuthService;
import com.xzgedu.supercv.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "开发模式登录接口")
@RequestMapping("/v1/login/dev")
@RestController
public class Login4DevController {

    private static final String DEFAULT_USER_HEAD_IMG_URL = "https://static.supercv.cn/image/default_head_img.png";
    private static final String DEFAULT_NICK_NAME_PREFIX = "超能用户";

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Value("${spring.profiles.active}")
    private String active;

    @Operation(summary = "手机号码登陆/注册")
    @PostMapping("/telephone")
    public AuthToken login(@RequestParam("telephone") String telephone) throws BizException {
        if (!active.equals("dev") && !active.equals("test") && !active.equals("ut")) {
            throw new BizException(ErrorCode.DENY_FOR_PROD);
        }

        Assert.checkTelephone(telephone);
        User user = userService.getUserByTelephone(telephone);
        if(user == null) {
            user = new User();
            user.setTelephone(telephone);
            user.setOpenId("o_" + StringRandom.genStrByDigitAndLetter(12));
            user.setUnionId("u_" + StringRandom.genStrByDigitAndLetter(12));
            user.setNickName(DEFAULT_NICK_NAME_PREFIX + StringRandom.genStrByDigit(4));
            user.setHeadImgUrl(DEFAULT_USER_HEAD_IMG_URL);
            if (!userService.addUser(user)) {
                throw new GenericBizException("Failed to create dev user: " + user);
            }
        }
        return authService.createToken(user.getId());
    }
}