package api.tools;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;

/**
 * 生成验证码图片
 * */
public class CodeUtil {

    //Excel模板路径
    @Value("${file.path.templatefilepath}")
    private static String templatefilepath;

    private static int width = 120;// 定义图片的width
    private static int height = 45;// 定义图片的height
    private static int codeCount = 4;// 定义图片上显示验证码的个数

    private static int xx = 22;

    private static int fontHeight = 35;

    private static int codeY =35;

    private static char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',

            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',

            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',

            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',

            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    private static char[] codeSequenceNumber = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    /**
     生成一个map集合
     code为生成的验证码
     codePic为生成的验证码BufferedImage对象
     @return
     */
    public static Map<String,Object> generateCodeAndPic() {

        // 定义图像buffer

        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics gd = buffImg.getGraphics();

        // 创建一个随机数生成器类

        Random random = new Random();

        // 将图像填充为白色

        gd.setColor(Color.WHITE);

        gd.fillRect(0, 0, width, height);

        // 创建字体，字体的大小应该根据图片的高度来定。

        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);

        // 设置字体。

        gd.setFont(font);

        // 画边框。

        gd.setColor(Color.BLACK);

        gd.drawRect(0, 0, width - 1, height - 1);

        // 随机产生50条干扰线，使图象中的认证码不易被其它程序探测到。

        gd.setColor(Color.BLACK);

        for (int i = 0; i < 50; i++) {

            int x = random.nextInt(width);

            int y = random.nextInt(height);

            int xl = random.nextInt(12);

            int yl = random.nextInt(12);

            gd.drawLine(x, y, x + xl, y + yl);

        }

        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。

        StringBuffer randomCode = new StringBuffer();

        int red = 0, green = 0, blue = 0;

        // 随机产生codeCount数字的验证码。

        for (int i = 0; i < codeCount; i++) {

            // 得到随机产生的验证码数字。

            String code = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);

            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。

            red = random.nextInt(255);

            green = random.nextInt(255);

            blue = random.nextInt(255);

            // 用随机产生的颜色将验证码绘制到图像中。

            gd.setColor(new Color(red, green, blue));

            gd.drawString(code, (i + 1) * xx, codeY);

            // 将产生的四个随机数组合在一起。

            randomCode.append(code);

        }

        Map<String,Object> map =new HashMap<String,Object>();

        //存放验证码

        map.put("code", randomCode);

        //存放生成的验证码BufferedImage对象

        map.put("codePic", buffImg);

        return map;

     }

    public static String[] fuhao = { "+", "-", "*", "/" };
    public static int num = 0;
    public static String result = "";
    // 生成随机算式
    public static String random() {

        List list = new ArrayList();
        Random ran = new Random();
        int n1 = ran.nextInt(10);
        int n2 = ran.nextInt(10);
        String f = fuhao[ran.nextInt(fuhao.length)];
        StringBuilder sb = new StringBuilder(4);

        switch (f) {
            case "+":
                num = n1 + n2;
                break;
            case "-":
                num = n1 - n2;
                break;
            case "*":
                num = n1 * n2;
                break;
            case "/":
                if (n2 == 0) {
                    n2 = ran.nextInt(10) + 1;
                    num = n1 / n2;
                } else {
                    num = n1 / n2;
                }
                break;
        }
        sb.append(n1);
        sb.append(f);
        sb.append(n2);
        sb.append("=");

        result = num + "";
        return sb.toString();
    }
    public static void outputImage(String str, OutputStream os) {
        Random ran = new Random();
        // 创建图片对象
        BufferedImage img = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        // 获取画布
        Graphics g = img.getGraphics();
        // 设置背景
        g.setColor(Color.white);
        g.fillRect(0, 0, 100, 40);
        // 写字
        g.setColor(Color.black);
        g.setFont(new Font("微软雅黑", Font.PLAIN, 30));
        g.drawString(str, 10, 40);
        // 随机三条线
        g.drawLine(ran.nextInt(100), ran.nextInt(40), ran.nextInt(100), ran.nextInt(40));
        g.drawLine(ran.nextInt(100), ran.nextInt(40), ran.nextInt(100), ran.nextInt(40));
        g.drawLine(ran.nextInt(100), ran.nextInt(40), ran.nextInt(100), ran.nextInt(40));
        // 4. 输出结果
        // 常见的压缩格式：jpeg(有损压缩), png(无损压缩) ...
        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getRandomNumber(int lenCount){

        StringBuffer randomCode = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < lenCount; i++) {
            // 得到随机产生的验证码数字。
            String code = String.valueOf(codeSequenceNumber[random.nextInt(codeSequenceNumber.length)]);
            // 将产生的四个随机数组合在一起。
            randomCode.append(code);
        }
        return  randomCode.toString();
    }

    //测试

    public static void main(String[] args) throws Exception {

//        String filePath = "D://work//UploadDoc//CODE//";
//        String fileName = System.currentTimeMillis()+".jpg";
//        //创建文件输出流对象j
//        OutputStream out = new FileOutputStream( filePath+fileName);
//
//        System.out.println(filePath +fileName);
//        Map<String,Object> map = CodeUtil.generateCodeAndPic();
//
//        ImageIO.write((RenderedImage) map.get("codePic"), "jpeg", out);
//
//        String fileImgBase64 = FilePathUtils.convertToBase64(filePath+fileName);
////        System.out.println(fileImgBase64);
//
//        System.out.println("验证码的值为："+map.get("code"));

//        System.out.println(getRandomNumber(4));


        String accessToken = "";

        LocalDateTime localDate = LocalDateTime.now();
        //每小时存储一次access_token，存在直接读取，未存储，进行获取。
        String filePathName = DateUtil.strToLocalDateTime(localDate, "yyyyMMddHH") + ".log";
        //创建目录
        String outFilePath = FilePathUtils.getRealFilePath("D://work//UploadDoc//access//");
        FilePathUtils.isFileFolder(outFilePath);
        System.out.println("filePathName = "+ outFilePath + filePathName);
        //读取数据
        String res = FilePathUtils.readFileResult(outFilePath + filePathName);
        if(StringUtils.isNotEmpty(res)){
            accessToken  = res;
        }else{
            accessToken = WechatUtil.getAccessToken();
            FilePathUtils.createFileResult(outFilePath + filePathName,accessToken);
        }
    }
}
