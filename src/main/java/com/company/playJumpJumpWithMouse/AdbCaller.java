package com.company.playJumpJumpWithMouse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdbCaller {

    private static String adbPath = Constants.ADB_PATH;

    private static String screenshotLocation = Constants.SCREENSHOT_LOCATION;

    private static Boolean error = null;

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
    public static void longPress(double timeMilli, BufferedImage image) throws IOException, InterruptedException{
        try {
            int x = image.getWidth() / 3 + (int) (Math.random() * image.getWidth() / 3);
            int y = image.getHeight() - 300 + (int) (Math.random() * 200);
            int x2 = (int) (x + ((Math.random() - 0.5) * 20));//左右10个像素随机
            int y2 = (int) (y + ((Math.random() - 0.5) * 20));//上下10个像素随机
            Process process = Runtime.getRuntime()
                    .exec(adbPath + " shell input touchscreen swipe " + x + " " + y + " " + x2 + " " + y2 + " " + (int) timeMilli);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = bufferedReader.readLine()) != null)
                System.out.println(s);
            process.waitFor();
        } catch (IOException io) {
            String msg = "IOException occurred: " + io.getLocalizedMessage();
                throw new IOException(msg, io);
        } catch (InterruptedException ie) {
            String msg = "InterruptedException occurred: " + ie.getLocalizedMessage();
                throw new InterruptedException(msg, ie);
        }
    }

    /**
     * 改进的截图方法<br>
     * 感谢 hxzqlh
     * 当改进的截图方法不能正常执行时降级为常规方法
     */
    public static void printScreen() throws IOException, InterruptedException{
        if (error != null && error) {
            printScreenWithOld();
        } else {
            try {
                String[] args = new String[]{"bash", "-c", adbPath + " exec-out screencap -p > " + screenshotLocation};
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
                checkScreenSuccess();
            } catch (IOException io) {
            String msg = "IOException occurred: " + io.getLocalizedMessage();
                throw new IOException(msg, io);
                error = true;
                printScreenWithOld();
            } catch (InterruptedException ie) {
            String msg = "InterruptedException occurred: " + ie.getLocalizedMessage();
                throw new InterruptedException(msg, ie);
            }
        }
    }

    private static void checkScreenSuccess() throws IOException {
        if (error == null) {
            BufferedImage image = ImageIO.read(new File(screenshotLocation));
            if (image == null) {
                throw new IOException("Can't read file \"" + screenshotLocation + "\" into image object.");
            }
        }
    }

    public static void printScreenWithOld() throws IOException, InterruptedException{
        try {
            Process p1 = Runtime.getRuntime().exec(adbPath + " shell screencap -p /sdcard/screenshot.png");
            p1.waitFor();
            Process p2 = Runtime.getRuntime().exec(adbPath + " pull /sdcard/screenshot.png " + screenshotLocation);
            p2.waitFor();
        } catch (IOException io) {
            String msg = "IOException occurred: " + io.getLocalizedMessage();
                throw new IOException(msg, io);
        } catch (InterruptedException ie) {
            String msg = "InterruptedException occurred: " + ie.getLocalizedMessage();
                throw new InterruptedException(msg, ie);
        }
    }

    public static int getSize() {
        try {
            Process p = Runtime.getRuntime().exec(adbPath + " shell wm density");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = bufferedReader.readLine();
            String[] splis = line.split(" ");
            return Integer.valueOf(splis[splis.length - 1]);
        } catch (Exception e) {
            String msg = "Exception occured while getting size: " + e.getLocalizedMessage();
            throw new Exception(msg, e)
        }
        return 400;
    }

    public static void main(String[] args) {
        System.out.println(getSize());
    }
}
