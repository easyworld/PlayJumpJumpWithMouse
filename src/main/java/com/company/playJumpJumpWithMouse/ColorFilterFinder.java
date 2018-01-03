package com.company.playJumpJumpWithMouse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 直接根据色差来定位下一个中心点 Created by tangshuai on 2017/12/29.
 */
public class ColorFilterFinder {

    static Color bgColor = Color.RED;

    static Point startCenterPoint;

    static int lastShapeMinMax = ScreenAdapter.getShapeMinWidth();

    public static Point findEndCenter(BufferedImage bufferedImage, Point startCenterPoint) {
        ColorFilterFinder.startCenterPoint = startCenterPoint;
        bgColor = new Color(bufferedImage.getRGB(bufferedImage.getWidth() / 2, 300));

        Point tmpStartCenterPoint;
        Point tmpEndCenterPoint;

        // 排除小人所在的位置的整个柱状区域检测,为了排除某些特定情况的干扰.
        Rectangle rectangle = new Rectangle((int) (startCenterPoint.getX() - lastShapeMinMax / 2), 0, lastShapeMinMax,
                (int) startCenterPoint.getY());

        Color lastColor = bgColor;
        for (int y = bufferedImage.getHeight() / 3; y < startCenterPoint.y; y++) {
            for (int x = 10; x < bufferedImage.getWidth(); x++) {
                if (rectangle.contains(x, y)) {
                    continue;
                }
                Color newColor = new Color(bufferedImage.getRGB(x, y));
                if ((Math.abs(newColor.getRed() - lastColor.getRed())
                        + Math.abs(newColor.getBlue() - lastColor.getBlue())
                        + Math.abs(newColor.getGreen() - lastColor.getGreen()) >= 24)
                        || (Math.abs(newColor.getRed() - lastColor.getRed()) >= 12
                        || Math.abs(newColor.getBlue() - lastColor.getBlue()) >= 12
                        || Math.abs(newColor.getGreen() - lastColor.getGreen()) >= 12)) {
//                    System.out.println("y = " + y + " x = " + x);
                    tmpStartCenterPoint = findStartCenterPoint(bufferedImage, x, y);
                    // System.out.println(tmpStartCenterPoint);
                    tmpEndCenterPoint = findEndCenterPoint(bufferedImage, tmpStartCenterPoint);
                    return new Point(tmpStartCenterPoint.x, (tmpEndCenterPoint.y + tmpStartCenterPoint.y) / 2);
                }
                lastColor = newColor;
            }
        }
        return null;
    }

    /**
     * 查找新方块/圆的有效结束最低位置
     *
     * @param bufferedImage
     * @param tmpStartCenterPoint
     * @return
     */
    private static Point findEndCenterPoint(BufferedImage bufferedImage, Point tmpStartCenterPoint) {
        Color startColor = new Color(bufferedImage.getRGB(tmpStartCenterPoint.x, tmpStartCenterPoint.y));
        Color lastColor = startColor;
        int centX = tmpStartCenterPoint.x, centY = tmpStartCenterPoint.y;
        for (int i = tmpStartCenterPoint.y; i < bufferedImage.getHeight() && i < startCenterPoint.y - 10; i++) {
            // -2是为了避开正方体的右边墙壁的影响
            Color newColor = new Color(bufferedImage.getRGB(tmpStartCenterPoint.x, i));
            if (Math.abs(newColor.getRed() - lastColor.getRed()) <= 8
                    && Math.abs(newColor.getGreen() - lastColor.getGreen()) <= 8
                    && Math.abs(newColor.getBlue() - lastColor.getBlue()) <= 8) {
                centY = i;
            }
        }
        if (centY - tmpStartCenterPoint.y < ScreenAdapter.getMinShapeHeight()) {
            centY = centY + ScreenAdapter.getMinShapeHeight();
        }
        if (centY - tmpStartCenterPoint.y > ScreenAdapter.getMaxShapeHeight()) {
            centY = tmpStartCenterPoint.y + ScreenAdapter.getMaxShapeHeight();
        }
        if (JumpPerfectControl.needMis()) {
            centY -= 10;
        }
        return new Point(centX, centY);
    }

    // 查找下一个方块的最高点的中点
    private static Point findStartCenterPoint(BufferedImage bufferedImage, int x, int y) {
        Color lastColor = new Color(bufferedImage.getRGB(x - 1, y));
        int centX = x, centY = y;
        for (int i = x; i < bufferedImage.getWidth(); i++) {
            Color newColor = new Color(bufferedImage.getRGB(i, y));
            if ((Math.abs(newColor.getRed() - lastColor.getRed()) + Math.abs(newColor.getBlue() - lastColor.getBlue())
                    + Math.abs(newColor.getGreen() - lastColor.getGreen()) >= 24)
                    || (Math.abs(newColor.getRed() - lastColor.getRed()) >= 12
                    || Math.abs(newColor.getBlue() - lastColor.getBlue()) >= 12
                    || Math.abs(newColor.getGreen() - lastColor.getGreen()) >= 12)) {
                centX = x + (i - x) / 2;
            } else {
                break;
            }
        }
        return new Point(centX, centY);
    }

    private static boolean like(Color a, Color b) {
        return !((Math.abs(a.getRed() - b.getRed()) + Math.abs(a.getBlue() - b.getBlue())
                + Math.abs(a.getGreen() - b.getGreen()) >= 24)
                || (Math.abs(a.getRed() - b.getRed()) >= 12 || Math.abs(a.getBlue() - b.getBlue()) >= 12
                || Math.abs(a.getGreen() - b.getGreen()) >= 12));
    }

    public static void updateLastShapeMinMax(BufferedImage bufferedImage, Point first, Point second) {
        if (first.x < second.y) {
            for (int x = second.x; x < bufferedImage.getWidth(); x++) {
                Color newColor = new Color(bufferedImage.getRGB(x, second.y));
                if (like(newColor, bgColor)) {
                    lastShapeMinMax = (int) Math.max((x - second.x) * 1.5, lastShapeMinMax);
                    break;
                }
            }
        } else {
            for (int x = second.x; x >= 10; x--) {
                Color newColor = new Color(bufferedImage.getRGB(x, second.y));
                if (like(newColor, bgColor)) {
                    lastShapeMinMax = (int) Math.max((second.x - x) * 1.5, lastShapeMinMax);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

        // BufferedImage bufferedImage = ImageIO.read(new
        // File(Constants.SCREENSHOT_2));
        BufferedImage bufferedImage = ImageIO.read(new File("/Users/tangshuai/Desktop/tmp/665_908.png"));
        Point point = StartCenterFinder.findStartCenter(bufferedImage);
        System.out.println(point);

        Point point2 = findEndCenter(bufferedImage, point);
        System.out.println(point2);

    }


    public static String toHexFromColor(Color color) {
        String r, g, b;
        StringBuilder su = new StringBuilder();
        r = Integer.toHexString(color.getRed());
        g = Integer.toHexString(color.getGreen());
        b = Integer.toHexString(color.getBlue());
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        r = r.toUpperCase();
        g = g.toUpperCase();
        b = b.toUpperCase();
        su.append("0xFF");
        su.append(r);
        su.append(g);
        su.append(b);
        //0xFF0000FF
        return su.toString();
    }
}
