package com.company.playJumpJumpWithMouse;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * 找小人的底盘中心点. Created by tangshuai on 2017/12/29.
 */
public class StartCenterFinder {

    static int[] centers = ScreenAdapter.getCenterArrays();

    public static Point findStartCenter(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int centerX = 0;
        int centerY = 0;
        for (int h = 0; h < height; h++)
            for (int w = 0; w < width; w++) {
                int color = bufferedImage.getRGB(w, h);
                if (color == centers[0]) {
                    if (checkIsCenter(bufferedImage, h, w)) {
                        centerX = w + ScreenAdapter.getBabyWidth() / 2;
                        centerY = h;

                        return new Point(centerX, (centerY + 2));
                    }
                }
            }
        return new Point(0, -1);
    }

    private static boolean checkIsCenter(BufferedImage bufferedImage, int h, int w) {
        for (int i = w; i < w + 50; i++) {
            int color = bufferedImage.getRGB(i, h);
            Color centerColor = new Color(centers[i - w]);
            Color newColor = new Color(color);
            if (Math.abs(newColor.getRed() - centerColor.getRed()) > 5
                    || Math.abs(newColor.getGreen() - centerColor.getGreen()) > 5
                    || Math.abs(newColor.getBlue() - centerColor.getBlue()) > 5) {
                return false;
            }
        }
        return true;
    }

}
