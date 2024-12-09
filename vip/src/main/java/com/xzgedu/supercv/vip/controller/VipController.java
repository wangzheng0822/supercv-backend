package com.xzgedu.supercv.vip.controller;

import com.xzgedu.supercv.vip.domain.Vip;
import com.xzgedu.supercv.vip.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/vip")
@RestController
public class VipController {

    @Autowired
    private VipService vipService;

    @GetMapping("/info")
    public Vip getVipInfo(@RequestHeader("uid") long uid) {
        return vipService.getVipInfo(uid);
    }
}
