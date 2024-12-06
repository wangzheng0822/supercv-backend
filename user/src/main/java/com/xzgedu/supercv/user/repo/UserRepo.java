package com.xzgedu.supercv.user.repo;

import com.xzgedu.supercv.user.domain.User;
import com.xzgedu.supercv.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class UserRepo {

    @Autowired
    private UserMapper userMapper;

    public boolean addUser(User user) {
        return userMapper.insertUser(user) == 1;
    }

    public User getUserById(long uid) {
        return userMapper.selectUserById(uid);
    }

    public User getUserByOpenId(String openId) {
        return userMapper.selectUserByOpenId(openId);
    }

    public User getUserByUnionId(String unionId) {
        return userMapper.selectUserByUnionId(unionId);
    }

    public User getUserByTelephone(String telephone) {
        return userMapper.selectUserByTelephone(telephone);
    }

    public List<User> getUsersByNickName(String nickName, int limitOffset, int limitSize) {
        return userMapper.selectUsersByNickName(nickName, limitOffset, limitSize);
    }

    public int countUsersByNickName(String nickName) {
        return userMapper.countUsersByNickName(nickName);
    }

    public List<User> listUsers(int limitOffset, int limitSize) {
        return userMapper.listUsers(limitOffset, limitSize);
    }

    public int countUsersByDuration(Date startTime, Date endTime) {
        return userMapper.countUsersByDuration(startTime, endTime);
    }

    public List<User> getUsersByUids(List<Long> uids) {
        return userMapper.selectUsersByUids(uids);
    }

    public boolean updateTelephone(long uid, String telephone) {
        return userMapper.updateTelephone(uid, telephone) == 1;
    }

    public boolean updateNickName(long uid, String nickName) {
        return userMapper.updateNickName(uid, nickName) == 1;
    }

    public boolean updateHeadImgUrl(long uid, String headImgUrl) {
        return userMapper.updateHeadImgUrl(uid, headImgUrl) == 1;
    }
}
