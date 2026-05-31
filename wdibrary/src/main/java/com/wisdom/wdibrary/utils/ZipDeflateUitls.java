package com.wisdom.wdibrary.utils;

import android.text.TextUtils;

import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Apache
 * @description:
 * @date :2020/3/20 10:24
 */
public class ZipDeflateUitls {

    /**
     * Deflate压缩
     * @param inputString
     * @return
     */
    public static byte[] compressData(String inputString) {
        try {
            byte[] input = inputString.getBytes("UTF-8");
            //LogDog.i("原始长度："+input.length);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Deflater deflater = new Deflater();
            DeflaterOutputStream compress = new DeflaterOutputStream(out, deflater, 1024);
            compress.write(input);
            compress.finish();
            compress.flush();
            //byte[] encode = out.toByteArray();
            //LogDog.i(",压缩长度："+encode.length);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * Deflater 解压
     * @param bytes
     * @return
     */
    public static String decompress(byte[] bytes){
        try {
        byte[] readBuffer = new byte[5000];
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        InflaterInputStream inputStream = new InflaterInputStream(arrayInputStream);
        int read = inputStream.read(readBuffer);

        //Should hold the original (reconstructed) data
        byte[] result = Arrays.copyOf(readBuffer, read);

        // Decode the bytes into a String

            return new String(result, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Zip压缩数据
     */
    public String compressForZip(String unZipStr) {

        if (TextUtils.isEmpty(unZipStr)) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(baos);
            zip.putNextEntry(new ZipEntry("0"));
            zip.write(unZipStr.getBytes());
            zip.closeEntry();
            zip.close();
            byte[] encode = baos.toByteArray();
            baos.flush();
            baos.close();
            LogDog.i("Zip压缩：" + Base64.toBase64String(encode));
            return Base64.toBase64String(Base64.encode(encode));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Zip解压数据
     *
     * @param zipStr
     * @return
     */
    public String decompressForZip(String zipStr) {

        if (TextUtils.isEmpty(zipStr)) {
            return null;
        }
        byte[] t = Base64.decode(zipStr);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(t);
            ZipInputStream zip = new ZipInputStream(in);
            zip.getNextEntry();
            byte[] buffer = new byte[256];
            int n = 0;
            while ((n = zip.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, n);
            }
            zip.close();
            in.close();
            out.close();
            LogDog.i("Zip解缩：" + out.toString("UTF-8"));
            return out.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
