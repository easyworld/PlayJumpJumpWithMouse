package com.company.playJumpJumpWithMouse;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Created by RoyZ on 2017/12/28.
 */
public class BackgroundImage4Panel extends javax.swing.JFrame {

    /**
     * serialVersionId
     */
    private static final long serialVersionUID = 1L;

    private static boolean isFirst = true;

    private static Point firstPoint;
    private static Point secondPoint;

    private static int playMode = Constants.MODE_MANUAL;

    private static BufferedImage bufferedImage;

    /**
     * Creates new form NewJFrame
     */
    public BackgroundImage4Panel() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * 测试入口
     *
     * @param args 参数列表
     */
    public static void main(String[] args) {

        ScreenAdapter.SCREEN_DPI = AdbCaller.getSize();

        final int resizedScreenWidth, resizedScreenHeight;
        final double resizedDistancePressTimeRatio;
        final int screenshotInterval;
        // adb path,screenshot path
        final String screenshotPath;

        Options options = new Options();
        Option opt = new Option("h", "help", false, "Print help");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("a", "adb-path", true,
                "adb path in system,required, eg: C:\\Users\\RoyZ\\Desktop\\platform-tools\\adb.exe");
        opt.setRequired(true);
        options.addOption(opt);

        opt = new Option("o", "screenshot-path", true,
                "screenshot path, eg: C:\\Users\\RoyZ\\Desktop\\untitled\\s.png");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("s", "size", true, "size of picture in window, eg: 675x1200");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("t", "interval", true, "screenshot interval, unit millisecond, eg: 2500");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("m", "play-mode", true, "1: manual-mode , 2: semi-mode(default) , 3: auto-mode ");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("r", "random", true, "random done perfect, Y:yes, N:no");
        opt.setRequired(false);
        options.addOption(opt);

        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(110);
        CommandLine commandLine = null;
        CommandLineParser parser = new PosixParser();
        try {
            commandLine = parser.parse(options, args);
            if (commandLine.hasOption('h')) {
                // 打印使用帮助
                hf.printHelp("PlayJumpJumpWithMouse", options, true);
            }

            if (commandLine.getOptionValue('a') != null) {
                AdbCaller.setAdbPath(commandLine.getOptionValue('a'));
            }
            if (commandLine.getOptionValue('o') != null) {
                AdbCaller.setScreenshotLocation(commandLine.getOptionValue('o'));
                screenshotPath = commandLine.getOptionValue('o');
            } else {
                AdbCaller.setScreenshotLocation("s.png");
                screenshotPath = "s.png";
            }
            if (commandLine.getOptionValue('s') != null
                    && Pattern.matches("\\d+x\\d+", commandLine.getOptionValue('s'))) {
                String[] str = commandLine.getOptionValue('s').split("x");
                resizedScreenWidth = Integer.parseInt(str[0]);
                resizedScreenHeight = Integer.parseInt(str[1]);
            } else {
                resizedScreenWidth = Constants.RESIZED_SCREEN_WIDTH;
                resizedScreenHeight = Constants.RESIZED_SCREEN_HEIGHT;
            }
            if (commandLine.getOptionValue('t') != null) {
                screenshotInterval = Integer.parseInt(commandLine.getOptionValue('t'));
            } else {
                screenshotInterval = Constants.SCREENSHOT_INTERVAL;
            }
            if (commandLine.getOptionValue('m') != null) {
                playMode = Integer.parseInt(commandLine.getOptionValue('m'));
            } else {
                playMode = Constants.MODE_SEMI_AUTO;
            }
            if (commandLine.getOptionValue('r') != null) {
                if (commandLine.getOptionValue('r').toLowerCase().trim().equals("y")) {
                    JumpPerfectControl.random = true;
                }
            }

        } catch (ParseException e) {
            hf.printHelp("PlayJumpJumpWithMouse", options, true);
            return;
        }
        //去掉了-r参数,改成根据width来进行自动换算.
        resizedDistancePressTimeRatio = Constants.RESIZED_DISTANCE_PRESS_TIME_RATIO * Constants.RESIZED_SCREEN_WIDTH / resizedScreenWidth;
        if (playMode == Constants.MODE_MANUAL || playMode == Constants.MODE_SEMI_AUTO) {
            manualMode(resizedScreenWidth, resizedScreenHeight, resizedDistancePressTimeRatio,
                    screenshotInterval, screenshotPath);
        } else if (playMode == Constants.MODE_AUTO) {
            autoJumpMode(screenshotInterval, screenshotPath);
        }

    }

