package api.tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyPictureUtils {

    public static void main(String[] args) {

        // 527 * 764
        // 测试图片的叠加
        overlyingImageTest();

    }

    /**
     * Java 测试图片叠加方法
     */
    public static void overlyingImageTest() {
        String sourceFilePath = "D://work//UploadDoc//map_dt.png";
        String waterFilePath = "D://work//UploadDoc//Rainfall/dzx1d67bcd6-ba49-4e44-a14f-d47303d98c40.png";
        String saveFilePath = "D://work//UploadDoc//overlyingImageNew.png";
        String distImgPath = "D://work//UploadDoc//overlyingImage_ys.png";
        try {
            BufferedImage bufferImage1 = getBufferedImage(sourceFilePath);
            BufferedImage bufferImage2 = getBufferedImage(waterFilePath);
            // 图像压缩  保证每张图片大小一致
            int width = bufferImage1.getWidth();
            int height = bufferImage1.getHeight();
            bufferImage2 = resize(bufferImage2, 510,750);

//            // 保存压缩后的图片
//            FileOutputStream out = new FileOutputStream(distImgPath);
//            ImageIO.write(bufferImage2, "jpg", out);
//            System.out.println("压缩完成");
//            out.close();

            // 构建叠加层
            BufferedImage buffImg = overlyingImage(bufferImage1, bufferImage2, 125, 15, 0.7f);
            // 输出水印图片
            generateSaveFile(buffImg, saveFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Java
     */

    /**
     *
     * @Title: 构造图片
     * @Description: 生成水印并返回 等值线与底图进行叠加图
     * @param sourceFilePath 源文件（底图图片）
     * @param dzmFilePath 水印文件（等值面图片）
     * @param saveFilePath 输出文件 （生成合并图片）
     * @param x 距离右下角的X偏移量(等值面图）
     * @param y 距离右下角的Y偏移量(等值面图）
     * @param alpha 透明度, 选择值从0.0~1.0: 完全透明~完全不透明
     * @return boolean  1 成功、0 失败
     * @throws IOException
     */
    public static boolean overlyingImage(String sourceFilePath,String dzmFilePath,String saveFilePath, int x, int y, float alpha) {
        //String sourceFilePath = "D://work//UploadDoc//map_dt.png";
        //String dzmFilePath = "D://work//UploadDoc//Rainfall/dzx1d67bcd6-ba49-4e44-a14f-d47303d98c40.png";
        //String saveFilePath = "D://work//UploadDoc//overlyingImageNew.png";
        //String distImgPath = "D://work//UploadDoc//overlyingImage_ys.png";
        try {
            BufferedImage bufferImage1 = getBufferedImage(sourceFilePath);
            BufferedImage bufferImage2 = getBufferedImage(dzmFilePath);
            // 图像压缩  保证每张图片大小一致
            int width = bufferImage1.getWidth();
            int height = bufferImage1.getHeight();
            bufferImage2 = resize(bufferImage2, 508,748);

//            // 保存压缩后的图片
//            FileOutputStream out = new FileOutputStream(distImgPath);
//            ImageIO.write(bufferImage2, "jpg", out);
//            System.out.println("压缩完成");
//            out.close();

            // 构建叠加层 overlyingImage(bufferImage1, bufferImage2, 125, 15, 0.7f);
            BufferedImage buffImg = overlyingImage(bufferImage1, bufferImage2, x, y, alpha);
            // 输出水印图片
            generateSaveFile(buffImg, saveFilePath);

            return  true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 压缩图片
     */
    private static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
        // targetW，targetH分别表示目标长和宽
        int type = source.getType();
        BufferedImage target = null;
        int width = source.getWidth();
        int height = source.getHeight();
        double sx = (double) targetW / width;
        double sy = (double) targetH / height;
        System.out.println( " sx = " + sx + " sy = " + sy);
        // 这里想实现在targetW，targetH范围内实现等比缩放
        if (sx > sy) {
            sx = sy;
            targetW = (int) (sx * source.getWidth());
        } else {
//            sy = sx;
//            targetH = (int) (sy * source.getHeight());
//            targetW = (int) (targetW * 1.01);
//            targetH = (int) (sy * source.getHeight() * 1.05);
//            targetW = (int) (527);
//            targetH = (int) (754);
        }
        System.out.println( type + " = " + type);
        System.out.println( " targetW = " + targetW + " targetH = " + targetH);
//        if (type == BufferedImage.TYPE_CUSTOM) {
//            ColorModel cm = source.getColorModel();
//            WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
//                    targetH);
//            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
//            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
//        } else {
            target = new BufferedImage(targetW, targetH, type);
//        }
        Graphics2D g = target.createGraphics();
        // smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    /**
     * @param fileUrl 文件绝对路径或相对路径
     * @return 读取到的缓存图像
     * @throws IOException 路径错误或者不存在该文件时抛出IO异常
     */
    public static BufferedImage getBufferedImage(String fileUrl) throws IOException {
        File f = new File(fileUrl);
        return ImageIO.read(f);
    }

    /**
     * 输出图片
     * @param buffImg 图像拼接叠加之后的BufferedImage对象
     * @param savePath 图像拼接叠加之后的保存路径
     */
    public static void generateSaveFile(BufferedImage buffImg, String savePath) {
        int temp = savePath.lastIndexOf(".") + 1;
        try {
            File outFile = new File(savePath);
            if(!outFile.exists()){
                outFile.createNewFile();
            }
            ImageIO.write(buffImg, savePath.substring(temp), outFile);

            System.out.println("ImageIO write...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @Title: 构造图片
     * @Description: 生成水印并返回java.awt.image.BufferedImage
     * @param buffImg 源文件(BufferedImage)
     * @param waterImg 水印文件(BufferedImage)
     * @param x 距离右下角的X偏移量
     * @param y 距离右下角的Y偏移量
     * @param alpha 透明度, 选择值从0.0~1.0: 完全透明~完全不透明
     * @return BufferedImage
     * @throws IOException
     */
    private static BufferedImage overlyingImage(BufferedImage buffImg, BufferedImage waterImg, int x, int y, float alpha) throws IOException {
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = buffImg.createGraphics();
        int waterImgWidth = waterImg.getWidth();// 获取层图的宽度
        int waterImgHeight = waterImg.getHeight();// 获取层图的高度
        // 在图形和图像中实现混合和透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        // 绘制
        g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
        g2d.dispose();// 释放图形上下文使用的系统资源
        return buffImg;
    }
}
