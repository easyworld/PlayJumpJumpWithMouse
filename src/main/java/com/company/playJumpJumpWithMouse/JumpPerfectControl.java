package com.company.playJumpJumpWithMouse;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连跳次数随机偏移控制
 * Created by tangshuai on 2018/1/2.
 */
public class JumpPerfectControl {

    private static AtomicInteger jumpCouter = new AtomicInteger(0);

    private static int randomMagic = 10;

    public static boolean random = false;

    public static boolean needMis() {
        return random && jumpCouter.get() % randomMagic == 0;
    }

    public static void jumpNext() {
        int counter = jumpCouter.incrementAndGet();
        if (random && counter % randomMagic == 0) {
            randomMagic = 10 + new Random().nextInt(20);
            jumpCouter.set(0);
        }
    }

}
