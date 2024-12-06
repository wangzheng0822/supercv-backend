package com.xzgedu.supercv;

import com.xzgedu.supercv.common.utils.StringRandom;
import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.service.AuthService;
import com.xzgedu.supercv.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAccountSetUp {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    public User createRandomUser() {
        return createUser(null, null);
    }

    public AuthToken createRandomUserAndLogin() {
        User user = createRandomUser();
        return createAuthToken(user.getId());
    }

    public User createUser(Long id, String telephone) {
        User user = new User();
        if (id != null) {
            user.setId(id);
        }
        user.setNickName("user" + StringRandom.genStrByDigit(4));
        user.setHeadImgUrl("https://img.supercv.cn/avatar/default.png");
        if (telephone != null) {
            user.setTelephone(telephone);
        } else {
            user.setTelephone("139" + StringRandom.genStrByDigit(8));
        }
        user.setUnionId("u_" + StringRandom.genStrByDigitAndLetter(10));
        user.setOpenId("o_" + StringRandom.genStrByDigitAndLetter(10));
        userService.addUser(user);
        return user;
    }

    public AuthToken createAuthToken(long uid) {
        return authService.createToken(uid);
    }
}
