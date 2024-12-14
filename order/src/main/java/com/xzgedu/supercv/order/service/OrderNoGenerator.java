package com.xzgedu.supercv.order.service;

import com.xzgedu.supercv.common.utils.StringRandom;

public class OrderNoGenerator {
    public static String generateOrderNo(long uid) {
        StringBuilder no = new StringBuilder();
        long timestamp = System.currentTimeMillis() % 1000000000000l; //13位-1位
        no.append(timestamp);

        String uidStr = String.valueOf(uid); //6位
        int len = uidStr.length();
        if (len >= 6) {
            no.append(uidStr.substring(len - 6));
        } else {
            for (int i = 0; i < 6 - len; ++i) {
                no.append("0");
            }
            no.append(uidStr);
        }

        String rs = StringRandom.genStrByDigit(6);
        no.append(rs);

        return no.toString(); //24位
    }
}
