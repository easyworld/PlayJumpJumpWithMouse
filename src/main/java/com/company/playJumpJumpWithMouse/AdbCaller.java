package com.company.playJumpJumpWithMouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
			Process process = Runtime.getRuntime()
					.exec(adbPath + " shell input touchscreen swipe 170 187 170 187 " + (int) timeMilli);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String s;
			while ((s = bufferedReader.readLine()) != null)
				System.out.println(s);
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
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
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
			String s;
			while ((s = bufferedReader.readLine()) != null)
				System.out.println(s);
			p1.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
