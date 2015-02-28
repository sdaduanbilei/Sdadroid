package com.scorpio.frame.util;

public class LogUtil {
	private static final String InfoTag = "myinfo";
	private static final String DebugTag = "ezb";
    private static final String ErrorTag = "myerror";
	public static void Log(String log) {
		android.util.Log.i(InfoTag, log);
	}

	public static void Debug(String debug) {
		android.util.Log.d(DebugTag, debug);
	}

	public static void Error(String error) {
		if (error != null)
			android.util.Log.e(ErrorTag, error);
		else 
			android.util.Log.e(ErrorTag, "the error message is null");
	}
}
