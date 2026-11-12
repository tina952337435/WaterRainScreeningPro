package api.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 添加水印util
 */
public class WatermarkUtil {

    /**
     * 读取本地图片
     *
     * @param path 本地图片存放路径
     */
    public static Image readLocalPicture(String path) {
        if (null == path) {
            throw new RuntimeException("本地图片路径不能为空");
        }
        // 读取原图片信息 得到文件
        File srcImgFile = new File(path);
        try {
            // 将文件对象转化为图片对象
            return ImageIO.read(srcImgFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取网络图片
     *
     * @param path 网络图片地址
     */
    public static Image readNetworkPicture(String path) {
        if (null == path) {
            throw new RuntimeException("网络图片路径不能为空");
        }
        try {
            // 创建一个URL对象,获取网络图片的地址信息
            URL url = new URL(path);
            // 将URL对象输入流转化为图片对象 (url.openStream()方法，获得一个输入流)
            BufferedImage bugImg = ImageIO.read(url.openStream());
            if (null == bugImg) {
                throw new RuntimeException("网络图片地址不正确");
            }
            return bugImg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 水印处理
     *
     * @param image     图片对象
     * @param type      水印类型（1-文字水印 2-图片水印）
     * @param watermark 水印内容（文字水印内容/图片水印的存放路径）
     */
    public static String manageWatermark(Image image, Integer type, String watermark) {
        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);
        BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        // 加水印：
        // 创建画笔
        Graphics2D graphics = bufImg.createGraphics();
        // 绘制原始图片
        graphics.drawImage(image, 0, 0, imgWidth, imgHeight, null);

        // 校验水印的类型
        if (type == 1) {
            if (StringUtils.isEmpty(watermark)) {
                throw new RuntimeException("文字水印内容不能为空");
            }

            // 添加文字水印：
            // 根据图片的背景设置水印颜色
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            // 设置字体  画笔字体样式为微软雅黑，加粗，文字大小为45pt
            graphics.setFont(new Font("等线 (正文)", Font.BOLD, 12));//
            // 设置水印的坐标(为原图片中间位置)
            int x = (imgWidth - getWatermarkLength(watermark, graphics)) / 2;
            int y = imgHeight / 2;
            // 画出水印 第一个参数是水印内容，第二个参数是x轴坐标，第三个参数是y轴坐标

            //张家港站入境
            watermark = "张家港站";
            x = 148;
            y = 72;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);

            //新开闸站入境
            watermark = "新开闸站";
            x = 54;
            y = 65;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);

            //鳗鲤泾段入境
            watermark = "鳗鲤泾段";
            x = 42;
            y = 210;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);

            //野尤泾段入境
            watermark = "野尤泾段";
            x = 32;
            y = 285;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);

            //市区娄江段入境
            watermark = "市区娄江段";
            x = 10;
            y = 363;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);

            //市区吴淞江站入境
            watermark = "市区吴淞江站";
            x = 26;
            y = 465;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);

