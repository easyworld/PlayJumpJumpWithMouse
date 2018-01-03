package com.company.playJumpJumpWithMouse;

/**
 * Created by RoyZ on 2017/12/29.
 */
public class Constants {
    /**
     * adb所在位置
     */
    public static final String ADB_PATH = "C:\\Users\\RoyZ\\Desktop\\platform-tools\\adb.exe";
    /**
     * 截屏文件所在位置
     */
    public static final String SCREENSHOT_LOCATION = "C:\\Users\\RoyZ\\Desktop\\untitled\\s.png";

    /**
     * 窗体显示的图片宽度
     */
    public static final int RESIZED_SCREEN_WIDTH = 675;

    /**
     * 窗体显示的图片高度
     */
    public static final int RESIZED_SCREEN_HEIGHT = 1200;

    /**
     * 在675*1200分辨率下，跳跃蓄力时间与距离像素的比率<br>
     * 可根据实际情况自行调整
     */
    public static final float RESIZED_DISTANCE_PRESS_TIME_RATIO = 2.175f;

    /**
     * 截图间隔
     */
    public static final int SCREENSHOT_INTERVAL = 3000; // ms

    public static final int HDPI = 240;

    public static final int XHDPI = 330;

    public static final int XXHDPI = 480;

    /**
     * 手动模式
     */
    public static final int MODE_MANUAL = 1;
    /**
     * 半自动模式,只需要点secondPoint
     */
    public static final int MODE_SEMI_AUTO = 2;
    /**
     * 自动模式
     */
    public static final int MODE_AUTO = 3;
}
