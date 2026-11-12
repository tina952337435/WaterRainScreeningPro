package api.tools;

//import sun.misc.BASE64Decoder;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @author lk
 * @Date 2020-08-31 16:27
 * @Description 文件路径替换
 */
public class FilePathUtils {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
//    public static final String FILE_SEPARATOR api/jsszy/viewWaterQ= File.separator;


    public static String getRealFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    public static String getHttpURLPath(String path) {
        return path.replace("\\", "/");
    }

    public static Boolean copyNio(String from, String to) {
        FileChannel input = null;
        FileChannel output = null;

        try {
            input = new FileInputStream(new File(from)).getChannel();
            output = new FileOutputStream(new File(to)).getChannel();
            output.transferFrom(input, 0, input.size());
            input.close();
            output.close();
        } catch (Exception e) {
            return false;
        } finally {
            return true;
        }
    }

    /**
     * base64转图片
     * @param base64str base64码
     * @param savePath 图片路径
     * @return
     */
    public static boolean GenerateImage(String base64str, String savePath) {
        //对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) {
            return false;
        }
//        BASE64Decoder decoder = new BASE64Decoder();
        Base64 base64encoder = new Base64();
//        return base64encoder.encodeBase64String(xxx);

        try {
            //Base64解码
//            byte[] b = decoder.decodeBuffer(base64str);
            byte[] b = base64encoder.decodeBase64(base64str);
            for (int i = 0; i < b.length; ++i) {
                //调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(savePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * base64转图片
     * @param base64Code base64码
     */
    public static void convertBase64ToImage(String base64Code, String savePath){
        BufferedImage image = null;
        byte[] imageByte = null;
        try {
            imageByte = DatatypeConverter.parseBase64Binary(base64Code);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(new ByteArrayInputStream(imageByte));
            bis.close();
            File outputfile = new File(savePath);
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertToBase64(String imagePath) throws IOException {
        InputStream imageInputStream = new FileInputStream(imagePath);
        byte[] imageBytes = new byte[imageInputStream.available()];
        imageInputStream.read(imageBytes);
        imageInputStream.close();

        return Base64.encodeBase64String(imageBytes);
    }

    public static void isFileFolder(String folderPath){

        File folder = new File(folderPath);
        if(!folder.exists()) {
            boolean success = folder.mkdirs();
            if (success) {
                System.out.println("文件夹创建成功");
            } else {
                System.out.println("文件夹创建失败");
            }
        }
    }

    /**
     * 文件读取
     * @param filePath 文件路径
     * */
    public static String readFileResult(String filePath){
        BufferedReader reader = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                StringBuffer tempString = new StringBuffer();
                String content;
                FileReader fileReader = new FileReader(file);
                reader = new BufferedReader(fileReader);
                while ((content = reader.readLine()) != null) {
                    tempString.append(content);
                }
                return tempString.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
      return "";
    }

    public static boolean createFileResult(String filePath, String content){
        FileOutputStream outputStream = null;
        try {
            File file = new File(filePath);
            outputStream = new FileOutputStream(file);
            byte[] bytes = content.getBytes();
            outputStream.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
