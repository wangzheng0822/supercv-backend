package com.xzgedu.supercv.common.utils;

import com.xzgedu.supercv.common.exception.DataInvalidException;
import com.xzgedu.supercv.common.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertTest {

    @Test
    public void checkNotNull_ObjectNotNull_NoExceptionThrown() {
        assertDoesNotThrow(() -> {
            Assert.checkNotNull("Not null object");
        }, "Exception should not be thrown for non-null object");
    }

    @Test
    public void checkNotNull_ObjectNull_ExceptionThrown() {
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            Assert.checkNotNull(null);
        });

        assertEquals(ErrorCode.DATA_SHOULD_NOT_EMPTY, exception.ERROR_CODE);
    }

    @Test
    public void checkNotNull_ObjectNotNullWithMessage_NoExceptionThrown() {
        assertDoesNotThrow(() -> {
            Assert.checkNotNull("Not null object", "Object should not be null");
        }, "Exception should not be thrown for non-null object");
    }

    @Test
    public void checkNotNull_ObjectNullWithMessage_ExceptionThrown() {
        String expectedMessage = "Object should not be null";
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            Assert.checkNotNull(null, expectedMessage);
        });

        assertEquals(ErrorCode.DATA_SHOULD_NOT_EMPTY, exception.ERROR_CODE);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void checkTelephone_ValidTelephone_NoExceptionThrown() {
        assertDoesNotThrow(() -> {
            Assert.checkTelephone("13800000000");
        }, "Exception should not be thrown for valid telephone number");
    }

    @Test
    public void checkTelephone_InvalidTelephone_ExceptionThrown() {
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            Assert.checkTelephone("1234567890");
        });

        assertEquals(ErrorCode.TELEPHONE_INVALID, exception.ERROR_CODE);
        assertEquals("Check telephone failed: [telephone=1234567890]", exception.getMessage());
    }

    @Test
    public void checkSmsCode_ValidSmsCode_NoExceptionThrown() {
        assertDoesNotThrow(() -> {
            Assert.checkSmsCode("123456");
        }, "Exception should not be thrown for valid SMS code");
    }

    @Test
    public void checkSmsCode_InvalidSmsCode_ExceptionThrown() {
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            Assert.checkSmsCode("12345");
        });

        assertEquals(ErrorCode.SMS_CODE_INVALID, exception.ERROR_CODE);
        assertEquals("Check sms code failed: [smscode=12345]", exception.getMessage());
    }

    @Test
    public void checkNickName_ValidNickName_NoExceptionThrown() {
        assertDoesNotThrow(() -> {
            Assert.checkNickName("wangzheng");
        }, "Exception should not be thrown for valid nickname");
    }

    @Test
    public void checkNickName_InvalidNickName_ExceptionThrown() {
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            Assert.checkNickName("ThisIsAVeryLongNickNameThatExceedsTheLimit");
        });

        assertEquals(ErrorCode.NICKNAME_INVALID, exception.ERROR_CODE);
        assertEquals("Check nick name failed: [nickname=ThisIsAVeryLongNickNameThatExceedsTheLimit]",
                exception.getMessage());
    }
}