            //长牵路短入境
            watermark = "长牵路段";
            x = 45;
            y = 590;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);



            //周市杨林塘段入境
            watermark = "周市杨林塘段";
            x = 408;
            y = 100;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);

            //浏河段入境
            watermark = "浏河段";
            x = 555;
            y = 241;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);

            //花桥吴淞江大桥段入境
            watermark = "花桥吴淞江大桥段";
            x = 550;
            y = 523;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);

            //千灯浦闸段入境
            watermark = "千灯浦闸段";
            x = 395;
            y = 633;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);

            //大朱砂港桥段入境
            watermark = "大朱砂港桥段";
            x = 320;
            y = 743;
            graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
            graphics.drawString(watermark +"入境", x, y);
            y = y + 35;
            graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
            graphics.drawString(watermark +"出境", x, y);

            graphics.dispose();
        } else {
            // 添加图片水印：
            if (StringUtils.isEmpty(watermark)) {
                throw new RuntimeException("图片水印存放路径不能为空");
            }
            Image srcWatermark = readLocalPicture(watermark);
            int watermarkWidth = srcWatermark.getWidth(null);
            int watermarkHeight = srcWatermark.getHeight(null);
            // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));

            System.out.println(watermarkWidth+" = " + watermarkHeight);

            // 旋转，degree是整型，度数，比如垂直90度。
            graphics.rotate(Math.toRadians(180),460,261);
            graphics.drawImage(srcWatermark,460,261, watermarkWidth, watermarkHeight, null);
//            // 绘制水印图片  坐标为中间位置
//            graphics.drawImage(srcWatermark, (imgWidth - watermarkWidth) / 2,
//                    (imgHeight - watermarkHeight) / 2, watermarkWidth, watermarkHeight, null);
            graphics.dispose();
        }
        // 定义存储的地址
        String tarImgPath = "D://log//watermark_" + UUID.randomUUID()+".png";
        // 输出图片
        try {
            FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
            ImageIO.write(bufImg, "png", outImgStream);
            outImgStream.flush();
            outImgStream.close();
            return "水印添加成功";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 水印处理
     *
     * @param image     图片对象
     * @param type      水印类型（1-文字水印 2-图片水印）
     * @param mList 水印内容（文字水印内容/图片水印的存放路径）
     */
    public static String manageWatermark(Image image, Integer type, List<Map<String,Object>> mList,String saveFilePath,String templateFilePath) {
        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);
        BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        // 加水印：
        // 创建画笔
        Graphics2D graphics = bufImg.createGraphics();
        // 绘制原始图片
        graphics.drawImage(image, 0, 0, imgWidth, imgHeight, null);

        // 校验水印的类型
        if (type == 1) {
            String watermark = "";
//            if (StringUtils.isEmpty(watermark)) {
//                throw new RuntimeException("文字水印内容不能为空");
//            }

            String accpw_left = "accpw_left.png", accpw_right = "accpw_right.png", accdw_left = "accdw_left.png", accdw_right = "accdw_right.png";
            String strPicFilePath = templateFilePath;
            if(null != mList && mList.size() > 0){
                // 添加文字水印：
                // 根据图片的背景设置水印颜色
                graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
                // 设置字体  画笔字体样式为微软雅黑，加粗，文字大小为45pt
                graphics.setFont(new Font("等线 (正文)", Font.BOLD, 14));//
                // 设置水印的坐标(为原图片中间位置)
                int x = (imgWidth - getWatermarkLength(watermark, graphics)) / 2;
                int y = imgHeight / 2;
                // 画出水印 第一个参数是水印内容，第二个参数是x轴坐标，第三个参数是y轴坐标

                for(Map<String,Object> map: mList){
                    if(null != map.get("stcd")){
                        String stcd = map.get("stcd").toString();
                        String stnm = map.get("stnm").toString();

                        int accpw = (int) Math.ceil(Double.parseDouble(map.get("accpw").toString()));
                        int accdw = (int) Math.ceil(Double.parseDouble(map.get("accdw").toString()));
    //                    Double accpw = (new BigDecimal(map.get("accpw").toString()).setScale(0, BigDecimal.ROUND_HALF_UP)).doubleValue() ;
    //                    Double accdw = (new BigDecimal(map.get("accdw").toString()).setScale(0, BigDecimal.ROUND_HALF_UP)).doubleValue() ;
                        if(stcd.equals("S3_bi6YX0tyQq1L")){
                            //市区吴淞江站入境
                            watermark = "市区吴淞江站";
                            x = 26;
                            y = 455;
                            if(accpw > 0){
                                graphics.setColor(new Color(32,56,100));    //出境颜色 192,0,0
                                graphics.drawString(watermark +"入境", x, y);

                                graphics.drawString(String.valueOf(accpw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accpw_left_S3_bi6YX0tyQq1L.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 130,y - 20 , watermarkWidth, watermarkHeight, null);
                            }
                            y = y + 35;
                            if(accdw > 0){
                                graphics.setColor(new Color(192,0,0));    //出境颜色 192,0,0
                                graphics.drawString(watermark +"出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accdw_left_S3_bi6YX0tyQq1L.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 130,y - 20 , watermarkWidth, watermarkHeight, null);
                            }
                        }else if(stcd.equals("S3_nTuV7PHpBfA0")){
                            //浏河段入境
                            watermark = "浏河段";
                            x = 555;
                            y = 241;
                            if(accpw > 0) {
                                graphics.setColor(new Color(32, 56, 100));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "入境", x, y);

                                graphics.drawString(String.valueOf(accpw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accpw_right_S3_nTuV7PHpBfA0.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x - 40,y, watermarkWidth, watermarkHeight, null);
                            }
                            y = y + 35;
                            if(accdw > 0) {
                                graphics.setColor(new Color(192, 0, 0));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accdw_right_S3_nTuV7PHpBfA0.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x - 40,y, watermarkWidth, watermarkHeight, null);
                            }
                        }else if(stcd.equals("S3_qRw3jJb1r6og")){
                            //周市杨林塘段入境
                            watermark = "周市杨林塘段";
                            x = 408;
                            y = 100;
                            if(accpw > 0) {
                                graphics.setColor(new Color(32, 56, 100));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "入境", x, y);

                                graphics.drawString(String.valueOf(accpw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accpw_right_S3_qRw3jJb1r6og.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x - 20,y + 50 , watermarkWidth, watermarkHeight, null);
                            }
                            y = y + 35;
                            if(accdw > 0) {
                                graphics.setColor(new Color(192, 0, 0));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accdw_right_S3_qRw3jJb1r6og.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x - 20,y + 50 , watermarkWidth, watermarkHeight, null);
                            }
                        }else if(stcd.equals("S3_u679EzPpKExN")){
                            //张家港站入境
                            watermark = stnm;
                            x = 148;
                            y = 72;
                            if(accpw > 0) {
                                graphics.setColor(new Color(32, 56, 100));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "入境", x, y);

                                graphics.drawString(String.valueOf(accpw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accpw_S3_u679EzPpKExN.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 30,y + 25 , watermarkWidth, watermarkHeight, null);
                            }
                            y = y + 35;
                            if(accdw > 0) {
                                graphics.setColor(new Color(192, 0, 0));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accdw_S3_u679EzPpKExN.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x,y , watermarkWidth, watermarkHeight, null);
                            }
                        }else if(stcd.equals("S3_evBOomvD2vrj")){
                            //新开闸站入境
                            watermark = stnm;
                            x = 54;
                            y = 65;
                            if(accpw > 0) {
                                graphics.setColor(new Color(32, 56, 100));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "入境", x, y);

                                graphics.drawString(String.valueOf(accpw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accpw_S3_evBOomvD2vrj.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 60 ,y + 60 , watermarkWidth, watermarkHeight, null);
                            }
                            y = y + 35;
                            if(accdw > 0) {
                                graphics.setColor(new Color(192, 0, 0));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accdw_S3_evBOomvD2vrj.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 30,y + 35 , watermarkWidth, watermarkHeight, null);
                            }
                        }else if(stcd.equals("S3_q2CEYa98JC9j")){
                            //鳗鲤泾段入境
                            watermark = stnm;
                            x = 42;
                            y = 210;
                            if(accpw > 0) {
                                graphics.setColor(new Color(32, 56, 100));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "入境", x, y);

                                graphics.drawString(String.valueOf(accpw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accpw_left.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 90,y-10 , watermarkWidth, watermarkHeight, null);
                            }
                            y = y + 35;
                            if(accdw > 0) {
                                graphics.setColor(new Color(192, 0, 0));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accdw_left.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 90,y-10 , watermarkWidth, watermarkHeight, null);
                            }
                        }else if(stcd.equals("S3_v7fpFOBSdaY3")){
                            //市区娄江段入境
                            watermark = stnm;
                            x = 10;
                            y = 363;
                            if(accpw > 0) {
                                graphics.setColor(new Color(32, 56, 100));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "入境", x, y);

                                graphics.drawString(String.valueOf(accpw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accpw_left_S3_v7fpFOBSdaY3.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 115,y - 20 , watermarkWidth, watermarkHeight, null);
                            }
                            y = y + 35;
                            if(accdw > 0) {
                                graphics.setColor(new Color(192, 0, 0));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accdw_left_S3_v7fpFOBSdaY3.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 115,y - 20 , watermarkWidth, watermarkHeight, null);
                            }
                        }else if(stcd.equals("632E2452")){
                            //野尤泾段入境 632E2452
                            watermark = stnm;
                            x = 32;
                            y = 285;
                            if(accpw > 0) {
                                graphics.setColor(new Color(32, 56, 100));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "入境", x, y);

                                graphics.drawString(String.valueOf(accpw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accpw_left.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 90,y-10 , watermarkWidth, watermarkHeight, null);
                            }
                            y = y + 35;
                            if(accdw > 0) {
                                graphics.setColor(new Color(192, 0, 0));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accdw_left.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 90,y-10 , watermarkWidth, watermarkHeight, null);
                            }
                        }else if(stcd.equals("63205581")){
                            //花桥吴淞江大桥段入境 63205581
                            watermark = "花桥吴淞江大桥段";
                            x = 550;
                            y = 523;
                            if(accdw > 0) {
                                graphics.setColor(new Color(192, 0, 0));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accdw_right_63205581.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x - 70,y-30 , watermarkWidth, watermarkHeight, null);
                            }
                        }else if(stcd.equals("632D2460")){
                            //长牵路短入境
                            watermark = "长牵路段";
                            x = 45;
                            y = 590;
                            if(accpw > 0) {
                                graphics.setColor(new Color(32, 56, 100));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "入境", x, y);

                                graphics.drawString(String.valueOf(accpw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accpw_632D2460.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x + 80,y - 15 , watermarkWidth, watermarkHeight, null);
                            }
                            y = y + 35;
                            if(accdw > 0) {
                                graphics.setColor(new Color(192, 0, 0));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);
                            }
                        }else if(stcd.equals("63205650")){
                            //千灯浦闸段入境 63205650
                            watermark = "千灯浦闸段";
                            x = 395;
                            y = 633;
                            if(accpw > 0) {
                                graphics.setColor(new Color(32, 56, 100));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "入境", x, y);

                                graphics.drawString(String.valueOf(accpw), x + 30, y + 18);
                            }
                            y = y + 35;
                            if(accdw > 0) {
                                graphics.setColor(new Color(192, 0, 0));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accdw_63205650.png"; //入境
                                System.out.println( stcd +":"+stnm + " _ " + _pic);
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark,x - 55,y - 45 , watermarkWidth, watermarkHeight, null);
                            }
                        }else if(stcd.equals("63403910")) {
                            //大朱砂港桥段入境 63403910
                            watermark = "大朱砂港桥段";
                            x = 320;
                            y = 743;
                            if (accpw > 0) {
                                graphics.setColor(new Color(32, 56, 100));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "入境", x, y);

                                graphics.drawString(String.valueOf(accpw), x + 30, y + 18);
                            }
                            if (accdw > 0) {
                                graphics.setColor(new Color(192, 0, 0));    //出境颜色 192,0,0
                                graphics.drawString(watermark + "出境", x, y);

                                graphics.drawString(String.valueOf(accdw), x + 30, y + 18);

                                String _pic = strPicFilePath + "accdw_63403910.png"; //入境
                                Image srcWatermark = readLocalPicture(_pic);
                                int watermarkWidth = srcWatermark.getWidth(null);
                                int watermarkHeight = srcWatermark.getHeight(null);
                                // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
                                // 旋转，degree是整型，度数，比如垂直90度。
    //                            graphics.rotate(Math.toRadians(-30),x - 80,y - 20);
                                graphics.drawImage(srcWatermark, x - 70, y - 65, watermarkWidth, watermarkHeight, null);
                            }
                        }
                    }
                }
            }

            graphics.dispose();
        }
