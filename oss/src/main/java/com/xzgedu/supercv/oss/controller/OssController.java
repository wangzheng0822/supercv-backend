package com.xzgedu.supercv.oss.controller;

import com.xzgedu.supercv.oss.service.StsService;
import com.xzgedu.supercv.oss.repo.StsToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/oss")
public class OssController {

    @Autowired
    private StsService stsService;

    @GetMapping("/sts")
    public StsToken getStsToken(@RequestParam(value = "uid", required = false) Long uid) {
        return stsService.getStsToken(uid);
    }
}
