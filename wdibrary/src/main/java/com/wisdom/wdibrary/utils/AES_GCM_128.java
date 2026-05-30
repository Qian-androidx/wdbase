package com.wisdom.wdibrary.utils;


import android.os.Build;

import org.bouncycastle.util.encoders.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Apache
 * @description:
 * @date :2020/3/19 9:59
 */
public class AES_GCM_128 {
    public static final int AES_KEY_SIZE = 128;
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;

    private static final String AES = "AES";
    private static final String AES_GCM = "AES/GCM/NoPadding";


    /**
     * 生成加密秘钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getSecretKey(String encryptPass) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(AES);
        // 初始化密钥生成器，AES要求密钥长度为128位、192位、256位
        kg.init(AES_KEY_SIZE, new SecureRandom(encryptPass.getBytes()));
        // kg.init(AES_KEY_SIZE);
        SecretKey secretKey = kg.generateKey();
        String dse = Base64.toBase64String(secretKey.getEncoded());
        LogDog.i("shui密钥:" + dse);
//        byte[] en = Base64.decode(dse);
        //Create SecretKeySpec
        return secretKey.getEncoded();// 转换为AES专用密钥
    }


    /**
     * encrypt 加密
     *
     * @param plaintext
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] plaintext, byte[] key) throws Exception {
        byte[] IV = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance(AES_GCM);

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key, AES);

        // Create GCMParameterSpec
        //GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, getParams(IV));

        // Perform Encryption
        byte[] cipherText = cipher.doFinal(plaintext);

       // LogDog.i("IV:" + AES256.byte2hex(IV) + ",cipherText:" + AES256.byte2hex(cipherText));
        return byteMerger(IV, cipherText);
    }

    private static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt, 0, bt1.length);
        System.arraycopy(bt2, 0, bt, bt1.length, bt2.length);
        // System.arraycopy(bt3, 0, bt, bt1.length+bt2.length, bt3.length);
        return bt;
    }


    /**
     * decrypt 解密
     *
     * @param cipherText
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] cipherText, byte[] key) throws Exception {

//        if (cipherText.length < 12 + 16)
//            throw new IllegalArgumentException();
        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] message = new byte[cipherText.length - iv.length];
        System.arraycopy(cipherText, 0, iv, 0, iv.length);
        System.arraycopy(cipherText, iv.length, message, 0, message.length);
//        LogDog.i("解密+message.length:" + message.length + ",cipherText_length:" + (cipherText.length - 12));
//        LogDog.i("解密IV:" + AES256.byte2hex(iv) + ",cipherText:" + AES256.byte2hex(message));
        //byte[] message = cipherText.getBytes();
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance(AES_GCM);

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key, AES);
        // Create GCMParameterSpec
       // GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, getParams(iv));

        // Perform Decryption
        byte[] decryptedText = cipher.doFinal(message);

        //LogDog.i("decryptedText:" + new String(decryptedText));
        return new String(decryptedText);
    }

    private static AlgorithmParameterSpec getParams(final byte[] iv) {
        return getParams(iv, 0, iv.length);
    }

    private static AlgorithmParameterSpec getParams(final byte[] buf, int offset, int len) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // GCMParameterSpec should always be present in Java 7 or newer, but it's missing on
            // some Android devices with API level <= 19. Fortunately, we can initialize the cipher
            // with just an IvParameterSpec. It will use a tag size of 128 bits.
            return new IvParameterSpec(buf, offset, len);
        }
        return new GCMParameterSpec(GCM_TAG_LENGTH * 8, buf, offset, len);
    }
}
