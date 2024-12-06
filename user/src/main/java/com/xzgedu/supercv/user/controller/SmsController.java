package com.xzgedu.supercv.user.controller;

import com.xzgedu.supercv.common.exception.NoPermissionException;
import com.xzgedu.supercv.common.exception.RequestTooFrequentException;
import com.xzgedu.supercv.common.exception.SendSmsCodeFailedException;
import com.xzgedu.supercv.common.utils.Assert;
import com.xzgedu.supercv.user.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name="短信验证码接口")
@Slf4j
@RestController
@RequestMapping("/v1/smscode")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Operation(summary = "发送短信验证码")
    @PostMapping(path = "/send")
    public void sendSmsCode(@RequestParam("telephone") String telephone,
                              @RequestParam(value = "ticket", required = false) String ticket,
                              @RequestParam(value = "rand_str", required = false) String randStr,
                              @RequestParam(value = "user_ip", required = false) String userIp)
            throws SendSmsCodeFailedException, RequestTooFrequentException, NoPermissionException {
        Assert.checkTelephone(telephone);
        smsService.sendSmsCode(telephone, ticket, randStr, userIp);
    }
}