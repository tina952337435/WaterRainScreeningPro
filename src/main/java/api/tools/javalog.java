package api.tools;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class javalog {
    @Value("${file.path.templatefilepath}")
    private String filePathName;

    public void writelog(String logStr,String PathName){
        FileOutputStream outputStream = null;
        //获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 格式化当前日期
        String formattedDate = currentDate.format(formatter);
        try {
            if (filePathName==null){
                filePathName=PathName;
            }
            String fullPath=filePathName + "/logs/";
            // 创建文件对象
            File logFile = new File(fullPath);

            // 获取父目录
            File parentDir = logFile.getParentFile();

            // 如果父目录不存在，则创建父目录
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            outputStream = new FileOutputStream(new File(fullPath+"/WaterRainScreeningPro"+formattedDate+".txt"),true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            DateTimeFormatter formatterYMDHM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime currentDateLog = LocalDateTime.now();
            String formattedDateLog = currentDateLog.format(formatterYMDHM);
            logStr=  formattedDateLog+"："+logStr+"\n\t\r";
            //logStr=logStr+"\n";
            outputStream.write(logStr.getBytes());
            outputStream.flush();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("javalog写入日志转换文件报错："+e.getMessage());
        }
    }
}
