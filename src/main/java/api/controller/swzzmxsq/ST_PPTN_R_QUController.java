package api.controller.swzzmxsq;

import api.data.swzzmxsq.*;
import api.pojo.swzzmxsq.*;
import api.tools.*;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/ST_PPTN_R_QU")
public class ST_PPTN_R_QUController {
    @Autowired
    private ST_PPTN_R_QUData data;

    @Autowired
    private ST_STBPRP_B_STCDData  st_stbprp_b_stcdData;

    //文件存储路径
    @Value("${file.path.templatefilepath}")
    private String templatefilepath;

    @Autowired
    private CommonUtills commonUtills;

    @RequestMapping("/addAll")
    public Map<String,Object> addAll(@RequestBody List<ST_PPTN_R_QUPojo> quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.insertAll(quPojo);
        watch.stop();
        return commonUtills.returnJson("insert",integer,quPojo);
    }

    @RequestMapping("/modify")
    public Map<String, Object> upDateOne(@RequestBody ST_PPTN_R_QUPojo quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.upDateOne(quPojo);
        watch.stop();
        return commonUtills.returnJson("insert",integer,null);
    }

    @RequestMapping("/findResult")
    public Map<String,Object> selectList(@RequestBody ColumnName param){
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime=DateUtil.dateFormat(date,"yyyy-MM-dd 08:00:00"),etime="",type="Minute";
        List<String> stcdList = new ArrayList<>();
        if(null != param.getStcd()){
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if(null != param.getPathname()){
            type = param.getPathname();
        }
        if(null != param.getStime()){
            stime = param.getStime();
        }
        if(null != param.getEtime()){
            etime = param.getEtime();
        }
        List<ST_PPTN_R_QUPojo> pptnList = data.selectList(stcdList, stime, etime);
        if(null != pptnList){
            return commonUtills.returnJson("select",pptnList.size(),pptnList);
        }else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }

    //读取.tsv文件并入库
    @RequestMapping("/readTsv")
    public Map<String,Object> readTsv(@RequestBody ColumnName param){
        StopWatch watch = new StopWatch();
        watch.start();

        List<String> stcdList = new ArrayList<>();
        List<String> typeList =Arrays.asList("2".split(","));
        if(param.getPathname()!=null) {
            typeList = Arrays.asList(param.getPathname().split(","));
        }
        List<ST_STBPRP_B_STCDPojo> listStcd=st_stbprp_b_stcdData.selectList(stcdList,typeList,null);

       String folderPath= templatefilepath+"/区里面雨量数据/";
        // 示例：只列出.tsv文件
        FileFilter txtFilter = file -> file.getName().toLowerCase().endsWith(".tsv");
        List<File> txtFiles = FileListerWithFilter.getAllFiles(folderPath, txtFilter);

        System.out.println("找到 " + txtFiles.size() + " 个tsv文件:");

        AtomicReference<Integer> integer= new AtomicReference<>(0);
        txtFiles.forEach(file -> {
            List<ST_PPTN_R_QUPojo> quPojo=new ArrayList<>();
            String filename=file.getAbsolutePath();
//            int chunkSize = 1024 * 1024 * 100; // 每100MB一个块
//            try {
//                TsvFileProcessor.parseTsvFileInChunks(filename,chunkSize);
//            } catch (IOException e) {
//                System.out.println("读取文件引发IOException报错： " +e.getMessage());
//            } catch (ParseException e) {
//                System.out.println("读取文件引发ParseException报错： " +e.getMessage());
//            }
            try {
                System.out.println("***********************开始读取"+file.getName());
                String areaname=filename.replace("tsdb.tsv","").replace("TSDB.tsv","");
                List<TsvDataPojo> listTsv= TsvData.parseTsvFile(filename,areaname);
                System.out.println("***********************"+file.getName()+"文件读取数组长度： " + listTsv.size());

                //processLargeTsv(listTsv,listStcd);


//                listTsv.forEach(tsv->{
//                    double[] vValues=tsv.getVValues();
//                    Date time =tsv.getTime();
//                    List<ST_STBPRP_B_STCDPojo> listStcdT=listStcd.stream().filter(p->p.getZSTCD().equals(tsv.getSenId())).collect(toList());
//                    String stcd=listStcdT.size()>0?listStcdT.get(0).getSTCD():"";
//                    if(!stcd.equals("")){
//                        for(int v=0;v<vValues.length;v++){
//                            ST_PPTN_R_QUPojo pojo=new ST_PPTN_R_QUPojo();
//                            Date tm= new Date(time.getTime() + v * 5 * 60 * 1000);
//                            String tmStr=DateUtil.dateFormat(tm,"yyyy-MM-dd HH:mm:ss"); // 增加分钟数
//                            pojo.setSTCD(stcd);
//                            pojo.setTM(tmStr);
//                            pojo.setDRP(vValues[v]);
//                            quPojo.add(pojo);
//                        }
//                    }
//                });
            } catch (IOException e) {
                System.out.println("读取文件引发IOException报错： " +e.getMessage());
//                throw new RuntimeException(e);
            } catch (ParseException e) {
//                throw new RuntimeException(e);
                System.out.println("读取文件引发ParseException报错： " +e.getMessage());
            }
//            if(quPojo.size()>0){
////                integer.set(integer.get() + data.insertAll(quPojo));
//                int count = 2000;
//                int number = quPojo.size() / count;
//                if(quPojo.size() % count != 0){
//                    number = number + 1;
//                }
//                List<ST_PPTN_R_QUPojo> list = new ArrayList<>();
//                for(int i = 0;i < number;i++){
//                    if(i == number - 1){
//                        list = quPojo.subList(count * i,quPojo.size());
//                    }else {
//                        list = quPojo.subList(count * i,count * ( i + 1));
//                    }
//                    integer.set(integer.get() + data.insertAll(list));
//                }
//            }
        });
        watch.stop();
        return commonUtills.returnJson("insert", integer.get(),null);
    }
    //读取.tsv文件并入库
    @RequestMapping("/readTsvSing")
    public Map<String,Object> readTsvSing(@RequestBody ColumnName param){
        StopWatch watch = new StopWatch();
        watch.start();
        String folderPath= templatefilepath+"/区里面雨量数据/";
        if(param.getPathname()!=null) {
            String filename=param.getPathname();
            System.out.println("***********************开始读取"+filename);
            new javalog().writelog("**********开始读取"+filename,templatefilepath);
            try {
                List<String> typeList =Arrays.asList("2".split(","));
                List<String> stcdList = new ArrayList<>();
                List<ST_STBPRP_B_STCDPojo> listStcd=st_stbprp_b_stcdData.selectList(stcdList,typeList,null);
                String areaname=filename.replace("tsdb.tsv","").replace("TSDB.tsv","");
                List<TsvDataPojo> listTsv= TsvData.parseTsvFile(folderPath+filename,areaname);
                new javalog().writelog("**********"+filename+"数据量：***********"+listTsv.size(),templatefilepath);

                List<TsvDataPojo> llistTsvT=listTsv.stream().filter(p->p.getTime()==null).collect(toList());
                new javalog().writelog("**********"+filename+"时间为空的数组长度：***********"+llistTsvT.size(),templatefilepath);

                List<TsvDataPojo> llistTsvT2=listTsv.stream().filter(p->p.getSenId()==null).collect(toList());
                new javalog().writelog("**********"+filename+"站码为空的数组长度：***********"+llistTsvT.size(),templatefilepath);

                // 按站点分组
                Map<String, List<TsvDataPojo>> groupedByDay = listTsv.stream()
                        .collect(Collectors.groupingBy(
                                p -> {
                                  return  p.getSenId();
                                }));
//                String zstcds="";
                StringBuilder zstcds=new StringBuilder();
                groupedByDay.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> {
                                    zstcds.append(entry.getKey()+",");
                        }
                        );

                new javalog().writelog("**********"+filename+"关联的站码**********"+zstcds,templatefilepath);

                processLargeTsv(listTsv,listStcd);





//                List<ST_PPTN_R_QUPojo> quPojo= TsvData.parseTsvFileNew(folderPath+filename);
//                System.out.println("***********************"+filename+"文件读取数组长度： " + quPojo.size());
//                new javalog().writelog("***********************"+filename+"文件读取数组长度： " + quPojo.size(),templatefilepath);
//                if(quPojo.size()>0){
//                    int count = 50;
//                    int number = quPojo.size() / count;
//                    if(quPojo.size() % count != 0){
//                        number = number + 1;
//                    }
//                    List<ST_PPTN_R_QUPojo> list = new ArrayList<>();
//                    for(int i = 0;i < number;i++){
//                        if(i == number - 1){
//                            list = quPojo.subList(count * i,quPojo.size());
//                        }else {
//                            list = quPojo.subList(count * i,count * ( i + 1));
//                        }
//                        data.insertAll(list);
//                    }
           // }
            } catch (IOException e) {
                System.out.println("读取文件引发IOException报错： " +e.getMessage());
                new javalog().writelog("读取文件引发IOException报错： " +e.getMessage(),templatefilepath);
            } catch (ParseException e) {
                System.out.println("读取文件引发ParseException报错： " +e.getMessage());
                new javalog().writelog("读取文件引发ParseException报错： " +e.getMessage(),templatefilepath);
            }
        }
        watch.stop();
        return commonUtills.returnJson("insert",0,null);
    }

