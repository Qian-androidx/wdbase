package com.wisdom.wdibrary.utils;



import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Video3des {

	private static final String DES = "DESede";

	private static final String PADDING = "0";

	private static final String CIPHER_DESC = "DESede/ECB/PKCS5Padding";

	private static final String TAG = "NetBossUtils";

	
	public Video3des() {


	}
/*
	public static void main(String[] args) throws Exception {
		String source = "99033CA298DCA130F9DEA61AD2106DB28278829A2A96271C1A9E082F43E4F5A236E1456C87B8599A";
		String key = "shanghaiiptv";
		String data = NetBossUtils.encrypt(source, key);
		data = "99033CA298DCA130CC510E5B5B267CFD8445F3DC367D066ED0432B11797533A536E1456C87B8599A";
		System.out.println(data);
		String message = NetBossUtils.decrypt(data, key);
		System.out.println(message);
	}
*/

	public static String decrypt(String message, String key) {
//		LogDog.d("decrypt message = " + message);
//		LogDog.d("decrypt key = " + key);
		try {
			byte[] bytes = ByteUitls.hex2byte(message);
			Cipher cipher = Cipher.getInstance(CIPHER_DESC);
			SecretKey secretKey = genSecretKey(key);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			bytes = cipher.doFinal(bytes);
			String val = new String(bytes);

			return val;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public  String encryptTest(String message, String key) {
		try {

			byte[] raw = key.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
			IvParameterSpec iv = new IvParameterSpec("fbe790be84e32ba3".getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] bytes = cipher.doFinal(message.getBytes());
			return ByteUitls.byte2hex(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encrypt(String message, String key) {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_DESC);
			SecretKey secretKey = genSecretKey(key);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] bytes = cipher.doFinal(message.getBytes());
			return ByteUitls.byte2hex(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static SecretKey genSecretKey(String key) throws Exception {

		key = StringTools.rightPad(key, 24, PADDING);
		DESedeKeySpec desKeySpec = new DESedeKeySpec(key.getBytes());
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
		return secretKey;
	}


	

}