//        else {
//            // 添加图片水印：
//            if (StringUtils.isEmpty(watermark)) {
//                throw new RuntimeException("图片水印存放路径不能为空");
//            }
//            Image srcWatermark = readLocalPicture(watermark);
//            int watermarkWidth = srcWatermark.getWidth(null);
//            int watermarkHeight = srcWatermark.getHeight(null);
//            // 设置 alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
//            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.9f));
//            // 绘制水印图片  坐标为中间位置
//            graphics.drawImage(srcWatermark, (imgWidth - watermarkWidth) / 2,
//                    (imgHeight - watermarkHeight) / 2, watermarkWidth, watermarkHeight, null);
//            graphics.dispose();
//        }

        // 定义存储的地址
        String tarImgPath = StringUtils.isEmpty(saveFilePath) ? "D://log//watermark_" + UUID.randomUUID()+".png" : saveFilePath;
        // 输出图片
        try {
            FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
            ImageIO.write(bufImg, "png", outImgStream);
            outImgStream.flush();
            outImgStream.close();
            return "1";
        } catch (IOException e) {
            return "0";
        }
    }

    /**
     * 添加水印
     *
     * @param pictureType   图片来源类型（1-本地图片 2-网络图片）
     * @param watermarkType 水印类型（1-文字水印 2-图片水印）
     * @param path          图片路径
     * @param watermark     水印内容（文字水印内容/图片水印的存放路径）
     * @param saveFilePath   图片存储路径
     */
    public static String addWatermark(Integer pictureType, Integer watermarkType, String path, String watermark,List<Map<String,Object>> mList,String saveFilePath,String templateFilePath) {
        if (null == pictureType) {
            throw new RuntimeException("图片来源类型不能为空");
        }
        if (null == watermarkType) {
            throw new RuntimeException("水印类型不能为空");
        }

        Image image;
        if (pictureType == 1) {
            // 读取本地图片
            image = readLocalPicture(path);
        } else {
            // 读取网络图片
            image = readNetworkPicture(path);
        }
        if (watermarkType == 1) {
            if(StringUtils.isEmpty(watermark)){
                // 添加文字水印
                return manageWatermark(image, 1, mList, saveFilePath,templateFilePath);
            }else{
                // 添加文字水印
                return manageWatermark(image, 1, watermark);
            }
        } else {
            // 添加图片水印
            return manageWatermark(image, 2, watermark);
        }
    }

    /**
     * 获取水印文字的长度
     *
     * @param watermarkContent 文字水印内容
     * @param graphics         图像类
     */
    private static int getWatermarkLength(String watermarkContent, Graphics2D graphics) {
        return graphics.getFontMetrics(graphics.getFont()).charsWidth(watermarkContent.toCharArray(), 0, watermarkContent.length());
    }

    public static void main(String[] args) throws IOException {

        // 本地图片路径：
        String localPath = "D://log//sl_indexN.png";
        // 网络图片地址：
        String networkPath = "https://img0.baidu.com/it/u=3708545959,316194769&fm=253&fmt=auto&app=138&f=PNG?w=1000&h=1000";
        // 文字水印内容
        String textWatermark = "市区娄江段入境";
        // 图片水印路径
        String pictureWatermark = "D:\\log\\2.png";

        //输出存储图片 路径
        String saveFilePath = "D://log//new_" + UUID.randomUUID()+".png";

        List<Map<String,Object>> mList = new ArrayList<>();
        String strRes = "[{\"stcd\":\"S3_v7fpFOBSdaY3\",\"stnm\":\"市区娄江段\",\"accpw\":110,\"accdw\":2386,\"lgtd\":120.8579349,\"lttd\":31.36917461},{\"stcd\":\"S3_qRw3jJb1r6og\",\"stnm\":\"周市杨林塘段\",\"accpw\":1311,\"accdw\":1152,\"lgtd\":121.0109067,\"lttd\":31.48094081},{\"stcd\":\"S3_nTuV7PHpBfA0\",\"stnm\":\"浏河段\",\"accpw\":9296,\"accdw\":350,\"lgtd\":121.0820389,\"lttd\":31.42965183},{\"stcd\":\"S3_bi6YX0tyQq1L\",\"stnm\":\"市区吴淞江站\",\"accpw\":4229,\"accdw\":200,\"lgtd\":120.8895206,\"lttd\":31.31690797},{\"stcd\":\"S3_q2CEYa98JC9j\",\"stnm\":\"鳗鲤泾段\",\"accpw\":8652,\"accdw\":350,\"lgtd\":120.8442173,\"lttd\":31.44852067},{\"stcd\":\"S3_u679EzPpKExN\",\"stnm\":\"张家港河站\",\"accpw\":9265,\"accdw\":0,\"lgtd\":120.8561003,\"lttd\":31.49525899},{\"stcd\":\"S3_evBOomvD2vrj\",\"stnm\":\"新开泾站\",\"accpw\":5589,\"accdw\":160,\"lgtd\":120.8427537,\"lttd\":31.49263339},{\"stcd\":\"632E2452\",\"stnm\":\"野尤泾站\",\"accpw\":681,\"accdw\":20,\"lgtd\":120.844915,\"lttd\":31.410737},{\"stcd\":\"632D2460\",\"stnm\":\"长牵路段\",\"accpw\":14295,\"accdw\":0,\"lgtd\":120.841406,\"lttd\":31.188685},{\"stcd\":\"632DX001\",\"stnm\":\"大朱砂港桥段\",\"accpw\":0,\"accdw\":33749,\"lgtd\":121.001793,\"lttd\":31.171323},{\"stcd\":\"63205581\",\"stnm\":\"千灯浦闸段\",\"accpw\":0,\"accdw\":5842,\"lgtd\":121.031793,\"lttd\":31.231323},{\"stcd\":\"632DX002\",\"stnm\":\"花桥吴淞江大桥段\",\"accpw\":0,\"accdw\":14776,\"lgtd\":121.0820389,\"lttd\":31.42965183}]";
        ObjectMapper objectMapper = new ObjectMapper();
        mList =  objectMapper.readValue(strRes, new TypeReference<List<Map<String, Object>>>(){});


        if(null != mList && mList.size() > 0){
            // 本地图片 添加文字水印 ( 出入境水量 测试）
            addWatermark(1, 1, localPath,"",mList,saveFilePath,"D:\\log\\");
        }

        // 网络图片 添加文字水印
        //addWatermark(2, 1, networkPath, textWatermark);

        // 本地图片 添加图片水印
//        addWatermark(1, 2, localPath,pictureWatermark,null, saveFilePath);

        // 网络图片 添加图片水印
//        addWatermark(2, 2, networkPath, pictureWatermark);

    }

}
