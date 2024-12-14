package com.xzgedu.supercv;

import com.xzgedu.supercv.admin.service.AdminService;
import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountSetup {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserAccountSetUp userAccountSetUp;

    public AuthToken createAdminAccountAndLogin() {
        AuthToken authToken = userAccountSetUp.createRandomUserAndLogin();
        adminService.grantAdmin(authToken.getUid());
        return authToken;
    }
}
