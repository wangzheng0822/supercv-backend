package com.xzgedu.supercv.user.service;

import com.xzgedu.supercv.common.exception.BindTelDuplicatedException;
import com.xzgedu.supercv.common.exception.SmsCodeExpiredException;
import com.xzgedu.supercv.common.exception.SmsCodeUnmatchedException;
import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SmsService smsService;

    public boolean addUser(User user) {
        return userRepo.addUser(user);
    }

    public User getUserById(Long uid) {
        return userRepo.getUserById(uid);
    }

    public User getUserByOpenId(String openId) {
        return userRepo.getUserByOpenId(openId);
    }

    public User getUserByUnionId(String unionId) {
        return userRepo.getUserByUnionId(unionId);
    }

    public User getUserByTelephone(String telephone) {
        return userRepo.getUserByTelephone(telephone);
    }

    public List<User> getUsersByNickName(String nickName, int limitOffset, int limitSize) {
        return userRepo.getUsersByNickName(nickName, limitOffset, limitSize);
    }

    public int countUsersByNickName(String nickName) {
        return userRepo.countUsersByNickName(nickName);
    }

    public List<User> listUsers(int limitOffset, int limitSize) {
        return userRepo.listUsers(limitOffset, limitSize);
    }

    public int countUsersByDuration(Date startTime, Date endTime) {
        return userRepo.countUsersByDuration(startTime, endTime);
    }

    public List<User> getUsersByUids(List<Long> uids) {
        return userRepo.getUsersByUids(uids);
    }

    public boolean updateNickName(long uid, String nickName) {
        return userRepo.updateNickName(uid, nickName);
    }

    public boolean updateHeadImgUrl(long uid, String headImgUrl) {
        return userRepo.updateHeadImgUrl(uid, headImgUrl);
    }

    public boolean bindTelephone(long uid, String telephone, String smsCode)
            throws SmsCodeUnmatchedException, BindTelDuplicatedException, SmsCodeExpiredException {
        // 验证短信验证码
        smsService.verifySmsCode(telephone, smsCode);

        User user = userRepo.getUserByTelephone(telephone);
        // 已绑定本账号
        if (user != null && user.getId() == uid) {
            return true;
        }

        // 已绑定其他账号
        if (user != null && user.getId() != uid) {
            throw new BindTelDuplicatedException("Bind telephone duplicated: [uid="
                    + uid + ", tel=" + telephone + "]");
        }

        // 去绑定
        return userRepo.updateTelephone(uid, telephone);
    }
}
