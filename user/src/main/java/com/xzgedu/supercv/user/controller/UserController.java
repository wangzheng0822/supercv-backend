package com.xzgedu.supercv.user.controller;

import com.xzgedu.supercv.common.exception.*;
import com.xzgedu.supercv.common.utils.Assert;
import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "用户信息接口")
@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取个人信息")
    @GetMapping(path = "/info")
    public User getUserInfo(@RequestHeader("uid") long uid) throws UserNotFoundException {
        User user = userService.getUserById(uid);
        if (user == null) {
            throw new UserNotFoundException("User not found: [uid=" + uid + "]");
        }
        return user;
    }

    @Operation(summary = "绑定/修改手机号码")
    @PostMapping(path = "/telephone/bind")
    public void bindTelephone(@RequestHeader("uid") long uid,
                              @RequestParam("telephone") String telephone,
                              @RequestParam("sms_code") String smsCode)
            throws SmsCodeExpiredException, SmsCodeUnmatchedException,
            BindTelDuplicatedException, GenericBizException {
        Assert.checkTelephone(telephone);
        Assert.checkSmsCode(smsCode);
        if (!userService.bindTelephone(uid, telephone, smsCode)) {
            throw new GenericBizException(
                    "Failed to bind telephone: [uid=" + uid + ", telephone=" + telephone + "]");
        }
    }

    @Operation(summary = "更新昵称")
    @PostMapping("/nick-name/update")
    public void updateNickName(@RequestHeader("uid") long uid,
                               @RequestParam("nick_name") String nickName) throws GenericBizException {
        Assert.checkNickName(nickName);
        if (!userService.updateNickName(uid, nickName)) {
            throw new GenericBizException("Failed to update nick name: [uid="
                    + uid + "; nickname=" + nickName + "]");
        }
    }

    @Operation(summary = "更新头像")
    @PostMapping("/head-img-url/update")
    public void updateHeadImgUrl(@RequestHeader("uid") long uid,
                                 @RequestParam("head_img_url") String headImgUrl) throws GenericBizException {
        Assert.checkNotNull(headImgUrl);
        if (!userService.updateHeadImgUrl(uid, headImgUrl)) {
            throw new GenericBizException("Failed to update head img url: [uid="
                    + uid + "; headImgUrl=" + headImgUrl + "]");
        }
    }
}