    private static void manualMode(final int resizedScreenWidth, final int resizedScreenHeight,
                                   final double resizedDistancePressTimeRatio, final int screenshotInterval, final String screenshotPath) {

        AdbCaller.printScreen();
        final BackgroundImage4Panel backgroundImage4Panel = new BackgroundImage4Panel();
        backgroundImage4Panel.setSize(resizedScreenWidth, resizedScreenHeight);
        backgroundImage4Panel.setVisible(true);

        JPanel jPanel = new JPanel() {
            /**
             * serialVersionId
             */
            private static final long serialVersionUID = -1183754274585001429L;

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    bufferedImage = ImageIO.read(new File(screenshotPath));
                    BufferedImage newImage = new BufferedImage(resizedScreenWidth, resizedScreenHeight,
                            bufferedImage.getType());
                    if (playMode == Constants.MODE_SEMI_AUTO) {
                        firstPoint = StartCenterFinder.findStartCenter(bufferedImage);
                        firstPoint.setLocation(firstPoint.getX() * resizedScreenWidth / bufferedImage.getWidth(),
                                firstPoint.getY() * resizedScreenWidth / bufferedImage.getWidth());
                        System.out.println("firstPoint = [x=" + firstPoint.x + ",y=" + firstPoint.y + "]");
                        isFirst = false;
                    }
                    /**
                     * try to resize
                     */
                    Graphics gTemp = newImage.getGraphics();
                    gTemp.drawImage(bufferedImage, 0, 0, resizedScreenWidth, resizedScreenHeight, null);
                    gTemp.dispose();
                    bufferedImage = newImage;
                    g.drawImage(bufferedImage, 0, 0, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        backgroundImage4Panel.getContentPane().add(jPanel);

        backgroundImage4Panel.getContentPane().getComponent(0).addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel jp = ((JPanel) backgroundImage4Panel.getContentPane().getComponent(0));
                if (isFirst) {
                    System.out.println("first " + e.getX() + " " + e.getY());
                    firstPoint = e.getPoint();
                    isFirst = false;
                } else {
                    secondPoint = e.getPoint();
                    int distance = distance(firstPoint, secondPoint);
                    System.out.println("distance:" + distance);
                    isFirst = true;
                    AdbCaller.longPress(distance * resizedDistancePressTimeRatio, bufferedImage);// magic
                    // number
                    try {
                        Thread.sleep(screenshotInterval * 2 / 3 + new Random().nextInt(screenshotInterval / 3));// wait for screencap
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    AdbCaller.printScreen();
                    jp.validate();
                    jp.repaint();
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private static void autoJumpMode(final int screenshotInterval,
                                     final String screenshotPath) {
        new Thread() {
            public void run() {
                while (true) {
                    AdbCaller.printScreen();
                    try {
                        BufferedImage bufferedImage = ImageIO.read(new File(screenshotPath));
                        //自动模式的魔数也改为自动计算
                        double resizedDistancePressTimeRatio = Constants.RESIZED_DISTANCE_PRESS_TIME_RATIO * Constants.RESIZED_SCREEN_WIDTH / bufferedImage.getWidth();
                        firstPoint = StartCenterFinder.findStartCenter(bufferedImage);
                        secondPoint = EndCenterFinder.findEndCenter(bufferedImage, firstPoint);
                        // System.out.println(firstPoint + " , " + secondPoint);
                        int distance = secondPoint == null ? 0 : distance(firstPoint, secondPoint);
                        if (secondPoint == null || secondPoint.getX() == 0 || distance < ScreenAdapter.getBabyWidth() ||
                                // true || //放开可改为全部用ColorFilterFinder来做下一个中心点的查找
                                Math.abs(secondPoint.getX() - firstPoint.getX()) < ScreenAdapter.getBabyWidth() / 2) {
                            secondPoint = ColorFilterFinder.findEndCenter(bufferedImage, firstPoint);
                            if (secondPoint == null) {
                                AdbCaller.printScreen();
                                continue;
                            }
                        } else {
                            Point colorfilterCenter = ColorFilterFinder.findEndCenter(bufferedImage, firstPoint);
                            if (Math.abs(secondPoint.getX() - colorfilterCenter.getX()) > ScreenAdapter.getBabyWidth() / 3) {
                                secondPoint = colorfilterCenter;
                            }
                        }
                        System.out.println("firstPoint = [x=" + firstPoint.x + ",y=" + firstPoint.y
                                + "] , secondPoint = [x=" + secondPoint.x + ",y=" + secondPoint.y + "]");
                        ColorFilterFinder.updateLastShapeMinMax(bufferedImage, firstPoint, secondPoint);
                        distance = distance(firstPoint, secondPoint);
                        AdbCaller.longPress(distance * resizedDistancePressTimeRatio, bufferedImage);// magic
                        // number
                        try {
                            Thread.sleep(screenshotInterval * 2 / 3 + new Random().nextInt(screenshotInterval / 3));// wait for screencap
                            // screencap
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        AdbCaller.printScreen();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 对图片进行强制放大或缩小
     *
     * @param originalImage 原始图片
     * @return
     */
    public static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    public static int distance(Point a, Point b) {// 求两点距离
        return (int) Math.sqrt((a.x - b.getX()) * (a.x - b.getX()) + (a.y - b.getY()) * (a.y - b.getY()));
    }

}
