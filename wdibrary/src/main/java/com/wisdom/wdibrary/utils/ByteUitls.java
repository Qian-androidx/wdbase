package com.wisdom.wdibrary.utils;

/**
 * @author Apache
 * @description:
 * @date :2020/3/20 12:05
 */
public class ByteUitls {

    /**
     * 转二进制
     *
     * @param b
     * @return String
     */
    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString();
    }

    /**
     * 十六进制转二进制
     * @param hex
     * @return
     */
    public static byte[] hex2byte(String hex) {
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length / 2; i++) {
            String temp = "" + hex.charAt(2 * i) + hex.charAt(2 * i + 1);
            bytes[i] = (byte) Integer.parseInt(temp, 16);
        }
        return bytes;
    }
}
