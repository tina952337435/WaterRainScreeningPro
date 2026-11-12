package api.tools;

import api.pojo.swzzmxsq.ZrxDataPojo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
// 新增这行导入
import java.nio.charset.Charset;

/**
 * 读取结构化文本类型的 .zrx 文件
 * 适配特征：#开头注释行、字段定义行（timestamp,value）、数据行（20130101000000 -777）
 */
public class ZrxStructuredTextReader {
    // 定义时间格式化器（14位时间戳 -> yyyy-MM-dd HH:mm:ss）
    private static final DateTimeFormatter TIMESTAMP_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter TIMESTAMP_OUTPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 核心方法：读取.zrx文件并解析数据
     * @param filePath 文件路径
     * @return 解析后的数据集
     * @throws IOException 读取文件异常
     */
    public static List<ZrxDataPojo> readAndParseZrx(String filePath) throws IOException {
        List<ZrxDataPojo> dataList = new ArrayList<>();
        String sname = ""; // 存储从 SNAME|xxx 中提取的值

        // 字符流读取（GBK编码）
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), Charset.forName("GBK")))) {

            String line;
            boolean isDataLineStart = false; // 标记是否进入数据行区域

            while ((line = br.readLine()) != null) {
                // 跳过空行
                if (line.trim().isEmpty()) {
                    continue;
                }

                // 1. 处理注释行（#开头）
//                if (line.startsWith("#")) {
//                    System.out.println("注释信息：" + line);
//                    continue;
//                }

                // 2. 新增：解析 SNAME|xxx 格式（提取 | 右侧的值）
                if (line.startsWith("SNAME") || line.contains("SNAME")) {
                    // 分割 | 符号，取右侧内容（兼容 SNAME|xxx 或 其他前缀SNAME|xxx 的情况）
                    String[] snameParts = line.split("\\|"); // 分割为2部分，避免 | 后包含多个 | 的情况

                    for(int i=0;i<snameParts.length;i++){
                        String nameStr= snameParts[i];
                        if(nameStr.contains("SNAME")){
                            System.out.println("解析到nameStr：" + nameStr);
                            sname = nameStr.trim().replace("#SNAME","").replace("SNAME",""); // 提取 | 右侧的内容（去空格）
                            System.out.println("解析到SNAME：" + sname);
                            continue;
                        }
                    }

//                    if (snameParts.length == 2) {
//
//                        sname = snameParts[0].trim().replace("#SNAME",""); // 提取 | 右侧的内容（去空格）
//                        System.out.println("解析到line：" + line);
//                    } else {
//                        System.out.println("无效的SNAME格式（缺少|）：" + line);
//                    }

                    //临时****************************************************************
//                    ZrxDataPojo zrxDataPojo = new ZrxDataPojo();
//                    zrxDataPojo.setSname(sname); // 赋值解析到的SNAME值
//                    dataList.add(zrxDataPojo);
                    //临时****************************************************************
                    continue;
                }

                // 3. 处理字段定义行（如：timestamp,value）
                if (line.contains("timestamp,value") || line.contains("timestamp value")) {
                    isDataLineStart = true; // 后续行开始是数据行
                    System.out.println("数据字段定义：" + line);
                    continue;
                }

//                 4. 解析数据行（时间戳 数值），并赋值sname
                if (isDataLineStart) {
                    // 分割规则：兼容空格/逗号分隔，处理多个空格
                    String[] fields = line.trim().split("\\s+|,");
                    if (fields.length >= 2) {
                        String timestamp = fields[0].trim();
                        String value = fields[1].trim();
                        // 时间格式化：14位字符串 -> yyyy-MM-dd HH:mm:ss
                        String formattedTimestamp = formatTimestamp(timestamp);

                        ZrxDataPojo zrxDataPojo = new ZrxDataPojo();
                        zrxDataPojo.setTimestamp(formattedTimestamp);
                        zrxDataPojo.setValue(value);
                        zrxDataPojo.setSname(sname); // 赋值解析到的SNAME值
                        dataList.add(zrxDataPojo);
                    } else {
                        System.out.println("无效数据行（字段不足）：" + line);
                    }
                }
            }
        }
        return dataList;
    }

    /**
     * 核心方法：将14位时间戳字符串转为 yyyy-MM-dd HH:mm:ss 格式
     * @param rawTimestamp 原始时间戳（如20130101000000）
     * @return 格式化后的时间字符串，格式错误返回原始值并提示
     */
    private static String formatTimestamp(String rawTimestamp) {
        // 校验原始时间戳长度（必须14位）
        if (rawTimestamp == null || rawTimestamp.length() != 14) {
            System.out.println("无效的时间戳长度（需14位）：" + rawTimestamp);
            return rawTimestamp; // 格式错误时返回原始值，避免数据丢失
        }

        try {
            // 解析为LocalDateTime，再格式化为目标格式
            LocalDateTime dateTime = LocalDateTime.parse(rawTimestamp, TIMESTAMP_INPUT_FORMAT);
            return dateTime.format(TIMESTAMP_OUTPUT_FORMAT);
        } catch (DateTimeParseException e) {
            // 时间格式非法（如包含非数字字符）
            System.out.println("时间戳格式解析失败：" + rawTimestamp + "，错误信息：" + e.getMessage());
            return rawTimestamp; // 解析失败返回原始值
        }
    }
}