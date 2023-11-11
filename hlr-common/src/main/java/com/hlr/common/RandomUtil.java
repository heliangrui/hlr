package com.hlr.common;

import java.util.Random;
import java.util.UUID;

/**
 * RandomUtil
 * Description:
 * date: 2023/11/11 10:34
 *
 * @author hlr
 */
public class RandomUtil {


    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static final String getUuid() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }

    public static final String getShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        String uuid1 = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid1.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(chars.length);
            sb.append(chars[number]);
        }

        return sb.toString();
    }

    public static String getRandomNum(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }

    public static int getRandomRange(int range) {
        Random random = new Random();
        return random.nextInt(range);
    }

    public static Object getRandomObj(Object[] strs) {
        Random random = new Random();
        return strs[random.nextInt(strs.length)];
    }

}
