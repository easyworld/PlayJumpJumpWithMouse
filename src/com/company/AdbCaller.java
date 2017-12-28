package com.company;

import java.io.IOException;

public class AdbCaller {

    public static void call(double timeMilli) {
        try {
            Runtime.getRuntime().exec(Constants.ADB_PATH + " shell input touchscreen swipe 170 187 170 187 " + (int) timeMilli);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printScreen() {
        try {
            Process p1 = Runtime.getRuntime().exec(Constants.ADB_PATH + " shell screencap -p /sdcard/screenshot.png");
            p1.waitFor();
            Process p2 = Runtime.getRuntime().exec(Constants.ADB_PATH + " pull /sdcard/screenshot.png " + Constants.SCREENSHOT_LOCATION);
            p2.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
