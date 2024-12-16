package com.xzgedu.supercv.admin.controller.account;

import com.xzgedu.supercv.admin.service.AdminService;
import com.xzgedu.supercv.common.exception.GenericBizException;
import com.xzgedu.supercv.common.exception.UserNotFoundException;
import com.xzgedu.supercv.common.utils.Assert;
import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name="管理员账户管理接口")
@RestController
@RequestMapping("/admin/account")
public class AdminAccountController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Operation(summary = "检查是否是管理员")
    @GetMapping("/check")
    public boolean checkIsAdmin(@RequestParam("uid") long uid) {
        return adminService.checkIfAdmin(uid);
    }

    @Operation(summary = "添加管理员")
    @PostMapping("/add")
    public void grantAdmin(@RequestParam("telephone") String telephone)
            throws UserNotFoundException, GenericBizException {
        Assert.checkTelephone(telephone);
        User user = userService.getUserByTelephone(telephone);
        if (user == null) {
            throw new UserNotFoundException("Failed to find user: " + telephone);
        }
        if (!adminService.grantAdmin(user.getId())) {
            throw new GenericBizException("Failed to add admin: " + telephone);
        }
    }

    @Operation(summary = "删除管理员")
    @PostMapping("/delete")
    public void removeAdmin(@RequestParam("telephone") String telephone)
            throws UserNotFoundException, GenericBizException {
        Assert.checkTelephone(telephone);
        User user = userService.getUserByTelephone(telephone);
        if (user == null) {
            throw new UserNotFoundException("Failed to find user: " + telephone);
        }
        if (!adminService.deleteAdmin(user.getId())) {
            throw new GenericBizException("Failed to delete admin: " + telephone);
        }
    }
}