package com.xzgedu.supercv.admin.controller;

import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.service.UserService;
import com.xzgedu.supercv.vip.domain.Vip;
import com.xzgedu.supercv.vip.service.VipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name="会员管理")
@RequestMapping("/admin/vip")
@RestController
public class AdminVipController {

    @Autowired
    private UserService userService;

    @Autowired
    private VipService vipService;

    //list or search 会员
    @Operation(summary = "分页查询会员")
    @GetMapping("/list")
    public Map<String, Object> getVipUsers(@RequestParam(value = "telephone", required = false) String telephone,
                                           @RequestParam(value = "page_no") Integer pageNo,
                                           @RequestParam(value = "page_size") Integer pageSize) {
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize == null) pageSize = 10;
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;

        List<Vip> vips = new ArrayList<>();
        if (telephone != null && !telephone.isEmpty()) {
            User user = userService.getUserByTelephone(telephone);
            if (user != null) {
                Vip vip = vipService.getVipInfo(user.getId());
                if (vip != null) {
                    vips.add(vip);
                }
            }
        } else {
            vips.addAll(vipService.getVipsByPagination(limitOffset, limitSize));
        }

        int count = 0;
        if (telephone == null || telephone.isEmpty()) {
            count = vipService.countVips(null, null);
        } else {
            count = vips.size();
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("vips", fillViewData(vips));
        resp.put("count", count);
        return resp;
    }

    private List<Vip> fillViewData(List<Vip> vips) {
        if (vips == null || vips.isEmpty()) return vips;

        List<Long> uids = new ArrayList<>();
        for (Vip vip : vips) {
            uids.add(vip.getUid());
        }
        if (uids != null && !uids.isEmpty()) {
            List<User> users = userService.getUsersByUids(uids);
            Map<Long, User> userMap = new HashMap<>();
            for (User user : users) {
                userMap.put(user.getId(), user);
            }
            for (Vip vip : vips) {
                User user = userMap.get(vip.getUid());
                if (user != null) {
                    vip.setNickName(user.getNickName());
                    vip.setHeadImgUrl(user.getHeadImgUrl());
                }
            }
        }

        return vips;
    }
}