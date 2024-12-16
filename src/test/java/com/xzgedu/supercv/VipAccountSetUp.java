package com.xzgedu.supercv;

import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.vip.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VipAccountSetUp {

    @Autowired
    private UserAccountSetUp userAccountSetUp;

    @Autowired
    private VipService vipService;

    public AuthToken createVipAccountAndLogin()
    {
        AuthToken authToken = userAccountSetUp.createRandomUserAndLogin();
        vipService.renewVip(authToken.getUid(), 30, 5, 5);
        return authToken;
    }
}
