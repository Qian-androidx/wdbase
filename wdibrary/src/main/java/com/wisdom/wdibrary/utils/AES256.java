package com.wisdom.wdibrary.utils;

import org.bouncycastle.util.encoders.Base64;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.zip.CRC32;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by apache on 2017/5/22.
 */

public class AES256 {

    //    private String key = "KEYXEJB7M1ZZWKM2";
    //
    //    private String message = "CUCTXXZBKYDRKEX4";

    private static final String CIPHER_DESC = "AES/CFB/NoPadding";

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String CHARSET = "UTF-8";
    private static final String ENCODE = "UTF-8";

    private static final String HMAC_SHA256 = "HmacSHA256";

    private static final String SHA_256 = "SHA-256";

    private static final String AES = "AES";


    /**
     * 字符串排序
     *
     * @param source
     * @return
     */
    public static String mySort(String source) {
        char[] c = source.toCharArray();
        Arrays.sort(c);
        return new String(c, 0, c.length);
    }


    /**
     * 随机字符串
     *
     * @return int
     */
    public static String getRandomNumber(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        //LogDog.i("随机字符串:"+sb.toString());
        return sb.toString();
    }

    /**
     * sha256加密
     *
     * @param data
     * @return
     */
    public static String sha256(String data) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = data.getBytes();
        try {
            md = MessageDigest.getInstance(SHA_256);
            md.update(bt);
            strDes = ByteUitls.byte2hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }


    /**
     * HMAC_Sha256加密
     *
     * @param bytes
     * @param key
     * @return
     */
    public static String HMAC_Sha256(byte[] bytes, String key) {
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
            sha256_HMAC.init(secret_key);
            //LogDog.i("HMAC_sha256:" + hash);
            return ByteUitls.byte2hex(sha256_HMAC.doFinal(bytes));
        } catch (Exception e) {
            System.out.println("Error");
            return null;
        }
    }

    public static byte[] HMAC_Sha256byte(byte[] bytes, String key) {
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
            sha256_HMAC.init(secret_key);
            //LogDog.i("HMAC_sha256:" + hash);
            return sha256_HMAC.doFinal(bytes);
        } catch (Exception e) {
            System.out.println("Error");
            return null;
        }
    }

    public static String crc32(String data) {
        if (data == null) {
            return null;
        }
        CRC32 crc32 = new CRC32();
        crc32.update(data.getBytes());
        return crc32.getValue() + "";
    }

    /**
     * AES256加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String aesEncrypt(String data, String key) {
        try {

            String pwd = new MD5().getMD5(key);
            LogDog.i("pwd:" + pwd);
            String spec = new MD5().getMD5(pwd).substring(0, 16);
            SecretKeySpec skeySpec = new SecretKeySpec(pwd.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(CIPHER_DESC);
            IvParameterSpec iv = new IvParameterSpec(spec.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] bytes = cipher.doFinal(data.getBytes("UTF-8"));
            return new String(Base64.encode(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * AES256解密
     *
     * @param data
     * @param key
     * @return
     */
    public static String aesDecrypt(String data, String key) {

        try {
            String pwd = new MD5().getMD5(key);
            //LogDog.i("pwd:" + pwd);
            String spec = new MD5().getMD5(pwd).substring(0, 16);
//            LogDog.i("IV:" + spec);
            byte[] bytes = Base64.decode(data.getBytes());
            Cipher cipher = Cipher.getInstance(CIPHER_DESC);
            SecretKeySpec secretKey = new SecretKeySpec(pwd.getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(spec.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            bytes = cipher.doFinal(bytes);
            //String val = new String(bytes);

            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            LogDog.i(e);
            return null;
        }
    }

    /**
     * *********************************************************
     */
    /**
     * AES加密
     * @param data 明文
     * @param key 密钥（16/24/32字节对应128/192/256位）
     * @param iv 初始化向量（16字节）
     */
    public static String encrypt(String data, String key, String iv) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(CHARSET));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes(CHARSET));
        return Base64.toBase64String(encrypted);
    }

    /**
     * AES解密
     * @param encryptedData Base64编码的密文
     * @param key 密钥（需与加密时一致）
     * @param iv 初始化向量（需与加密时一致）
     */
    public static String decrypt(String encryptedData, String key, String iv) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(CHARSET));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decoded = Base64.decode(encryptedData);
        byte[] original = cipher.doFinal(decoded);
        return new String(original, CHARSET);
    }

    public static final int AES_KEY_SIZE = 128;
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;

    public static String aesGcm128Encrypt(String content,String keystr) {

        try {

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(keystr));
            byte[] iv = cipher.getIV();
            assert iv.length == 12;
            byte[] encryptData = cipher.doFinal(content.getBytes());
            assert encryptData.length == content.getBytes().length + 16;
            byte[] message = new byte[12 + content.getBytes().length + 16];
            System.arraycopy(iv, 0, message, 0, 12);
            System.arraycopy(encryptData, 0, message, 12, encryptData.length);

            LogDog.i("-----" + Base64.toBase64String(message));

            return new String(message);
        } catch (Exception e) {
            LogDog.e(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String aesGcm128Decrypt(String content,String keystr) {
        LogDog.i("解密前="+content);
        LogDog.i("密前="+keystr);
        try {
            byte[] message = content.getBytes(StandardCharsets.UTF_8);
            if (message.length < 12 + 16)
                throw new IllegalArgumentException();

            GCMParameterSpec params = new GCMParameterSpec(128, message, 0, 12);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(keystr), params);
            byte[] decryptData = cipher.doFinal(message, 12, message.length - 12);
            LogDog.d("解密后：" + new String(decryptData));
            return new String(decryptData);
        } catch (Exception e) {
            LogDog.e(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getIv(){
        byte[] IV = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);
        return IV;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static SecretKeySpec getSecretKey(String encryptPass) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(AES);
        // 初始化密钥生成器，AES要求密钥长度为128位、192位、256位
        kg.init(128, new SecureRandom(encryptPass.getBytes(StandardCharsets.UTF_8)));
        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), AES);// 转换为AES专用密钥
    }





    /**
     * URL 解码
     *
     * @param str
     * @return String
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return null;
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * URL 转码
     *
     * @return String
     * @author
     * @date
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String findSign(String url, String nonce, String date) {
        String urlencoder = getURLEncoderString(url);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(AES256.mySort(urlencoder.toUpperCase()))
                .append(nonce)
                .append(date);

//        String und = AES256.mySort(urlencoder.toUpperCase()) + nonce + date;
//        LogDog.i("und：" + und);
        // LogDog.i("stringBuffer：" + urlencoder.toUpperCase());
        return sha256(stringBuffer.toString());
    }

    /**
     * 文件hash
     *
     * @return
     */
    public static String getFileHash(String path) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            byte[] buffer = new byte[1024];
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            int numRead = 0;
            while (numRead != -1) {
                numRead = inputStream.read(buffer);
                if (numRead > 0)
                    digest.update(buffer, 0, numRead);
            }
            byte[] sha1Bytes = digest.digest();
            return ByteUitls.byte2hex(sha1Bytes);
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }
        }
    }



}
