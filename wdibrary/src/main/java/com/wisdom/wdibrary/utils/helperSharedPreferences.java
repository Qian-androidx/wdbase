package com.wisdom.wdibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class helperSharedPreferences {

	/* JSON(String) Object */
	public static void setJsonSP(String key, JSONObject value, Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, String.valueOf(value));
		editor.commit();
	}

	public static JSONObject getJsonSP(String key, Context context) {
		JSONObject _rtn = new JSONObject();
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String JSONStr = preferences.getString(key, null) == null ? ""
				: preferences.getString(key, null);
		if (JSONStr.length() > 0) {
			try {
				_rtn = new JSONObject(JSONStr);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		} else return null;
		
		return _rtn;
	}

	public static JSONObject getJsonBooleanSP(String key, Boolean clearBool,
			Context context) {
		JSONObject _rtn = new JSONObject();
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String JSONStr = preferences.getString(key, null) == null ? ""
				: preferences.getString(key, null);
		if (clearBool) {
			clearSharedPreferences(key, context);
		}
		try {
			_rtn = new JSONObject(JSONStr);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return _rtn;
	}

	/* String */
	public static void setStringSP(String key, String value, Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getStringSP(String key, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(key, null) == null ? "" : preferences
				.getString(key, null);
	}

	public static String getStringBooleanSP(String key, Boolean clearBool,
			Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		// Check what return if casting null to integer
		String rtn = preferences.getString(key, null) == "" ? null
				: preferences.getString(key, null);
		if (clearBool) {
			clearSharedPreferences(key, context);
		}
		return rtn;
	}

	/* Integer */
	public static void setIntegerSP(String key, Integer value, Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getIntegerSP(String key, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		// Check what return if casting null to integer
		return preferences.getInt(key, 0);
	}

	public static int getIntegerBooleanSP(String key, Boolean clearBool, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		// Check what return if casting null to integer
		Integer rtn = preferences.getInt(key, (Integer) null);
		if (clearBool) {
			clearSharedPreferences(key, context);
		}
		return rtn;
	}

	/* Long */
	public static void setLongSP(String key, Long value, Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static Long getLongSP(String key, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		// Check what return if casting null to integer
		return preferences.getLong(key, (int) 0);
	}

	public static Long getLongBooleanSP(String key, Boolean clearBool,
			Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		// Check what return if casting null to integer
		Long rtn = preferences.getLong(key, (int) 0);
		if (clearBool) {
			clearSharedPreferences(key, context);
		}
		return rtn;
	}

	/* Boolean */
	public static void setBooleanSP(String key, Boolean value, Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getBooleanSP(String key, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(key, (Boolean) false);
	}

	public static boolean getBooleanSPtrue(String key, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(key, (Boolean) true);
	}

	public static boolean getBooleanSP(String key, Boolean clearBool,
			Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Boolean rtn = preferences.getBoolean(key, (Boolean) false);
		if (clearBool) {
			clearSharedPreferences(key, context);
		}
		return rtn;
	}

	public static void clearSharedPreferences(String key, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}

//	保存所以以播放过的Num
	public static void saveNumRecord(String name,Object obj, Context context) {

		try {
			SharedPreferences preferences;
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				preferences = context.getSharedPreferences("data",
						Context.MODE_PRIVATE);
			}else {
                preferences = context.getSharedPreferences("data",
                        Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
			}


			SharedPreferences.Editor editor = preferences.edit();



			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);

			oos.writeObject(obj);

			String enStr = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT));
			oos.close();
			baos.close();
			editor.remove(name);
			editor.putString(name, enStr);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static Object getNumRecord(String name, Context context) {

        SharedPreferences preferences;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            preferences = context.getSharedPreferences("data",
                    Context.MODE_PRIVATE);
        }else {
            preferences = context.getSharedPreferences("data",
                    Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
        }

		Object obj = null;

		String convertStr = preferences.getString(name, null);

		if (null == convertStr) {
			return obj;
		}

		try {

			byte[] enStrByte = Base64.decode(convertStr, Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(enStrByte);
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj = ois.readObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return obj;
	}

}