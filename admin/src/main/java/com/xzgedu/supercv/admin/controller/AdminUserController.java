package com.xzgedu.supercv.admin.controller;

import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.service.UserService;
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

@Tag(name = "用户管理接口")
@RequestMapping("/admin/user")
@RestController
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "分页查询用户")
    @GetMapping("/list")
    public Map<String, Object> listUsers(@RequestParam(value = "search_type", required = false) String searchType,
                                         @RequestParam(value = "search_text", required = false) String searchText,
                                         @RequestParam(value = "page_no") Integer pageNo,
                                         @RequestParam(value = "page_size") Integer pageSize) {
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize == null) pageSize = 10;
        int limitOffset = (pageNo - 1) * pageSize;
        int limitSize = pageSize;

        List<User> users = new ArrayList<>();
        int count = 0;
        if (searchType == null || searchType.isEmpty() || searchText == null || searchText.isEmpty()) {
            users.addAll(userService.listUsers(limitOffset, limitSize));
            count = userService.countUsersByDuration(null, null);
        } else if (searchType.equals("telephone")) {
            User user = userService.getUserByTelephone(searchText);
            if (user != null) {
                users.add(user);
                count = 1;
            }
        } else if (searchType.equals("uid")) {
            User user = userService.getUserById(Long.parseLong(searchText));
            if (user != null) {
                users.add(user);
                count = 1;
            }
        } else if (searchType.equals("nickname")) {
            users.addAll(userService.getUsersByNickName(searchText, limitOffset, limitSize));
            count = userService.countUsersByNickName(searchText);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("count", count);
        resp.put("users", users);
        return resp;
    }
}
