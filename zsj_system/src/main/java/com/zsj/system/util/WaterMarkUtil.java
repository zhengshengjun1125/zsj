package com.zsj.system.util;

import com.zsj.system.param.Location;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/30.
 */
public class WaterMarkUtil {

    public static Integer SIZE = 60;

    /**
     * 添加水印
     * 自定义位置
     */
    public static void waterMark(InputStream inputStream,
                                 Color color,
                                 String text,
                                 String location,
                                 HttpServletResponse response) throws Exception {
        BufferedImage image = ImageIO.read(inputStream);//读取图片对象
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 加水印
        //创建画笔
        Graphics2D g = bufImg.createGraphics();
        //srcImg 为上面获取到的原始图片的图片对象
        g.drawImage(image, 0, 0, width, height, null);
        //根据图片的背景设置水印颜色
        g.setColor(color);
        //设置水印的坐标
        if (location.equals(Location.FULL)) {
            SIZE = width / 20;//需要加倍缩小文字
            //设置字体  画笔字体样式为微软雅黑，加粗，文字大小为60pt
            g.setFont(new Font("微软雅黑", Font.BOLD, SIZE));
            //如果铺满的话是根据像素的各个方向来画 每一行四个 根据大小来算
            int row = width / 4; //每行画四个水印
            int column = height / 5;//一共画五行
            int rowBegin = 0;
            int columnBegin = 0;
            for (int i = 0; i < 4; i++) {
                columnBegin = 0;
                for (int j = 0; j < 5; j++) {
                    g.drawString(text, rowBegin, columnBegin += column);
                }
                rowBegin += row;
            }
            g.dispose();
            ImageIO.write(bufImg, "png", response.getOutputStream());
        } else {
            SIZE = width / 10;//根据图片的实际大小来算出真正字体应该有多大
            //设置字体  画笔字体样式为微软雅黑，加粗，文字大小为60pt
            g.setFont(new Font("微软雅黑", Font.BOLD, SIZE));
            // 算出水印位置
            int[] arr = getXYbyLocation(location, width, height, text, g);
            //画出水印 第一个参数是水印内容，第二个参数是x轴坐标，第三个参数是y轴坐标
            g.drawString(text, arr[0], arr[1]);
            g.dispose();
            // 输出图片
            ImageIO.write(bufImg, "png", response.getOutputStream());
        }
    }

    /**
     * 算法实现水印位置计算
     *
     * @param location 位置
     * @param width    原图宽度
     * @param height   原图高度
     * @param text
     * @param g
     * @return 真正水印的位置
     */
    private static int[] getXYbyLocation(String location, int width, int height, String text, Graphics2D g) {
        int x = (width - getWatermarkLength(text, g)) / 2;
        int y = height / 2;
        switch (location) {
            case Location.LEFT_UP:
                x = 0;
                y = SIZE;
                break;
            case Location.MID:
                x = (width - getWatermarkLength(text, g)) / 2;
                y = height / 2;
                break;
            case Location.RIGHT_UP:
                x = width - getWatermarkLength(text, g);
                y = SIZE;
                break;
            case Location.LEFT_DOWN:
                x = 0;
                y = height - SIZE;
                break;
            case Location.RIGHT_DOWN:
                x = width - getWatermarkLength(text, g);
                y = height - SIZE;
                break;
            default:
                break;
        }
        return new int[]{x, y};
    }

    /**
     * 获取水印文字的长度
     * @param waterMarkContent 水印文字
     * @param g 画图对象
     * @return 长度
     */
    private static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

}
