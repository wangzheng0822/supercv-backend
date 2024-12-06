package com.xzgedu.supercv.user.repo;

import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.user.mapper.AuthTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class AuthTokenRepo {

    @Autowired
    private AuthTokenMapper authTokenMapper;

    public boolean addAuthToken(AuthToken authToken) {
        return authTokenMapper.insertAuthToken(authToken) == 1;
    }

    public AuthToken getAuthToken(String token) {
        return authTokenMapper.selectAuthToken(token);
    }

    public boolean deleteToken(String token) {
        return authTokenMapper.deleteToken(token) == 1;
    }

    public boolean updateTokenExpireTime(String token, Date expireTime) {
        return authTokenMapper.updateTokenExpireTime(token, expireTime) == 1;
    }
}
