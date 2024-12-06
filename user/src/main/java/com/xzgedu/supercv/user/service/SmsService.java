package com.xzgedu.supercv.user.service;

import com.xzgedu.supercv.common.exception.*;
import com.xzgedu.supercv.common.utils.StringRandom;
import com.xzgedu.supercv.user.domain.SmsCode;
import com.xzgedu.supercv.user.repo.SmsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class SmsService {
    private static final int SMS_CODE_VALID_PERIOD = 300; //5min
    private static final int MIN_SEND_TIME_INTERVAL = 60; //60s

    @Autowired
    private SmsClient smsClient;

    @Autowired
    private SmsRepo smsRepo;

    @Autowired
    private Captcha captcha;

    public void sendSmsCode(String telephone, String ticket, String randStr, String userIp)
            throws SendSmsCodeFailedException, RequestTooFrequentException, NoPermissionException {
        log.info("" + captcha.verifyTicket(ticket, randStr, userIp));
        if (!captcha.verifyTicket(ticket, randStr, userIp)) {
            log.info("奇了怪了");
            throw new NoPermissionException("Failed to verify captcha: [ticket=" + ticket +
                    ", randStr=" + randStr + ", userIp=" + userIp);
        }

        SmsCode smsCodeInDb = smsRepo.getSmsCodeByTelephone(telephone);
        if (smsCodeInDb != null &&
                smsCodeInDb.getSendTime().getTime() + MIN_SEND_TIME_INTERVAL * 1000 > System.currentTimeMillis()) {
            throw new RequestTooFrequentException("Too frequent to send sms code: [telephone=" + telephone + "]");
        }

        String code = StringRandom.genStrByDigit(6);
        SmsCode smsCodeObj = new SmsCode();
        smsCodeObj.setCode(code);
        smsCodeObj.setTelephone(telephone);
        smsCodeObj.setSendTime(new Date());
        smsCodeObj.setUsed(false);
        if (smsCodeInDb == null) {
            smsRepo.saveSmsCode(smsCodeObj);
        } else {
            smsRepo.updateSmsCode(smsCodeObj);
        }
        smsClient.sendSmsCode(telephone, code);
    }

    public void verifySmsCode(String telephone, String code)
            throws SmsCodeUnmatchedException, SmsCodeExpiredException {
        SmsCode smsCodeInDb = smsRepo.getSmsCodeByTelephone(telephone);
        if (smsCodeInDb == null || smsCodeInDb.isUsed() || !smsCodeInDb.getCode().equals(code)) {
            throw new SmsCodeUnmatchedException("Sms code verify failed: [telephone="
                    + telephone + ", code=" + code + "]");
        }

        long diff = (System.currentTimeMillis() - smsCodeInDb.getSendTime().getTime()) / 1000;
        if (diff > SMS_CODE_VALID_PERIOD) {
            throw new SmsCodeExpiredException("Sms code expired: [telephone="
                    + telephone + ", code=" + code + "]");
        }
    }
}