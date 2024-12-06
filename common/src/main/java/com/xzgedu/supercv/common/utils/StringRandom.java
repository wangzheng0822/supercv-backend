package com.xzgedu.supercv.common.utils;

import java.util.Random;

/**
 * 随机字符串生成类
 *
 * @author wangzheng
 */
public class StringRandom {
    private static final Random r = new Random(System.currentTimeMillis());

    public static String genStrByDigit(int length) {
        if (length < 0) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            sb.append(r.nextInt(0, 10));
        }
        return sb.toString();
    }

    public static String genStrByDigitAndLetter(int length) {
        if (length < 0) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            int type = r.nextInt(0, 3);
            if (type == 0) {
                sb.append(r.nextInt(0, 10));
            } else if (type == 1) {
                sb.append((char)('a'+r.nextInt(0, 26)));
            } else {
                sb.append((char)('A' + r.nextInt(0, 26)));
            }
        }
        return sb.toString();
    }
}
