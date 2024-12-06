package com.xzgedu.supercv.user.repo;

import com.xzgedu.supercv.user.domain.SmsCode;
import com.xzgedu.supercv.user.mapper.SmsCodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class SmsRepo {

    @Autowired
    private SmsCodeMapper smsCodeMapper;

    public void saveSmsCode(SmsCode smsCode) {
        smsCodeMapper.insertSmsCode(smsCode);
    }

    public SmsCode getSmsCodeByTelephone(String telephone) {
        return smsCodeMapper.selectSmsCodeByTelephone(telephone);
    }

    public boolean updateSmsCode(SmsCode smsCode) {
        return smsCodeMapper.updateSmsCode(smsCode) == 1;
    }

    public boolean updateToBeUsed(String telephone) {
        return smsCodeMapper.updateToBeUsed(telephone) == 1;
    }
}
