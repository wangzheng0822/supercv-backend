package com.xzgedu.supercv.common.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StringRandomTest {

    private static final int DEFAULT_TEST_LENGTH = 10;

    @Test
    void genStrByDigit_GeneratesCorrectLength_DigitsOnly() {
        String result = StringRandom.genStrByDigit(DEFAULT_TEST_LENGTH);
        assertNotNull(result); // 断言结果不为空
        assertEquals(DEFAULT_TEST_LENGTH, result.length()); // 断言结果长度正确
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c)); // 断言结果中的每个字符都是数字
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100})
    void genStrByDigit_VariousLengths(int length) {
        String result = StringRandom.genStrByDigit(length);
        assertNotNull(result); // 断言结果不为空
        assertEquals(length, result.length()); // 断言结果长度正确
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c)); // 断言结果中的每个字符都是数字
        }
    }

    @Test
    void genStrByDigit_NegativeLength_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> StringRandom.genStrByDigit(-1));
    }

    @Test
    void genStrByDigitAndLetter_GeneratesCorrectLength_MixedCharacters() {
        String result = StringRandom.genStrByDigitAndLetter(DEFAULT_TEST_LENGTH);
        assertNotNull(result); // 断言结果不为空
        assertEquals(DEFAULT_TEST_LENGTH, result.length()); // 断言结果长度正确
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c) || Character.isLetter(c)); // 断言结果中的每个字符是数字或字母
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100})
    void genStrByDigitAndLetter_VariousLengths(int length) {
        String result = StringRandom.genStrByDigitAndLetter(length);
        assertNotNull(result); // 断言结果不为空
        assertEquals(length, result.length()); // 断言结果长度正确
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c) || Character.isLetter(c)); // 断言结果中的每个字符是数字或字母
        }
    }

    @Test
    void genStrByDigitAndLetter_NegativeLength_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> StringRandom.genStrByDigitAndLetter(-1));
    }
}
