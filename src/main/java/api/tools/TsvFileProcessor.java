package api.tools;

import api.data.swzzmxsq.ST_PPTN_R_QUData;
import api.data.swzzmxsq.ST_STBPRP_B_STCDData;
import api.pojo.swzzmxsq.ST_PPTN_R_QUPojo;
import api.pojo.swzzmxsq.ST_STBPRP_B_STCDPojo;
import api.pojo.swzzmxsq.TsvDataPojo;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TsvFileProcessor {
    @Autowired
    private static ST_STBPRP_B_STCDData st_stbprp_b_stcdData;
    @Autowired
    private static ST_PPTN_R_QUData data;

    // 方案1：基于行的分段处理
    public static List<TsvDataPojo> parseTsvFileInChunks(String filePath, int chunkSize) throws IOException, ParseException {
        List<TsvDataPojo> dataList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/M/d HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // 跳过标题行
            String line;
            int lineCount = 0;
            List<TsvDataPojo> chunk = new ArrayList<>(chunkSize);

            while ((line = br.readLine()) != null) {
                TsvDataPojo record = parseLine(line, dateFormat);
                if (record != null) {
                    chunk.add(record);
                    if (++lineCount % chunkSize == 0) {
                        processChunk(chunk); // 处理当前分块
                        dataList.addAll(chunk);
                        chunk.clear();
                    }
                }
            }
            // 处理剩余记录
            if (!chunk.isEmpty()) {
                processChunk(chunk);
                dataList.addAll(chunk);
            }
        }
        return dataList;
    }

    // 方案2：基于内存映射的高效处理
    public static void processLargeTsvWithMmap(String filePath) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r");
             FileChannel channel = raf.getChannel()) {

            long fileSize = channel.size();
            long position = 0;
            long chunkSize = 1024 * 1024 * 100; // 每100MB一个块

            while (position < fileSize) {
                long remaining = fileSize - position;
                long currentChunkSize = Math.min(chunkSize, remaining);

                MappedByteBuffer buffer = channel.map(
                        FileChannel.MapMode.READ_ONLY,
                        position,
                        currentChunkSize
                );

                processBuffer(buffer); // 处理当前内存映射块
                position += currentChunkSize;
            }
        }
    }

    private static TsvDataPojo parseLine(String line, SimpleDateFormat dateFormat) {
        String[] fields = line.split("\t");
        if (fields.length < 26) return null;

        TsvDataPojo record = new TsvDataPojo();
        try {
            record.setSenId(fields[0]);
            // 增强日期解析的健壮性
            try {
                record.setTime(dateFormat.parse(fields[1]));
            } catch (ParseException e) {
                try {
                // 尝试备用格式解析
                SimpleDateFormat fallbackFormat = new SimpleDateFormat("yyyy/M/d");
                record.setTime(fallbackFormat.parse(fields[1]));
                } catch (ParseException ex) {
                    System.err.println("解析行出错(时间转化出问题): " + line);
                }
            }

            double[] vValues = new double[12];
            for (int i = 0; i < 12; i++) {
                vValues[i] = fields[2 + i].isEmpty() ? 0 : Double.parseDouble(fields[2 + i]);
            }
            record.setVValues(vValues);

            int[] sValues = new int[12];
            for (int i = 0; i < 12; i++) {
                sValues[i] = fields[14 + i].isEmpty() ? 0 : Integer.parseInt(fields[14 + i]);
            }
            record.setSValues(sValues);

            record.setRowId(fields[26]);
            return record;
        } catch (Exception e) {
            System.err.println("解析行出错: " + line);
            return null;
        }
    }

    private static void processChunk(List<TsvDataPojo> chunk) {
        // 这里实现分块处理逻辑，如写入数据库或临时文件
        System.out.println("处理分块，大小: " + chunk.size());
        List<String> stcdList = new ArrayList<>();
        List<String> typeList = Arrays.asList("2".split(","));
        List<ST_STBPRP_B_STCDPojo> listStcd=st_stbprp_b_stcdData.selectList(stcdList,typeList,null);
        List<ST_PPTN_R_QUPojo> quPojo=new ArrayList<>();
        chunk.forEach(tsv->{
            double[] vValues=tsv.getVValues();
            Date time =tsv.getTime();
            List<ST_STBPRP_B_STCDPojo> listStcdT=listStcd.stream().filter(p->p.getZSTCD().equals(tsv.getSenId())).collect(toList());
            String stcd=listStcdT.size()>0?listStcdT.get(0).getSTCD():"";
            if(!stcd.equals("")){
                for(int v=0;v<vValues.length;v++){
                    ST_PPTN_R_QUPojo pojo=new ST_PPTN_R_QUPojo();
                    Date tm= new Date(time.getTime() + v * 5 * 60 * 1000);
                    String tmStr=DateUtil.dateFormat(tm,"yyyy-MM-dd HH:mm:ss"); // 增加分钟数
                    pojo.setSTCD(stcd);
                    pojo.setTM(tmStr);
                    pojo.setDRP(vValues[v]);
                    quPojo.add(pojo);
                }
            }
        });
        data.insertAll(quPojo);
    }

    private static void processBuffer(MappedByteBuffer buffer) {
        // 实现内存映射缓冲区的处理逻辑
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String content = new String(bytes);
        // 处理内容...
    }
}
