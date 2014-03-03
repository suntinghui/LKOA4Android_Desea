package com.lkoa.util;

import android.util.Log;

public class LogUtil {
	static int LOG_LEVEL = Log.INFO;

	public static void d(String tag, String msg) {
		if (Log.DEBUG >= LOG_LEVEL) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (Log.INFO >= LOG_LEVEL) {
			Log.i(tag, msg);
			
		}
	}

	public static void e(String tag, String msg) {
		if (Log.ERROR >= LOG_LEVEL) {
			Log.e(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (Log.VERBOSE >= LOG_LEVEL) {
			Log.v(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (Log.WARN >= LOG_LEVEL) {
			Log.w(tag, msg);
		}
	}
}
