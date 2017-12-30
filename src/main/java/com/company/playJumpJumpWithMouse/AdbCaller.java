package com.company.playJumpJumpWithMouse;

import java.io.IOException;

public class AdbCaller {

	private static String adbPath = Constants.ADB_PATH;

	private static String screenshotLocation = Constants.SCREENSHOT_LOCATION;

	public static void setAdbPath(String adbPath) {
		AdbCaller.adbPath = adbPath;
	}

	public static void setScreenshotLocation(String screenshotLocation) {
		AdbCaller.screenshotLocation = screenshotLocation;
	}

	/**
	 * 调用adb长按屏幕
	 * 
	 * @param timeMilli
	 */
	public static void longPress(double timeMilli) {
		try {
			Runtime.getRuntime().exec(adbPath + " shell input touchscreen swipe 170 187 170 187 " + (int) timeMilli);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 改进的截图方法<br>
	 * 感谢 hxzqlh
	 */
	public static void printScreen() {

		try {
			String[] args = new String[] { "bash", "-c", adbPath + " exec-out screencap -p > " + screenshotLocation };
			String os = System.getProperty("os.name");
			if (os.toLowerCase().startsWith("win")) {
				args[0] = "cmd";
				args[1] = "/c";
			}
			Process p1 = Runtime.getRuntime().exec(args);
			p1.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
