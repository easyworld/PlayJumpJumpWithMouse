package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    /**
     * Creates new form NewJFrame
     */
    public BackgroundImage4Panel() {
        setSize(675, 1200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * 测试入口
     *
     * @param args 参数列表
     */
    public static void main(String[] args) {
        AdbCaller.printScreen();
        BackgroundImage4Panel backgroundImage4Panel = new BackgroundImage4Panel();
        backgroundImage4Panel.setVisible(true);

        JPanel jPanel = new JPanel() {
            /**
             * serialVersionId
             */
            private static final long serialVersionUID = -1183754274585001429L;

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage bufferedImage = ImageIO.read(new File(Constants.TMP_IMG_PATH));
                    BufferedImage newImage = new BufferedImage(675, 1200, bufferedImage.getType());
                    /**
                     * try to resize
                     */
                    Graphics gTemp = newImage.getGraphics();
                    gTemp.drawImage(bufferedImage, 0, 0, 675, 1200, null);
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
                if (isFirst) {
                    System.out.println("first " + e.getX() + " " + e.getY());
                    firstPoint = e.getPoint();
                    isFirst = false;
                } else {
                    secondPoint = e.getPoint();
                    int distance = distance(firstPoint, secondPoint);
                    System.out.println("distance:" + distance);
                    isFirst = true;
                    AdbCaller.call(distance * 2.19);//magic number
                    try {
                        Thread.sleep(2500);// wait for screencap
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    AdbCaller.printScreen();
                    JPanel jp = ((JPanel) backgroundImage4Panel.getContentPane().getComponent(0));
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

    /**
     * 实现图像的等比缩放
     *
     * @param source  待处理的图片流
     * @param targetW 宽度
     * @param targetH 高度
     * @return
     */
    public static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
        int width = source.getWidth();// 图片宽度
        int height = source.getHeight();// 图片高度
        return zoomInImage(source, targetW, targetH);
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

    public static int distance(Point a, Point b) {//求两点距离
        return (int) Math.sqrt((a.x - b.getX()) * (a.x - b.getX()) + (a.y - b.getY()) * (a.y - b.getY()));
    }
}
