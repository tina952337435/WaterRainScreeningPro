package api.tools;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFilesUtils {

    @Value("${file.path.templatefilepath}")
//    private static String templatefilepath;
    private static String templatefilepath = "D://work//ks//UploadDoc//";

    public static void main(String[] args) {
        String line1 = "第一行内容";
        String line2 = "第二行内容";

        String val = line1+"\t"+line2+"\n"+line1+"\t"+line2+"\n"+line1+"\t"+line2+"\n";
        WriteToTxt(templatefilepath+"","1.txt",val);

        val = line1+"\t"+line2+"\n"+line1+"\t"+line2+"\n"+line1+"\t"+line2+"\n";
        val += line1+"\t"+line2+"\n"+line1+"\t"+line2+"\n"+line1+"\t"+line2+"\n";
        WriteToTxt(templatefilepath+"","1.txt",val);

//        if(WriteToTxt(templatefilepath+"rain//outin//","1.txt",val)) {
//            String outZipFilePath = templatefilepath + "rain//output//";
//            ExeclZipFiles(templatefilepath+"rain//outin//",outZipFilePath,"雨量批量导出.zip");
//        }
    }
    /**
     * sourceFolder  txt文件所在目录路径
     * outZipFilePath  输出的压缩文件路径
     * filename 输出的压缩文件名称
     * **/
    public static String ExeclZipFiles(String sourceFolder,String outZipFilePath,String filename) {
        String zipFilePath = outZipFilePath + "" + filename; // 输出的压缩文件路径及名称

        File folder = new File(sourceFolder);
        if (!folder.exists()) {
            System.out.println("源文件夹不存在！");
            return "";
        }

        try {
            BufferedInputStream bis = null;

            BufferedOutputStream bos = null;
            FileOutputStream fos = new FileOutputStream(zipFilePath);

            byte[] buffer = new byte[1024];
            int bytesRead;

            ZipOutputStream zos = new ZipOutputStream(fos);
            for (File file : folder.listFiles()) {
                if (file.isDirectory() || !file.getName().endsWith(".txt")) continue;

                bis = new BufferedInputStream(new FileInputStream(file));
                zos.putNextEntry(new ZipEntry(file.getName()));

                while ((bytesRead = bis.read(buffer)) != -1) {
                    zos.write(buffer, 0, bytesRead);
                }

                zos.closeEntry();
                bis.close();
            }

            zos.finish();
            zos.flush();
            zos.close();

            System.out.println("成功创建并保存了压缩文件：" + zipFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        //删除目录下所有已生成临时 .txt 文件
        deleteFiles(folder);

        return filename;
    }

    /*
    * writeFilePath  路径
    * fileName 文件名称
    * strRes 内容
    * */
    public static boolean WriteToTxt(String writeFilePath,String fileName,String strRes){
        try {
            String filePath = writeFilePath;

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath+fileName,true));

            writer.write(strRes);
            writer.newLine(); // 换行
            writer.flush(); // 确保所有内容都被写入文件

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void deleteFiles(File folder) {
        try {
            if (folder.exists()) {
                FileUtils.cleanDirectory(folder); // 使用FileUtils类的cleanDirectory方法删除文件夹下的所有文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
