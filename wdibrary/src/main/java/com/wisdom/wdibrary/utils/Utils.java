package com.wisdom.wdibrary.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.File;
import java.util.Scanner;

/**
 * 
 * @author hailongqiu 356752238@qq.com
 *
 */
public class Utils {
	
	/**
	 * 获取SDK版本
	 */
	public static int getSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
		}
		return version;
	}

	/**
	 * 是否电视机HDMI
	 * @param
	 * @return
	 */
	public static boolean isHdmiSwitchSet() {
		// The file '/sys/devices/virtual/switch/hdmi/state' holds an int -- if it's 1 then an HDMI device is connected.
		// An alternative file to check is '/sys/class/switch/hdmi/state' which exists instead on certain devices.
		File switchFile = new File("/sys/devices/virtual/switch/hdmi/state");
		if (!switchFile.exists()) {
			switchFile = new File("/sys/class/switch/hdmi/state");
		}
		try {
			Scanner switchFileScanner = new Scanner(switchFile);
			int switchValue = switchFileScanner.nextInt();
			switchFileScanner.close();
			return switchValue > 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否有电话功能
	 * @param context
	 * @return
	 */
	public static boolean isTabletDevice(Context context) {
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		assert telephony != null;
		int type = telephony.getPhoneType();
		if (type == TelephonyManager.PHONE_TYPE_NONE) {
			//LogDog.i("is Tablet!");
			return false;
		} else {
			//LogDog.i("is phone!");
			return true;
		}
	}

}
