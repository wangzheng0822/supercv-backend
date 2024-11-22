package com.xzgedu.supercv.user.controller;

import com.xzgedu.supercv.user.domain.OkTest;
import com.xzgedu.supercv.user.service.OkTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/oktest")
public class OkTestController {

    @Autowired
    private OkTestService okTestService;

    @GetMapping("/ping")
    public String ping() {
        log.info("ping...");
        return "pong";
    }

    @GetMapping("/select")
    public OkTest selectOkTest(@RequestParam("id") long id) {
        return okTestService.selectOkTest(id);
    }

    @PostMapping("/insert")
    public boolean insertOkTest(@RequestParam("name") String name) {
        OkTest okTest = new OkTest();
        okTest.setName(name);
        return okTestService.insertOkTest(okTest);
    }

    @PostMapping("/delete")
    public boolean deleteOkTest(@RequestParam("id") long id) {
        return okTestService.deleteOkTest(id);
    }

    @PostMapping("/update")
    public boolean updateOkTest(@RequestParam("id") long id, @RequestParam("name") String name) {
        OkTest okTest = new OkTest();
        okTest.setId(id);
        okTest.setName(name);
        return okTestService.updateOkTest(okTest);
    }
}