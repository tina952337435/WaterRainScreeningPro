package api.tools;

import api.pojo.swzzmxsq.ST_STBPRP_B_STCDPojo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ZRX 雨量文件解析器（支持大文件流式读取）
 * 适用于：20年以上降雨数据，避免 Java heap space
 */
public class ZrxRainfallParser {

    // 时间格式化器：YYYYMMDDHHmmss -> LocalDateTime
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter TIMESTAMP_OUTPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 缺失值标记（常见于ZRX格式）
    private static final int MISSING_VALUE = -777;

    /**
     * 流式解析 .zrx 文件，逐行读取并处理
     * @param filePath zrx 文件路径
     * @param startTime 起始时间（可选，null表示不限制）
     * @param endTime   结束时间（可选，null表示不限制）
     * @return 解析后的雨量数据列表
     * @throws IOException 文件读取异常
     */
    public static List<RainfallRecord> parseZrxFile(String filePath, LocalDateTime startTime, LocalDateTime endTime, List<ST_STBPRP_B_STCDPojo> listStcd)
            throws IOException {

        List<RainfallRecord> records = new ArrayList<>();
        Path path = Paths.get(filePath);
        String sname = ""; // 存储从 SNAME|xxx 中提取的值
        try (BufferedReader reader = Files.newBufferedReader(path, java.nio.charset.Charset.forName("GBK"))) {// ← 指定编码
            String line;
            boolean headerRead = false; // 是否已读完头部信息

            while ((line = reader.readLine()) != null) {
                // 1. 忽略空行和纯注释行（以 # 开头的配置行）
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    // 1. 忽略空行和纯注释行（以 # 开头的配置行）
                    if (line.trim().isEmpty() || line.startsWith("#")) {
                        if (line.startsWith("SNAME") || line.contains("SNAME")) {
                            String[] snameParts = line.split("\\|"); // 分割为2部分，避免 | 后包含多个 | 的情况
                            for(int i=0;i<snameParts.length;i++){
                                String nameStr= snameParts[i];
                                if(nameStr.contains("SNAME")){
//                                System.out.println("解析到nameStr：" + nameStr);
                                    sname = nameStr.trim().replace("#SNAME","").replace("SNAME",""); // 提取 | 右侧的内容（去空格）
//                                System.out.println("解析到SNAME：" + sname);
                                    continue;
                                }
                            }
                        }
                        continue;
                    }
                    continue;
                }

                // 2. 解析数据行：timestamp value
                String[] parts = line.trim().split("\\s+");
                if (parts.length < 2) {
                    System.err.println("跳过无效数据行：" + line);
                    continue;
                }

                String timestampStr = parts[0];
                Double value = Double.valueOf(parts[1]);

                // 3. 时间转换
                LocalDateTime time = LocalDateTime.parse(timestampStr, FORMATTER);

                // 4. 时间范围过滤（可选）
                if (startTime != null && time.isBefore(startTime)) continue;
                if (endTime != null && time.isAfter(endTime)) continue;

                String tm=time.format(TIMESTAMP_OUTPUT_FORMAT);
                // 5. 过滤缺失值
                if (value == MISSING_VALUE) {
                    // 可选择忽略或保留（标记为缺测）
                    continue;
                }
                String finalSname = sname;
                List<ST_STBPRP_B_STCDPojo> listStcdT=listStcd.stream().filter(p->p.getSTNM().equals(finalSname)).collect(Collectors.toList());
                String stcd=listStcdT.size()>0?listStcdT.get(0).getZSTCD():null;
                // 6. 构建对象并添加
                RainfallRecord record = new RainfallRecord(tm, value,sname,stcd);
                records.add(record);

                // 🔍 可选：实时输出进度（调试用）
                // if (records.size() % 10000 == 0) {
                //     System.out.println("已处理 " + records.size() + " 条记录");
                // }
            }
        }

        return records;
    }

    /**
     * 简单的雨量记录类
     */
    public static class RainfallRecord {
        private final String timestamp;
        private final Double rainfallValue; // 单位：mm

        private final String stcd;//站码
        private final String stnm;//站名

        public RainfallRecord(String timestamp, Double rainfallValue,String stnm,String stcd) {
            this.timestamp = timestamp;
            this.rainfallValue = rainfallValue;
            this.stnm=stnm;
            this.stcd=stcd;
        }

        // Getter 方法
        public String getTimestamp() { return timestamp; }
        public Double getRainfallValue() { return rainfallValue; }
        public String getStnm() { return stnm; }
        public String getStcd() { return stcd; }

        @Override
        public String toString() {
            return String.format("%s: %.1f mm", timestamp, rainfallValue);
        }
    }


    /**
     * 导出雨量数据到 Excel 文件
     *
     * @param records     雨量记录列表
     * @param outputPath  输出文件路径，例如 "rainfall_output.xlsx"
     * @throws IOException 文件写入异常
     */
    public static void exportToExcel(List<ZrxRainfallParser.RainfallRecord> records, String outputPath) throws IOException {
        // 创建工作簿（.xlsx 格式）
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("雨量数据");

            // 创建表头样式（可选）
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // 创建表头行
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("时间");
            headerRow.createCell(1).setCellValue("雨量 (mm)");
            headerRow.createCell(2).setCellValue("站码");
            headerRow.createCell(3).setCellValue("站名");

            // 应用表头样式
            for (int i = 0; i < 4; i++) {
                headerRow.getCell(i).setCellStyle(headerStyle);
            }

            // 填充数据行
            int rowNum = 1;
            for (ZrxRainfallParser.RainfallRecord record : records) {
                Row row = sheet.createRow(rowNum++);
                String timeStr = record.getTimestamp();
                row.createCell(0).setCellValue(timeStr);
                row.createCell(1).setCellValue(record.getRainfallValue());
                row.createCell(2).setCellValue(record.getStcd());
                row.createCell(3).setCellValue(record.getStnm());
            }

            // 自动调整列宽（可选，提升可读性）
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            // 写入文件
            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                workbook.write(out);
            }
        }
    }


    public static void exportToCsv(List<ZrxRainfallParser.RainfallRecord> records, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "UTF-8"))) {
            // 写入表头
            writer.write("时间,雨量 (mm),站码,站名");
            writer.newLine();

            // 写入数据行
            for (ZrxRainfallParser.RainfallRecord record : records) {
                if(record.getStcd()!=null){

                    // 对字段进行 CSV 转义（处理包含逗号、换行符或引号的情况）
                    String timeStr = escapeCsvField(record.getTimestamp());
                    String rainfall = String.valueOf(record.getRainfallValue());
                    String stcd = escapeCsvField(record.getStcd());
                    String stnm = escapeCsvField(record.getStnm());

                    writer.write(String.join(",", timeStr, rainfall, stcd, stnm));
                    writer.newLine();
                }
            }
        }
    }

    // CSV 字段转义工具方法：如果字段包含逗号、双引号或换行符，则用双引号包裹，并转义内部双引号
    private static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}
