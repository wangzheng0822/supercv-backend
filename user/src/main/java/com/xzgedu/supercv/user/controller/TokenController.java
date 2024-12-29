package com.xzgedu.supercv.user.controller;

import com.xzgedu.supercv.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping("/v1/auth/token")
@RestController
public class TokenController {

    @Autowired
    private AuthService authService;

    @PostMapping("/delete")
    public void deleteToken(@RequestHeader("uid") long uid,
                            @RequestParam("token") String token) {
        authService.deleteToken(token);
    }

    @GetMapping("/expire")
    public long getExpiredTime(@RequestHeader("uid") long uid,
                               @RequestParam("token") String token) {
        Date expiredTime = authService.getExpiredTime(token);
        if (expiredTime == null) return -1;
        return expiredTime.getTime();
    }
}
