package api.tools;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.sound.sampled.AudioFileFormat;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 公共字段类
 */
@Component
@Data
public class MyVoiceTTSUtils {
    //调用外部 TTS 工具（如 eSpeak、Google TTS 等）可以通过 Java 调用这些工具生成语音文件。
    public static void MultiSpeek(String text,String outputFilePath){
        try {
            System.out.println("语音文件内容：" + text);
            // 调用 eSpeak 生成语音文件
            ProcessBuilder pb = new ProcessBuilder(
                    "espeak","-v", "zh", "-w", outputFilePath, zhongwentopinyin(text)
            );
            // 打印命令
            System.out.println("执行的命令: " + String.join(" ", pb.command()));

            Process process = pb.start();
            int exitCode=process.waitFor(); // 等待进程结束

            if (exitCode == 0) {
                System.out.println("语音文件已生成：" + outputFilePath);
            } else {
                System.out.println("语音文件生成失败，退出代码: " + exitCode);
            }        } catch (IOException | InterruptedException e) {
            System.out.println("语音文件生成报错：" + e.getMessage());
        }
    }
    //使用 Google TTS API（需要网络访问）
    public static void MultiGoogleTTS(String text,String outputFilePath){

        try {
            // 构造 Google TTS 请求 URL
            String ttsUrl = "https://translate.google.com/translate_tts?ie=UTF-8&q=" + text + "&tl=en&client=tw-ob";
            URL url = new URL(ttsUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // 读取响应并保存为文件
            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(new File(outputFilePath))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("语音文件已生成：" + outputFilePath);
        } catch (IOException e) {
//            e.printStackTrace();
        }

    }


    public static String zhongwentopinyin(String chinese ){
        String pinyinStr="";
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE); // 拼音小写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // 不带声调

        try {
            StringBuilder pinyin = new StringBuilder();
            for (char c : chinese.toCharArray()) {
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]")) { // 判断是否为中文字符
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (pinyinArray != null && pinyinArray.length > 0) {
                        pinyin.append(pinyinArray[0]); // 取第一个拼音（多音字默认取第一个）
                    }
                } else {
                    pinyin.append(c); // 非中文字符直接保留
                }
            }
            System.out.println(pinyin.toString()); // 输出: nihao,shijie
            pinyinStr=pinyin.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return pinyinStr;
    }
}
