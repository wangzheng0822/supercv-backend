package com.xzgedu.supercv.user.service;

import com.xzgedu.supercv.common.utils.StringRandom;
import com.xzgedu.supercv.user.domain.AuthToken;
import com.xzgedu.supercv.user.repo.AuthTokenRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * 登陆认证Token，Token有效期为7天，在失效前12小时进行Token刷新续期，续期为1天
 * @author wangzheng
 */
@Slf4j
@Service
public class AuthService {
    private static final long DEFAULT_TOKEN_EXPIRE_TIME = 7 * 24 * 3600 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1 * 24 * 3600 * 1000L;
    private static long TIME_AHEAD_TO_REFRESH_TOKEN = 12 * 3600 * 1000L;

    @Autowired
    private AuthTokenRepo authTokenRepo;

    public boolean auth(long uid, String token) {
        AuthToken authToken = authTokenRepo.getAuthToken(token);
        if (authToken == null || authToken.getUid() != uid) return false;
        // 在快要过期（提前12小时）就给token续期（7天）
        if (authToken.getExpireTime().before(new Date(System.currentTimeMillis() + TIME_AHEAD_TO_REFRESH_TOKEN))) {
            refreshToken(token);
        }
        return true;
    }

    public AuthToken createToken(long uid) {
        long expireTime = System.currentTimeMillis() + DEFAULT_TOKEN_EXPIRE_TIME;
        return createToken(uid, new Date(expireTime));
    }

    public AuthToken createToken(long uid, Date expireTime) {
        AuthToken authToken = new AuthToken();
        authToken.setToken(generateToken());
        authToken.setUid(uid);
        authToken.setExpireTime(expireTime);
        authTokenRepo.addAuthToken(authToken);
        return authToken;
    }

    public boolean refreshToken(String token) {
        long timestamp = System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME;
        return authTokenRepo.updateTokenExpireTime(token, new Date(timestamp));
    }

    public void deleteToken(String token) {
        authTokenRepo.deleteToken(token);
    }

    private String generateToken() {
        String rawData = StringRandom.genStrByDigitAndLetter(32) + System.currentTimeMillis();
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No MD5 algorithm", e);
        }

        byte[] bytes = md5.digest(rawData.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            builder.append(Integer.toHexString((0x000000FF & aByte) | 0xFFFFFF00).substring(6));
        }
        return builder.toString();
    }
}
