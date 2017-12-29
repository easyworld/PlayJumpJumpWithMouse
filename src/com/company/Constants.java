package com.company;

/**
 * Created by RoyZ on 2017/12/29.
 */
public class Constants {
    /**
     * adb所在位置
     */
    public static final String ADB_PATH = "/usr/local/bin/adb";
    /**
     * 截屏文件所在位置
     */
    public static final String SCREENSHOT_LOCATION = "/Users/Allen/Downloads/adb_screenshot.png";

    // TODO：我也不知道这个参数怎么取名较合适
    // https://github.com/easyworld/PlayJumpJumpWithMouse/issues/6
    // 经过测试发现，这个参数跟截图的尺寸有关
    // 原作者 675x1200 的尺寸该参数取 2.19
    // 我手机是小米max，1080x1920 分辨率，按比例缩放至 540x960，取 2.85 正合适
    public static final float SWIPE_DEPTH = 2.85f;
    public static final int SCREEN_WIDTH = 540;
    public static final int SCREEN_HEIGHT = 960;

    // 跳跃间隔，为了让某些彩蛋生效(+5,+10,+15,+30)，至少需要2s
    public static final int JUMP_INTERVAL = 2500; // ms
}