    public  void processLargeTsv(List<TsvDataPojo> listTsv, List<ST_STBPRP_B_STCDPojo> listStcd) {
        int BATCH_SIZE=5000;
        int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // 分批处理
        int totalSize = listTsv.size();
        for (int i = 0; i < totalSize; i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, totalSize);
            List<TsvDataPojo> batch = listTsv.subList(i, end);

            // 提交任务到线程池
            executor.submit(() -> processBatch(batch, listStcd));
        }

        // 关闭线程池
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private  void processBatch(List<TsvDataPojo> batch, List<ST_STBPRP_B_STCDPojo> listStcd) {
        List<ST_PPTN_R_QUPojo> batchResults = new ArrayList<>(batch.size() * 12);

        for (TsvDataPojo tsv : batch) {
            double[] vValues = tsv.getVValues();
            Date time = tsv.getTime();

//            List<ST_STBPRP_B_STCDPojo> listStcdT = listStcd.stream()
//                    .filter(p -> p.getZSTCD().equals(tsv.getSenId()))
//                    .collect(Collectors.toList());
//
//            String stcd = listStcdT.isEmpty() ? "" : listStcdT.get(0).getSTCD();
            String stcd=tsv.getSenId();
//            if (!stcd.isEmpty()) {
                for (int v = 0; v < vValues.length; v++) {
                    ST_PPTN_R_QUPojo pojo = new ST_PPTN_R_QUPojo();
                    Date tm = new Date(time.getTime() + v * 5 * 60 * 1000);
                    String tmStr = DateUtil.dateFormat(tm, "yyyy-MM-dd HH:mm:ss");

                    pojo.setSTCD(stcd);
                    pojo.setTM(tmStr);
                    pojo.setDRP(vValues[v]);
                    pojo.setSOURCE(tsv.getSource());
                    batchResults.add(pojo);
                }
            //}
        }

        // 批量保存结果到数据库
        batchInsertPrecipitationData(batchResults);
    }

    private int batchInsertPrecipitationData(List<ST_PPTN_R_QUPojo> quPojo){
        AtomicReference<Integer> integer= new AtomicReference<>(0);
        if(quPojo.size()>0){
            int count = 80;
            int number = quPojo.size() / count;
            if(quPojo.size() % count != 0){
                number = number + 1;
            }
            List<ST_PPTN_R_QUPojo> list = new ArrayList<>();
            for(int i = 0;i < number;i++){
                if(i == number - 1){
                    list = quPojo.subList(count * i,quPojo.size());
                }else {
                    list = quPojo.subList(count * i,count * ( i + 1));
                }
                integer.set(integer.get() + data.insertAll(list));
            }
        }
        return integer.get();
    }


    //*************************************多线程

    //*************************************多线程
}