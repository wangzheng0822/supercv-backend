package com.xzgedu.supercv.common.utils;

import com.xzgedu.supercv.common.exception.DataInvalidException;
import com.xzgedu.supercv.common.exception.ErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 数据校验类
 *
 * @author wangzheng
 */
public class Assert {
    public static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new DataInvalidException(ErrorCode.DATA_SHOULD_NOT_EMPTY);
        }
    }

    public static void checkNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new DataInvalidException(ErrorCode.DATA_SHOULD_NOT_EMPTY, msg);
        }
    }

    public static void checkTelephone(String telephone) {
        String regex = "^[1]([3-9])[0-9]{9}$";
        if (StringUtils.isBlank(telephone) || telephone.length() != 11 ||
                Pattern.matches(regex, telephone) == false) {
            throw new DataInvalidException(ErrorCode.TELEPHONE_INVALID,
                    "Check telephone failed: [telephone=" + telephone + "]");
        }
    }

    public static void checkSmsCode(String smsCode) {
        if (StringUtils.isBlank(smsCode) || smsCode.length() != 6) {
            throw new DataInvalidException(ErrorCode.SMS_CODE_INVALID,
                    "Check sms code failed: [smscode=" + smsCode + "]");
        }
    }

    public static void checkNickName(String nickName) {
        if (StringUtils.isBlank(nickName) || nickName.length() > 20) {
            throw new DataInvalidException(ErrorCode.NICKNAME_INVALID,
                    "Check nick name failed: [nickname=" + nickName + "]");
        }
    }
}

