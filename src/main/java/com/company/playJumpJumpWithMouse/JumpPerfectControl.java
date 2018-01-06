package com.company.playJumpJumpWithMouse;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连跳次数随机偏移控制
 * Created by tangshuai on 2018/1/2.
 */
public class JumpPerfectControl {

    public static boolean random = false;
    public static int randomStepPx = 50;

    /**
     * 距离随机偏移50px，模仿人类的行为
     *
     * @return
     */
    public static int getRandomDistance(int distance) {
        int result = (int) (distance + ((Math.random() - 0.5) * 2 * randomStepPx));
        System.out.println("calculated distance " + distance + ", random distance " + result);
        return result;
    }

}
