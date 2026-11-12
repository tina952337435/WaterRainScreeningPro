package api.tools;

import api.pojo.swzzmxsq.ST_PPTN_R_QUPojo;
import api.pojo.swzzmxsq.TsvDataPojo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TsvData {
     // 解析TSV文件的方法
     public static List<TsvDataPojo> parseTsvFile(String filePath,String source) throws IOException, ParseException {
         List<TsvDataPojo> dataList = new ArrayList<>();
         // 修改为更宽松的日期格式，支持单数字月份和日
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/M/d HH:mm:ss");
         try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
             // 跳过标题行
             br.readLine();

             String line;
             while ((line = br.readLine()) != null) {
                 String[] fields = line.split("\t");
                 if (fields.length < 26) continue;

                 TsvDataPojo record = new TsvDataPojo();
                 record.setSenId(fields[0]);

                 // 增强日期解析的健壮性
                 try {
                     record.setTime(dateFormat.parse(fields[1]));
                 } catch (ParseException e) {
                     try {
                         // 尝试备用格式解析
                         SimpleDateFormat fallbackFormat = new SimpleDateFormat("yyyy/M/d");
                         record.setTime(fallbackFormat.parse(fields[1]));
                     }catch (ParseException ex){
                         System.out.println("***********************转化时间报错：" +ex.getMessage());
                     }
                 }

//                 record.setTime(parseDate(fields[1]));

                 // 解析V0-V11
                 double[] vValues = new double[12];
                 for (int i = 0; i < 12; i++) {
                     vValues[i] = fields[2 + i].isEmpty() ? 0 : Double.parseDouble(fields[2 + i]);
                 }
                 record.setVValues(vValues);

                 // 解析S0-S11
                 int[] sValues = new int[12];
                 for (int i = 0; i < 12; i++) {
                     sValues[i] = fields[14 + i].isEmpty() ? 0 : Integer.parseInt(fields[14 + i]);
                 }
                 record.setSValues(sValues);

                 record.setRowId(fields[26]);
                 record.setSource(source);
                 dataList.add(record);
             }
         }
         return dataList;
     }

    public static List<ST_PPTN_R_QUPojo> parseTsvFileNew(String filePath) throws IOException, ParseException {
//        System.out.println("***********************开始执行parseTsvFileNew方法："+filePath);
        List<TsvDataPojo> dataList = new ArrayList<>();
        List<ST_PPTN_R_QUPojo> dataListNew = new ArrayList<>();
        // 修改为更宽松的日期格式，支持单数字月份和日
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/M/d HH:mm:ss");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // 跳过标题行
            String title=br.readLine();
            System.out.println("***********************标题行："+title);

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                if (fields.length < 26) continue;

//                TsvDataPojo record = new TsvDataPojo();
//                record.setSenId(fields[0]);

                // 增强日期解析的健壮性
                Date time;
                try {
                    time=dateFormat.parse(fields[1]);
//                    record.setTime(dateFormat.parse(fields[1]));
                } catch (ParseException e) {
                    // 尝试备用格式解析
                    SimpleDateFormat fallbackFormat = new SimpleDateFormat("yyyy/M/d");
//                    record.setTime(fallbackFormat.parse(fields[1]));
                    time=fallbackFormat.parse(fields[1]);
                }
                // 解析V0-V11
//                double[] vValues = new double[12];
                // 解析S0-S11
//                int[] sValues = new int[12];


               for (int i = 0; i < 12; i++) {
                   ST_PPTN_R_QUPojo pojo =new  ST_PPTN_R_QUPojo();
                   double drp=fields[2 + i].isEmpty() ? 0 : Double.parseDouble(fields[2 + i]);
                    pojo.setSTCD(fields[0]);
                    pojo.setDRP(drp);
                    Date tm= new Date(time.getTime() + i * 5 * 60 * 1000);
                    String tmStr=DateUtil.dateFormat(tm,"yyyy-MM-dd HH:mm:ss"); // 增加分钟数
                    pojo.setTM(tmStr);
                    dataListNew.add(pojo);
                }
//                record.setVValues(vValues);
//                record.setSValues(sValues);
//
//                record.setRowId(fields[26]);
//                dataList.add(record);
            }
        }
        return dataListNew;
    }


    // 尝试多种时间格式
    private static Date parseDate(String dateStr) throws ParseException {
        String[] formats = {
                "yyyy/M/d HH:mm:ss",
                "yyyy/M/d",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd"
        };

        for (String format : formats) {
            try {
                return new SimpleDateFormat(format).parse(dateStr);
            } catch (ParseException ignored) {
                // 尝试下一种格式
            }
        }
        throw new ParseException("无法解析日期: " + dateStr, 0);
    }
}
