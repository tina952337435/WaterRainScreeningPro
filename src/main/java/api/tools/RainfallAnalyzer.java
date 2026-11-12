package api.tools;

import api.pojo.swzzmxsq.RainfallResultPojo;
import api.pojo.swzzmxsq.ST_PPTN_RPojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
public class RainfallAnalyzer {
    // 定义日期格式（根据实际TM字符串格式调整，例如yyyy-MM-dd HH:mm:ss）
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 解析日期字符串为LocalDateTime，解析失败返回null
    private static LocalDateTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(timeStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("日期解析失败: " + timeStr + ", 错误: " + e.getMessage());
            return null;
        }
    }

    // 核心计算方法
    public static List<RainfallResultPojo> calculateYearlyMaxRainfall(List<ST_PPTN_RPojo> data, int min) {
        // 过滤有效数据（TM解析成功、DRP非空），并按站点+年份分组
        Map<String, Map<Integer, List<ST_PPTN_RPojo>>> groupedData = data.stream()
                .filter(d -> parseTime(d.getTM()) != null && d.getDRP() != null)
                .collect(Collectors.groupingBy(
                        ST_PPTN_RPojo::getSTCD,
                        Collectors.groupingBy(d -> parseTime(d.getTM()).getYear()) // 从解析后的时间中提取年份
                ));

        List<RainfallResultPojo> results = new ArrayList<>();

        // 遍历每个站点的分组数据
        for (Map.Entry<String, Map<Integer, List<ST_PPTN_RPojo>>> stationEntry : groupedData.entrySet()) {
            String stcd = stationEntry.getKey();
            Map<Integer, List<ST_PPTN_RPojo>> yearMap = stationEntry.getValue();

            // 遍历每个年份的数据
            for (Map.Entry<Integer, List<ST_PPTN_RPojo>> yearEntry : yearMap.entrySet()) {
                int year = yearEntry.getKey();
                List<ST_PPTN_RPojo> yearData = yearEntry.getValue();

                // 按时间排序（先解析TM为LocalDateTime，再排序）
                yearData.sort(Comparator.comparing(d -> parseTime(d.getTM())));

                double maxRainfall = 0;
                LocalDateTime startTime = null;
                LocalDateTime endTime = null;

                // 滑动窗口计算指定分钟内的累计雨量
                for (int i = 0; i < yearData.size(); i++) {
                    ST_PPTN_RPojo current = yearData.get(i);
                    LocalDateTime currentStart = parseTime(current.getTM());
                    if (currentStart == null) {
                        continue;
                    }
                    LocalDateTime currentEnd = currentStart.plusMinutes(min); // 计算窗口结束时间

                    double sum = 0;
                    for (int j = i; j < yearData.size(); j++) {
                        ST_PPTN_RPojo item = yearData.get(j);
                        LocalDateTime itemTime = parseTime(item.getTM());
                        if (itemTime == null || itemTime.isAfter(currentEnd)) {
                            break; // 超出窗口时间，停止累加
                        }
                        sum += item.getDRP(); // 累加雨量
                    }

                    // 更新最大雨量及对应时间
                    if (sum > maxRainfall) {
                        maxRainfall = sum;
                        startTime = currentStart;
                        endTime = currentEnd;
                    }
                }

                // 构建结果对象
                RainfallResultPojo result = new RainfallResultPojo();
                result.setSTCD(stcd);
                result.setYear(year);
                result.setMaxRainfall(maxRainfall);
                result.setStartTime(startTime.format( DATE_FORMATTER));
                result.setEndTime(endTime.format( DATE_FORMATTER));

                // 补充站点名称（取该年份第一条数据的STNM）
                if (!yearData.isEmpty()) {
                    result.setSTNM(yearData.get(0).getSTNM());
                }

                // 可选：将StartTime/EndTime转为字符串格式（stime/etime）
                if (startTime != null) {
                    result.setStime(startTime.format(DATE_FORMATTER));
                }
                if (endTime != null) {
                    result.setEtime(endTime.format(DATE_FORMATTER));
                }

                results.add(result);
            }
        }

        return results;
    }
}
