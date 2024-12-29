package com.xzgedu.supercv.vip.controller;

import com.xzgedu.supercv.vip.domain.Vip;
import com.xzgedu.supercv.vip.service.VipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name="会员")
@RequestMapping("/v1/vip")
@RestController
public class VipController {

    @Autowired
    private VipService vipService;

    @Operation(summary = "查询用户的会员信息")
    @GetMapping("/info")
    public Vip getVipInfo(@RequestHeader("uid") long uid) {
        return vipService.getVipInfo(uid);
    }

    @GetMapping("/check-if-valid")
    public boolean checkIfValidVip(@RequestHeader("uid") long uid) {
        return vipService.permitValidVip(uid);
    }
}
