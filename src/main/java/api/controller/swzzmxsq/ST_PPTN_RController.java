package api.controller.swzzmxsq;

import Equivalent.MyDZXUtils;
import Equivalent.ObjUtils;
import api.data.swzzmxsq.*;
import api.data.swzzzjk.RTEVData;
import api.data.swzzzjk.TSDBData;
import api.pojo.DENGZHIXIANYQPojo;
import api.pojo.swzzmxsq.*;
import api.pojo.swzzzjk.RTEVPojo;
import api.pojo.swzzzjk.RTSQPojo;
import api.pojo.swzzzjk.TSDBPojo;
import api.server.swzzmxsq.ST_PPTN_RServer;
import api.tools.*;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import com.alibaba.fastjson.JSONObject;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/ST_PPTN_R")
public class ST_PPTN_RController {
    @Autowired
    private ST_PPTN_RData data;

    @Autowired
    private ST_PPTN_R_LOGData st_pptn_r_logData;

    @Autowired
    private ST_STBPRP_BData stbprpBData;
    @Autowired
    private ST_PPTN_RServer pptnServer;

    @Autowired
    private ST_STBPRP_B_STCDData  st_stbprp_b_stcdData;

    @Autowired
    private RTEVData rtevData;

    @Autowired
    private ST_SCREENINGRULESData st_screeningrulesData;

    @Autowired
    private  ST_STBPRP_B_DISTANCEData st_stbprp_b_distanceData;

    @Autowired
    private ST_PPTN_R_ABData st_pptn_r_abData;

    @Autowired
    private  TSDBData tsdbData;

    @Autowired
    private ST_PSTAT_RData  st_pstat_rData;

    //文件存储路径
    @Value("${file.path.templatefilepath}")
    private String templatefilepath;

    @Autowired
    private CommonUtills commonUtills;

    @RequestMapping("/addAll")
    public Map<String,Object> addAll(@RequestBody List<ST_PPTN_RPojo> quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.insertAll(quPojo);
        watch.stop();
        return commonUtills.returnJson("insert",integer,quPojo);
    }

    @RequestMapping("/modify")
    public Map<String, Object> upDateOne(@RequestBody ST_PPTN_RPojo quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.upDateOne(quPojo);
        watch.stop();
        return commonUtills.returnJson("insert",integer,null);
    }
    @RequestMapping("/modifyAll")
    public Map<String,Object>  modifyAll(@RequestBody List<ST_PPTN_RPojo> quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer num = 0;
        int count = 80;
        int number = quPojo.size() / count;
        if (quPojo.size() % count != 0){
            number += 1;
        }
        List<ST_PPTN_RPojo> list = new ArrayList<>();
        for(int i=0;i<number;i++){
            if(i == number - 1){
                list = quPojo.subList(count * i,quPojo.size());
            }else {
                list = quPojo.subList(count * i,count * (i + 1));
            }
            num += data.updateALL(list);
        }
        watch.stop();
        return commonUtills.returnJson("insert",num,quPojo);
    }

    @RequestMapping("/selectListDanZhan")
    public Map<String,Object> selectListDanZhan(@RequestBody ColumnName param){
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
        List<ST_PPTN_RPojo> pptnList = new ArrayList<>();
        if("Minute".equals(type)){
             pptnList = data.selectListByTime(stcdList, stime, etime);
        }else if("HOUR".equals(type)){
//            pptnList = data.selectListDanZhanByHouse(stcdList,stime,etime);
            pptnList = pptnServer.selectListByHour(stcdList,stime,etime);
        }else if("DAY".equals(type)){
            pptnList = pptnServer.selectListByDay(stcdList,stime,etime);
        }
        List<ST_STBPRP_BPojo> bPojos = stbprpBData.selectList(stcdList, null,null);
        for(ST_PPTN_RPojo pptnRPojo : pptnList){
            List<ST_STBPRP_BPojo> collect = bPojos.stream().filter(i -> {
                if(null != i.getSTCD() && null != pptnRPojo.getSTCD()){
                    return i.getSTCD().equals(pptnRPojo.getSTCD());
                }
                return false;
            }).collect(toList());
            if(collect.size() > 0){
                ST_STBPRP_BPojo stb = collect.get(0);
                if(stb.getSTNM() != null){
                    pptnRPojo.setSTNM(stb.getSTNM());
                }
                if(stb.getADMAUTH() != null){
                    pptnRPojo.setADMAUTH(stb.getADMAUTH());
                }
            }
            if("HOUR".equals(type)){
                if("" != pptnRPojo.getTM() && pptnRPojo.getTM().length() < 16 ){
                    pptnRPojo.setTM(pptnRPojo.getTM()+":00:00");
                }
            }
        }
        if(null != pptnList){
            return commonUtills.returnJson("select",pptnList.size(),pptnList);
        }else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }

    @RequestMapping("/selectListIsLast")
    public Map<String,Object> selectListIsLast(@RequestBody ColumnName param){
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime="",//DateUtil.dateFormat(date,"yyyy-MM-dd 08:00:00"),
                etime="",type="Minute";
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
        List<ST_PPTN_RPojo> pptnList = new ArrayList<>();
        pptnList = data.selectListIsLast(stcdList, stime, etime);
        if(null != pptnList){
            return commonUtills.returnJson("select",pptnList.size(),pptnList);
        }else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }


    @RequestMapping("/selectListIsDay")
    public Map<String,Object> selectListIsDay(@RequestBody ColumnName param){
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime= DateUtil.dateFormat(date,"yyyy-MM-dd HH:mm:ss"),etime="",PID="",ADMAUTH="",STNM="";
        List<String> stcdList = new ArrayList<>();
        if(null != param.getPid()){
            PID = param.getPid();
        }
        if(null != param.getStime()){
            stime = param.getStime();
        }
        if(null != param.getEtime()){
            etime = param.getEtime();
        }
        if(null != param.getKey()){
            STNM = param.getKey();
        }
        if(null != param.getDatasource()){
            ADMAUTH = param.getDatasource();
        }
        if(param.getStcd()!=null){
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        //查询累计降雨
        List<ST_PPTN_RPojo> sprlist = data.selectListIsDay(stcdList,stime, etime);
        //查询基础站码信息
        List<ST_STBPRP_BPojo> stStbprpBPList = stbprpBData.selectList(stcdList, null,null);
        if(param.getStcd()==null){
            stcdList=stStbprpBPList.stream()
                    .map(ST_STBPRP_BPojo::getSTCD)
                    .filter(stcd -> stcd != null) // 过滤掉 null
                    .collect(Collectors.toList());
        }
        List<ST_PPTN_RPojo> mList = new ArrayList<>();
        if(stcdList.size() > 0){
            for(String  item : stcdList){
                List<ST_STBPRP_BPojo> collect = stStbprpBPList.stream().filter(i -> i.getSTCD().equals(item)).collect(toList());
                if(collect.size() > 0){
                    ST_PPTN_RPojo model = new ST_PPTN_RPojo();

                    ST_STBPRP_BPojo stbprpBPojo = collect.get(0);
                    model.setSTCD(item);

                    if(null != stbprpBPojo.getSTNM()){
                        model.setSTNM(stbprpBPojo.getSTNM());
                    }
                    if(null != stbprpBPojo.getADMAUTH()){
                        model.setADMAUTH(stbprpBPojo.getADMAUTH());
                    }
                    if(null != stbprpBPojo.getRVNM()){
                        model.setRVNM(stbprpBPojo.getRVNM());
                    }
                    if(null != stbprpBPojo.getBSNM()){
                        model.setBSNM(stbprpBPojo.getBSNM());
                    }
                    if(null != stbprpBPojo.getLGTD()){
                        model.setLGTD(stbprpBPojo.getLGTD());
                    }
                    if(null != stbprpBPojo.getLTTD()){
                        model.setLTTD(stbprpBPojo.getLTTD());
                    }
                    List<ST_PPTN_RPojo> drpList = sprlist.stream().filter(i -> i.getSTCD().equals(item)).collect(toList());
                    if(drpList.size() > 0) {
                        model.setDRP(Double.parseDouble( String.format("%.1f",drpList.get(0).getDRP())));
                        if(null != drpList.get(0).getTM())
                            model.setTM(drpList.get(0).getTM());
                    }
                    mList.add(model);
                }
            }
        }
//        if(null != mList && mList.size() > 0) {
//            mList.sort((a, b) -> {
//                if (a.getDRP() != null && b.getDRP() != null) {
//                    return (int) (b.getDRP() * 1000 - a.getDRP() * 1000);
//                }
//                return -1;
//            });
//        }
//        return mList;
        if(null != mList){
            return commonUtills.returnJson("select",mList.size(),mList);
        }else {
            return commonUtills.returnJson("select", -1, mList);
        }
    }



    //多站雨情
    @RequestMapping("/queryByYLDuoZhan")
    public JSONObject queryByYLDuoZhan(@RequestBody ColumnName param){
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime=DateUtil.dateFormat(date,"yyyy-MM-dd HH:mm:ss"),etime=DateUtil.dateFormat(new Date(),"yyyy-MM-dd HH:mm:ss"),id="Minute";
        List<String> stcdList = new ArrayList<>();
        String mtype="";
        if(null != param.getPathname()){
            id = param.getPathname();
        }
        if(null != param.getStcd()){
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if(null != param.getStime()){
            stime = param.getStime();
        }
        if(null != param.getEtime()){
            etime = param.getEtime();
        }
        if(null != param.getDatasource()){
            mtype = param.getDatasource();
        }
        List<ST_STBPRP_BPojo> stStbprpBList = stbprpBData.selectList(stcdList, null,null);
        List<ST_PPTN_RPojo> list = data.selectLists(stcdList, stime, etime);

        Map<String, Object> map = new HashMap<>();
        List<String> stcdListnm=new ArrayList<>();
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(stStbprpBList.size()>0){
            for(int numII=0;numII<stStbprpBList.size();numII++) {
                String stcdStnm=stStbprpBList.get(numII).getSTCD()+":"+stStbprpBList.get(numII).getSTNM();
                stcdListnm.add(stcdStnm);
            }
        }

        if (null != list && list.size() > 0) {
            List<List<ST_PPTN_RPojo>> waterGroupList = new ArrayList<>();
            list.sort(Comparator.comparing(ST_PPTN_RPojo::getTM));
            list.stream().collect(Collectors.groupingBy(ST_PPTN_RPojo::getTM, LinkedHashMap::new, Collectors.toList())).forEach((tm, fooListByTm) -> {
                waterGroupList.add(fooListByTm);
            });
            if (null != waterGroupList && waterGroupList.size() > 0) {
                List<String> finalStcdList = stcdList;
                List<ST_PPTN_RPojo> finalList = list;
                waterGroupList.forEach((mWaterList) ->{
                    if(mWaterList.size() > 0){
                        String finaTime = mWaterList.get(0).getTM();
                        Map<String, Object> strWhere = new HashMap<>();
                        strWhere.put("tm",finaTime);
                        for(int numII = 0; numII< finalStcdList.size(); numII++){
                            String _stcd=finalStcdList.get(numII);
                            List<ST_PPTN_RPojo> finalListTemp= finalList.stream().filter(p->DateUtil.strToDate(p.getTM(),DateUtil.YMDHMS).getTime()==DateUtil.strToDate(finaTime,DateUtil.YMDHMS).getTime()&&p.getSTCD().equals(_stcd)).collect(Collectors.toList());
                            if(finalListTemp.size()>0){
                                ST_PPTN_RPojo itemList=finalListTemp.get(0);
                                if(!CommonUtills.isEmpty(itemList.getDRP())){
                                    Double strVal=Double.valueOf(itemList.getDRP());
                                    strWhere.put(_stcd+"drp",strVal);
                                }else{
                                    strWhere.put(_stcd+"drp",0.00);
                                }
                            }
                        }
                        mapList.add(strWhere);
                    }
                });
            }
        }
        map.put("listSTCD",stcdListnm);
        map.put("list",mapList);
        JSONObject json=new JSONObject(map);
        return json;
    }


    @RequestMapping(value = "/dengzhixian",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> dengzhixian(@RequestBody DENGZHIXIANYQPojo param){
        StopWatch watch = new StopWatch();
        // 计时器开始
        watch.start();
        List<String> listSTCD= new ArrayList<>();
        String filePath=templatefilepath;
        filePath = FilePathUtils.getRealFilePath(templatefilepath+"Rainfall\\");
        File savefile = new File(filePath);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }

        String filename = "dzx"+UUID.randomUUID().toString()+".png";
        MyDZXUtils dzxUtils=new MyDZXUtils();
        dzxUtils.bijiao_x=0.1;
        String filenameNew = dzxUtils.init(param.getLEVELDATA(),"",FilePathUtils.getRealFilePath(filePath+"/"+filename),param.getLEVELRANGE());

        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        if(!ObjUtils.isEmpty(filenameNew)){
            map.put("filename",filename);
        }
        list.add(map);
        //计时器结束
        watch.stop();

        return list;
    }

    //整理南瑞5分钟数据入库
    @RequestMapping("/arrangeMinutedata")
    public Map<String, Object> arrangeMinutedata(@RequestBody ColumnName param){
        StopWatch watch = new StopWatch();
        watch.start();
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime=DateUtil.dateFormat(date,"yyyy-MM-dd 08:00:00"),etime="";
        Integer integer=0;
        String pathname="",datasource="2";
        List<String> stcdList = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        if(null != param.getStcd()){
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if(param.getDatasource()!=null){
            datasource=param.getDatasource();
            typeList=Arrays.asList(param.getDatasource().split(","));
        }
        if(param.getPathname()!=null){
            pathname=param.getPathname();
        }
        if(null != param.getStime()){
            stime = param.getStime();
        }
        if(null != param.getEtime()){
            etime = param.getEtime();
        }
        if(pathname=="all"){//整理所有站的数据
            stcdList=null;
        }
        List<ST_STBPRP_B_STCDPojo> listStcd=st_stbprp_b_stcdData.selectList(stcdList,typeList,null);
        List<String> zstcdList=listStcd.stream()
                .map(ST_STBPRP_B_STCDPojo::getZSTCD)
                .filter(zstcd -> zstcd != null) // 过滤掉 null
                .collect(Collectors.toList());
        List<RTEVPojo> listRtev=rtevData.selectList(zstcdList,stime,etime);
        System.out.println("listRtev长度："+listRtev.size());
        List<ST_PPTN_RPojo> listData=new ArrayList<>();
        if(listRtev.size()>0){
            listStcd.forEach(item->{
                List<RTEVPojo> listRtevTemp=listRtev.stream().filter(u->u.getSenid().equals(item.getZSTCD())).collect(toList());
                for (int num=0;num<listRtevTemp.size();num++){
                    ST_PPTN_RPojo pojo=new ST_PPTN_RPojo();
                    pojo.setSTCD(item.getSTCD());
                    pojo.setTM(listRtevTemp.get(num).getTime());
                    double drp=0;
                    if(num==0){
//                        drp=listRtevTemp.get(num).getFactv();
                    }else{
                        double adrp=listRtevTemp.get(num-1).getFactv();
                        double cdrp=listRtevTemp.get(num).getFactv();
//                        if(num<listRtevTemp.size()-1){
//                            //处理异常值：当前值与前后两个值相差50，取前后的两个数值的平均
//                            double bdrp=listRtevTemp.get(num+1).getFactv();
//                            drp=cdrp-adrp;//南瑞库，雨量是个累加值
//                            if(Math.abs(drp)>50){
//                                drp=(adrp+bdrp)/2;
//                            }else if(adrp==0&&bdrp==0){//前后都是0的情况
//                                drp=0;
//                            }
//                        }
//                        else{
                            drp=cdrp-adrp;//南瑞库，雨量是个累加值
                        //}
                        String formattedValue= String.format("%.2f", drp);
                        drp = Double.parseDouble(formattedValue);
                        pojo.setDRP(drp);
                        listData.add(pojo);
                    }
                }
            });
        }
        if(listData.size()>0){
            int count = 200;
            int number = listData.size() / count;
            if(listData.size() % count != 0){
                number = number + 1;
            }
            List<ST_PPTN_RPojo> list = new ArrayList<>();
            for(int i = 0;i < number;i++){
                if(i == number - 1){
                    list = listData.subList(count * i,listData.size());
                }else {
                    list = listData.subList(count * i,count * ( i + 1));
                }
                integer += data.insertAll(list);
            }
        }
        watch.stop();
        return commonUtills.returnJson("insert",integer,listData);
    }


    //整理南瑞5分钟数据入库：修正后的
    @RequestMapping("/arrangeMinutedataTsdb")
    public Map<String, Object> arrangeMinutedataTsdb(@RequestBody ColumnName param){
        StopWatch watch = new StopWatch();
        watch.start();
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime=DateUtil.dateFormat(date,"yyyy-MM-dd 08:00:00"),etime="";
        Integer integer=0;
        String pathname="",datasource="2";
        List<String> stcdList = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        if(null != param.getStcd()){
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if(param.getDatasource()!=null){
            datasource=param.getDatasource();
            typeList=Arrays.asList(param.getDatasource().split(","));
        }
        if(param.getPathname()!=null){
            pathname=param.getPathname();
        }
        if(null != param.getStime()){
            stime = param.getStime();
        }
        if(null != param.getEtime()){
            etime = param.getEtime();
        }
        if(pathname=="all"){//整理所有站的数据
            stcdList=null;
        }
        List<ST_STBPRP_B_STCDPojo> listStcd=st_stbprp_b_stcdData.selectList(stcdList,typeList,null);
        List<String> zstcdList=listStcd.stream()
                .map(ST_STBPRP_B_STCDPojo::getZSTCD)
                .filter(zstcd -> zstcd != null) // 过滤掉 null
                .collect(Collectors.toList());
        List<RTSQPojo> listTsdb=getRTSQ_5MINXZYL(zstcdList,stime,etime);
        System.out.println("listTsdb长度："+listTsdb.size());
        List<ST_PPTN_RPojo> listData=new ArrayList<>();
        if(listTsdb.size()>0){
            listStcd.forEach(item->{
                List<RTSQPojo> listTsdbTemp=listTsdb.stream().filter(u->u.getSENID().equals(item.getZSTCD())).collect(toList());
                for (int num=0;num<listTsdbTemp.size();num++){
                    ST_PPTN_RPojo pojo=new ST_PPTN_RPojo();
                    pojo.setSTCD(item.getSTCD());
                    pojo.setTM(listTsdbTemp.get(num).getTIME());
                    double drp=listTsdbTemp.get(num).getFACTV();
                    String formattedValue= String.format("%.2f", drp);
                    drp = Double.parseDouble(formattedValue);
                    pojo.setDRP(drp);
                    listData.add(pojo);
                }
            });
        }
        if(listData.size()>0){
            int count = 200;
            int number = listData.size() / count;
            if(listData.size() % count != 0){
                number = number + 1;
            }
            List<ST_PPTN_RPojo> list = new ArrayList<>();
            for(int i = 0;i < number;i++){
                if(i == number - 1){
                    list = listData.subList(count * i,listData.size());
                }else {
                    list = listData.subList(count * i,count * ( i + 1));
                }
                integer += data.insertAll(list);
            }
        }
        watch.stop();
        return commonUtills.returnJson("insert",integer,listData);
    }

    //异常数据入库
    @RequestMapping("/arrangeMinutedataab")
    public Map<String, Object> arrangeMinutedataab(@RequestBody ColumnName param){
        StopWatch watch = new StopWatch();
        watch.start();
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime=DateUtil.dateFormat(date,"yyyy-MM-dd 08:00:00"),etime="",pathname="",datasource="";
        List<String> stcdList = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        if(null != param.getStcd()){
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if(param.getDatasource()!=null){
            datasource=param.getDatasource();
            typeList=Arrays.asList(param.getDatasource().split(","));
        }
        if(null != param.getPathname()){
            pathname = param.getPathname();
        }
        if(param.getStrExp()!=null){
            idList= Arrays.asList(param.getStrExp().split(","));
        }
        if(null != param.getStime()){
            stime = param.getStime();
        }
        if(null != param.getEtime()){
            etime = param.getEtime();
        }
        if(pathname=="all"){//整理所有站的数据
            stcdList=null;
        }
        List<ST_STBPRP_B_STCDPojo> listStcd=st_stbprp_b_stcdData.selectList(stcdList,typeList,null);
        stcdList=listStcd.stream()
                .map(ST_STBPRP_B_STCDPojo::getSTCD)
                .filter(stcd -> stcd != null) // 过滤掉 null
                .collect(Collectors.toList());
        Integer integer=0;
        //查询异常办法接口
        List<ST_SCREENINGRULESPojo> listRules=st_screeningrulesData.selectList(idList,"雨量",null);
        List<ST_PPTN_RPojo> pptnList = data.selectListByTime(stcdList, stime, etime);
        List<ST_PPTN_R_ABPojo> listData=new ArrayList<>();
        if(pptnList.size()>0){
            List<String> finalStcdList = stcdList;
            listRules.forEach(rule->{
                List<String> ruleIDList = Arrays.asList("1", "5", "6", "7");
                String ruleID=rule.getId();
                if(ruleIDList.contains(ruleID))//站点阈值
                {
                    double wvalue=rule.getWvalue();
                    double wvaluemax=rule.getWvaluemax();
                    List<ST_PPTN_RPojo> pptnListTemp=pptnList.stream().filter(pptn -> pptn.getDRP() != null && pptn.getDRP() >= wvalue&&pptn.getDRP() <wvaluemax).collect(Collectors.toList());
                    pptnListTemp.forEach(pptn->{
                        ST_PPTN_R_ABPojo pojo=new ST_PPTN_R_ABPojo();
                        pojo.setSTCD(pptn.getSTCD());
                        pojo.setTM(pptn.getTM());
                        pojo.setDRP(pptn.getDRP());
                        pojo.setTYPE(ruleID);
                        pojo.setSTATUS("0");
                        pojo.setCONTENT("minute");
                        listData.add(pojo);
                    });
                }
                else if(ruleID.equals("2"))//临近站偏差
                {
                    System.out.println("雨量数据多少条："+pptnList.size());
                    List<ST_STBPRP_B_DISTANCEPojo> listDistance= st_stbprp_b_distanceData.selectList(null,"PP");
                    double wvalue=rule.getWvalue();
                    if(pptnList.size()>0){
                        finalStcdList.forEach(danStcd->{
                            //大于5mm降雨的才考虑异常问题
                            List<ST_PPTN_RPojo> pptnListTemp=pptnList.stream().filter(pptn -> pptn.getSTCD().equals(danStcd)&&pptn.getDRP() != null && pptn.getDRP() >= 5).collect(Collectors.toList());
                            pptnListTemp.forEach(pptn->{
                                List<ST_STBPRP_B_DISTANCEPojo> listDistanceTemp=listDistance.stream().filter(dis -> dis.getStcd().equals(pptn.getSTCD())).collect(Collectors.toList());
                                if(listDistanceTemp.size()>0){
                                    String bStcd=listDistanceTemp.get(0).getStcddis();//最近的一个站
                                    System.out.println("stcd："+pptn.getSTCD()+"，"+"bStcd："+bStcd);
                                    //相对偏差度
                                    //(A站-B站)/A站
                                    String tm=pptn.getTM();
                                    List<ST_PPTN_RPojo> pptnListTempBTM=pptnList.stream().filter(pptnb -> pptnb.getSTCD().equals(bStcd)&&pptnb.getTM().equals(tm)).collect(Collectors.toList());
                                    double bdrp=0;
                                    double adrp=pptn.getDRP();
                                    String MARKS="临近站缺数据";
                                    if(pptnListTempBTM.size()>0){//临近站不缺数据
                                        bdrp=pptnListTempBTM.get(0).getDRP();
                                        MARKS="";
                                    }
                                    double pcd=Math.abs ((adrp-bdrp)/adrp);
                                    if(pcd>wvalue){
                                        ST_PPTN_R_ABPojo pojo=new ST_PPTN_R_ABPojo();
                                        pojo.setSTCD(pptn.getSTCD());
                                        pojo.setTM(pptn.getTM());
                                        pojo.setDRP(pptn.getDRP());
                                        pojo.setTYPE(ruleID);
                                        pojo.setSTCDDIS(bStcd);
                                        pojo.setSTCDDISDRP(bdrp);
                                        pojo.setSTATUS("0");
                                        pojo.setCONTENT("minute");
                                        if(!MARKS.equals("")){
                                            pojo.setMARKS(MARKS);
                                        }
                                        listData.add(pojo);
                                    }
                                }
                            });
                        });
                    }
                }
                else if(ruleID.equals("3"))//数据不连续
                {
                    double wvalue=rule.getWvalue();
                    List<ST_PPTN_RPojo> pptnListTemp=pptnList.stream().filter(pptn -> pptn.getDRP() != null && pptn.getDRP() == wvalue).collect(Collectors.toList());
                    pptnListTemp.forEach(pptn->{
                        ST_PPTN_R_ABPojo pojo=new ST_PPTN_R_ABPojo();
                        pojo.setSTCD(pptn.getSTCD());
                        pojo.setTM(pptn.getTM());
                        pojo.setDRP(pptn.getDRP());
                        pojo.setTYPE(ruleID);
                        pojo.setSTATUS("0");
                        pojo.setCONTENT("minute");
                        listData.add(pojo);
                    });
//                    finalStcdList.forEach(danStcd->{
//                        List<ST_PPTN_RPojo> pptnListTemp=pptnList.stream().filter(pptn -> pptn.getSTCD().equals(danStcd)).collect(Collectors.toList());
//                        for(int num=0;num<pptnListTemp.size();num++){
//                            ST_PPTN_RPojo pptn=pptnListTemp.get(num);
//                            if(num>0){
//                                String dateStr1= pptnListTemp.get(num-1).getTM();
//                                String dateStr2= pptnListTemp.get(num).getTM();
//                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                try {
//                                    Date date1 = formatter.parse(dateStr1);
//                                    Date date2 = formatter.parse(dateStr2);
//                                    // 判断 dateStr1 加上 5 分钟是否等于 dateStr2
//                                    while (!date1.equals(date2)) {
//                                        // 给 date1 加上 5 分钟
//                                        date1.setTime(date1.getTime() + 5 * 60 * 1000);
//                                        // 如果 date1 超过了 date2，说明有问题
//                                        if (date1.after(date2)) {
//                                            System.out.println("dateStr1 加上 5 分钟后超过了 dateStr2，可能存在数据问题！");
//                                            break;
//                                        }
//                                        // 如果 date1 等于 date2，退出循环
//                                        if (date1.equals(date2)) {
//                                            break;
//                                        }
//                                        else //说明缺数据
//                                        {
//                                            String formattedDate1 = formatter.format(date1);
//                                            ST_PPTN_R_ABPojo pojo=new ST_PPTN_R_ABPojo();
//                                            pojo.setSTCD(pptn.getSTCD());
//                                            pojo.setTM(formattedDate1);
//                                            pojo.setDRP(0.0);
//                                            pojo.setTYPE(ruleID);
//                                            pojo.setSTATUS("0");
//                                            listData.add(pojo);
//                                        }
//                                    }
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    });
                }
                else if(ruleID.equals("4"))//相邻站偏差
                {
                    List<ST_STBPRP_B_DISTANCEPojo> listDistance= st_stbprp_b_distanceData.selectList(null,"PP");
                    double wvalue=rule.getWvalue();
                    if(pptnList.size()>0){
                        finalStcdList.forEach(danStcd->{
                            //大于5mm降雨的才考虑异常问题
                            List<ST_PPTN_RPojo> pptnListTemp=pptnList.stream().filter(pptn -> pptn.getSTCD().equals(danStcd)&&pptn.getDRP() != null && pptn.getDRP() >= 5).collect(Collectors.toList());
                            pptnListTemp.forEach(pptn->{
                                List<ST_STBPRP_B_DISTANCEPojo> listDistanceTemp=listDistance.stream().filter(dis -> dis.getStcd().equals(pptn.getSTCD())).collect(Collectors.toList());
                                if(listDistanceTemp.size()>0){
                                    List<String> bStcdList =listDistanceTemp.stream()
                                            .map(ST_STBPRP_B_DISTANCEPojo::getStcddis)
                                            .filter(stcd -> stcd != null) // 过滤掉 null
                                            .collect(Collectors.toList());
                                    List<String> finalBStcdList = bStcdList;
                                    List<ST_PPTN_RPojo> pptnListTempB=pptnList.stream().filter(pptnb -> finalBStcdList.contains( pptnb.getSTCD())).collect(Collectors.toList());
                                    //相对偏差度
                                    //(A站-B站)/A站
                                    String tm=pptn.getTM();
                                    List<ST_PPTN_RPojo> pptnListTempBTM=pptnListTempB.stream().filter(pptnb -> pptnb.getTM().equals(tm)).collect(Collectors.toList());
                                    String MARKS="临近站缺数据";
                                    double adrp=pptn.getDRP();
                                    double bdrp=0;
                                    if(pptnListTempBTM.size()>0){
                                        double sumDRP = pptnListTempBTM.stream()
                                                .filter(item -> item.getDRP() != null) // 过滤掉 DRP 为 null 的项
                                                .mapToDouble(ST_PPTN_RPojo::getDRP)   // 将 DRP 提取为 double 类型
                                                .sum();
                                        bdrp=sumDRP/pptnListTempBTM.size(); //求平均
                                        MARKS="";
                                    }
                                    double pcd=Math.abs ((adrp-bdrp)/adrp);
                                    if(pcd>wvalue){
                                        ST_PPTN_R_ABPojo pojo=new ST_PPTN_R_ABPojo();
                                        pojo.setSTCD(pptn.getSTCD());
                                        pojo.setTM(pptn.getTM());
                                        pojo.setDRP(pptn.getDRP());
                                        pojo.setTYPE(ruleID);
                                        pojo.setSTCDDIS(String.join(",",finalBStcdList));
                                        pojo.setSTCDDISDRP(bdrp);
                                        pojo.setSTATUS("0");
                                        pojo.setCONTENT("minute");
                                        if(!MARKS.equals("")){
                                            pojo.setMARKS(MARKS);
                                        }
                                        listData.add(pojo);
                                    }
                                }
                            });
                        });
                    }
                }
            });
        }
        if(listData.size()>0){
            int count = 80;
            int number = listData.size() / count;
            if(listData.size() % count != 0){
                number = number + 1;
            }
            List<ST_PPTN_R_ABPojo> list = new ArrayList<>();
            for(int i = 0;i < number;i++){
                if(i == number - 1){
                    list = listData.subList(count * i,listData.size());
                }else {
                    list = listData.subList(count * i,count * ( i + 1));
                }
                integer += st_pptn_r_abData.insertAll(list);
            }
        }
        watch.stop();
        return commonUtills.returnJson("insert",integer,listData);
    }


    //异常数据入库：日数据
    @RequestMapping("/arrangeMinutedataDayab")
    public Map<String, Object> arrangeMinutedataDayab(@RequestBody ColumnName param){
        StopWatch watch = new StopWatch();
        watch.start();
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime=DateUtil.dateFormat(date,"yyyy-MM-dd 08:00:00"),etime="",pathname="";
        List<String> stcdList = new ArrayList<>();
        List<String> stcdListAll = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        List<String> pathnameList = new ArrayList<>();
        if(null != param.getStcd()){
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if(param.getDatasource()!=null){//分区（数据来源）
            typeList=Arrays.asList(param.getDatasource().split(","));
        }
        if(null != param.getPathname()){
            pathname = param.getPathname();
            pathnameList=Arrays.asList(param.getPathname().split(","));
        }
        if(param.getStrExp()!=null){
            idList= Arrays.asList(param.getStrExp().split(","));
        }
        if(null != param.getStime()){
            stime = param.getStime();
        }
        if(null != param.getEtime()){
            etime = param.getEtime();
        }

        List<ST_STBPRP_B_STCDPojo> listStcd=st_stbprp_b_stcdData.selectList(stcdList,typeList,null);
        stcdListAll=listStcd.stream()
                .map(ST_STBPRP_B_STCDPojo::getSTCD)
                .filter(stcd -> stcd != null) // 过滤掉 null
                .collect(Collectors.toList());//所有站，包含参考的基本站
        List<ST_PSTAT_RPojo> pptnList = st_pstat_rData.selectListDay(stcdListAll, stime, etime,pathnameList);

        listStcd = listStcd.stream()
                .filter(u -> u.getREMARK() == null || !"基本站".equals(u.getREMARK()))
                .collect(Collectors.toList());//基本站不筛选
        stcdList=listStcd.stream()
                .map(ST_STBPRP_B_STCDPojo::getSTCD)
                .filter(stcd -> stcd != null) // 过滤掉 null
                .collect(Collectors.toList());//不包含参考的基本站

        Integer integer=0;
        //查询异常办法接口
        List<ST_SCREENINGRULESPojo> listRules=st_screeningrulesData.selectList(idList,"雨量",null);
        List<ST_PPTN_R_ABPojo> listData=new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        DateTimeFormatter formatterYMDHMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(pptnList.size()>0){
            List<String> finalStcdList = stcdList;
            listRules.forEach(rule->{
                List<String> ruleIDList = Arrays.asList("8");
                String ruleID=rule.getId();
                if(ruleIDList.contains(ruleID))//站点阈值
                {
                    double wvalue=rule.getWvalue();
                    double wvaluemax=rule.getWvaluemax();
                    List<ST_PSTAT_RPojo> pptnListTemp=pptnList.stream().filter(pptn -> pptn.getACCP() != null && pptn.getACCP() >= wvalue&&pptn.getACCP() <wvaluemax).collect(Collectors.toList());
                    pptnListTemp.forEach(pptn->{
                        ST_PPTN_R_ABPojo pojo=new ST_PPTN_R_ABPojo();
                        pojo.setSTCD(pptn.getSTCD());
                        String idtm =pptn.getIDTM();
                        pojo.setTM(idtm);
                        pojo.setDRP(pptn.getACCP());
                        pojo.setTYPE(ruleID);
                        pojo.setSTATUS("0");
                        pojo.setCONTENT("day");
                        listData.add(pojo);
                    });
                }
                else if(ruleID.equals("2"))//临近站偏差
                {
                    System.out.println("雨量数据多少条："+pptnList.size());
                    List<ST_STBPRP_B_DISTANCEPojo> listDistance= st_stbprp_b_distanceData.selectList(null,"PP");
                    if(pptnList.size()>0){
                        finalStcdList.forEach(danStcd->{
                            //日雨量大于10mm降雨的才考虑异常问题
                            List<ST_PSTAT_RPojo> pptnListTemp=pptnList.stream().filter(pptn -> pptn.getSTCD().equals(danStcd)&&pptn.getACCP() != null && pptn.getACCP() >= 10&&pptn.getACCP()<500).collect(Collectors.toList());
                            pptnListTemp.forEach(pptn->{
                                List<ST_STBPRP_B_DISTANCEPojo> listDistanceTemp=listDistance.stream().filter(dis -> dis.getStcd().equals(pptn.getSTCD())&&dis.getDistance()<=10).collect(Collectors.toList());//十公里内才考虑临近站
                                String tm=pptn.getIDTM();
                                if(listDistanceTemp.size()>0){
                                    List<ST_PSTAT_RPojo> pptnListDistance5=new ArrayList<>();//最近的5个站
                                    for (int i = 0; i < listDistanceTemp.size(); i++) {
                                        ST_STBPRP_B_DISTANCEPojo distancePojo=listDistanceTemp.get(i);
                                        //最近的一个有数据的站
                                        List<ST_PSTAT_RPojo> pptnListDistance=pptnList.stream().filter(pptnb -> pptnb.getSTCD().equals(distancePojo.getStcddis())&&pptnb.getIDTM().equals(tm)).collect(Collectors.toList());
                                        if(pptnListDistance.size()>0){
                                            pptnListDistance5.add(pptnListDistance.get(0));
                                        }
                                        if(pptnListDistance5.size()>=5){
                                            break; // 直接跳出
                                        }
                                    }
                                    if (pptnListDistance5.size()>0){
                                        //中小雨（＜25mm）绝对偏差率达到100%，
                                        // //大雨(25mm~50mm)绝对偏差率达到或超过50%，
                                        // 暴雨及以上（＞=50mm）绝对偏差率达到或超过20%
                                        double adrp=pptn.getACCP();
                                        double wvalue=0;
                                        if(adrp<25){
                                            wvalue=1;//100%
                                        }
                                        else if(adrp>=25&&adrp<50){
                                            wvalue=0.5;//50%
                                        }
                                        else if(adrp>=50){
                                            wvalue=0.2;//20%
                                        }
                                        StringBuilder stringBuilder=new StringBuilder();
                                        stringBuilder.append(tm+"*****************stcd:"+pptn.getSTCD()+"("+adrp+")");
                                        double finalWvalue = wvalue;
                                        AtomicBoolean allTrue= new AtomicBoolean(false);
                                        AtomicInteger _index= new AtomicInteger(0);
                                        pptnListDistance5.forEach(pptn5->{//每个站都必须满足才能算异常
                                            String bStcd=pptn5.getSTCD();
                                            double bdrp=pptn5.getACCP();
//                                        String MARKS="临近站缺数据";
                                            //相对偏差度
                                            //(A站-B站)/B站
                                            double pcd=bdrp>0?Math.abs ((adrp-bdrp)/bdrp):0;
                                            if(pcd>= finalWvalue){
                                                stringBuilder.append(",stcd"+_index.get()+":"+bStcd+"("+bdrp+"@"+pcd+")");
//                                            System.out.println("stcd："+pptn.getSTCD()+"，"+"bStcd："+bStcd);
                                                allTrue.set(true);
                                                _index.getAndIncrement();
                                            }
                                            else {
                                                allTrue.set(false);
                                            }
                                        });

                                        if(_index.get()==pptnListDistance5.size()){//都满足了才算异常数据
                                            System.out.println("ok！满足了"+stringBuilder.toString());
//                                        System.out.println("allTrue都满足："+allTrue.get());
                                            List<String> bStcdList =pptnListDistance5.stream()
                                                    .map(ST_PSTAT_RPojo::getSTCD)
                                                    .filter(stcd -> stcd != null) // 过滤掉 null
                                                    .collect(Collectors.toList());
                                            List<String> finalBStcdList = bStcdList;

                                            String idtm = pptn.getIDTM();
                                            ST_PPTN_R_ABPojo pojo = new ST_PPTN_R_ABPojo();
                                            pojo.setSTCD(pptn.getSTCD());
                                            pojo.setTM(idtm);
                                            pojo.setDRP(pptn.getACCP());
                                            pojo.setTYPE(ruleID);
//                                                pojo.setSTCDDIS(bStcd);
                                            pojo.setSTCDDIS(String.join(",",finalBStcdList));
//                                        pojo.setSTCDDISDRP(bdrp);
                                            pojo.setSTATUS("0");
                                            pojo.setCONTENT("day");
//                                                if (!MARKS.equals("")) {
//                                                    pojo.setMARKS(MARKS);
//                                                }
                                            listData.add(pojo);
                                        }
                                    }
                                }
                            });
                        });
                    }
                }
                else if(ruleID.equals("4"))//相邻5站偏差
                {
                    List<ST_STBPRP_B_DISTANCEPojo> listDistance= st_stbprp_b_distanceData.selectList(null,"PP");
                    double wvalue=rule.getWvalue();
                    if(pptnList.size()>0){
                        finalStcdList.forEach(danStcd->{
                            //日雨量大于10mm降雨的才考虑异常问题
                            List<ST_PSTAT_RPojo> pptnListTemp=pptnList.stream().filter(pptn -> pptn.getSTCD().equals(danStcd)&&pptn.getACCP() != null && pptn.getACCP() >= 10&&pptn.getACCP()<500).collect(Collectors.toList());
                            pptnListTemp.forEach(pptn->{
                                List<ST_STBPRP_B_DISTANCEPojo> listDistanceTemp=listDistance.stream().filter(dis -> dis.getStcd().equals(pptn.getSTCD())).collect(Collectors.toList());
                                if(listDistanceTemp.size()>0){
                                    List<String> bStcdList =listDistanceTemp.stream()
                                            .map(ST_STBPRP_B_DISTANCEPojo::getStcddis)
                                            .filter(stcd -> stcd != null) // 过滤掉 null
                                            .collect(Collectors.toList());
                                    List<String> finalBStcdList = bStcdList;
                                    List<ST_PSTAT_RPojo> pptnListTempB=pptnList.stream().filter(pptnb -> finalBStcdList.contains( pptnb.getSTCD())).collect(Collectors.toList());
                                    //相对偏差度
                                    //(A站-B站)/A站
                                    String tm=pptn.getIDTM();
                                    List<ST_PSTAT_RPojo> pptnListTempBTM=pptnListTempB.stream().filter(pptnb -> pptnb.getIDTM().equals(tm)).collect(Collectors.toList());
                                    double bdrp=0;
                                    double adrp=pptn.getACCP();
                                    String MARKS="相邻5站缺数据";
                                    if(pptnListTempBTM.size()>0){
                                        double sumDRP = pptnListTempBTM.stream()
                                                .filter(item -> item.getACCP() != null) // 过滤掉 DRP 为 null 的项
                                                .mapToDouble(ST_PSTAT_RPojo::getACCP)   // 将 DRP 提取为 double 类型
                                                .sum();
                                        bdrp=sumDRP/pptnListTempBTM.size(); //求平均
                                        MARKS="";
                                    }
                                    double pcd=Math.abs ((adrp-bdrp)/adrp);
                                    if(pcd>wvalue){
                                        String idtm =pptn.getIDTM();
                                        ST_PPTN_R_ABPojo pojo=new ST_PPTN_R_ABPojo();
                                        pojo.setSTCD(pptn.getSTCD());
                                        pojo.setTM(idtm);
                                        pojo.setDRP(pptn.getACCP());
                                        pojo.setTYPE(ruleID);
                                        pojo.setSTCDDIS(String.join(",",finalBStcdList));
                                        pojo.setSTCDDISDRP(bdrp);
                                        pojo.setSTATUS("0");
                                        pojo.setCONTENT("day");
                                        if(!MARKS.equals("")){
                                            pojo.setMARKS(MARKS);
                                        }
                                        listData.add(pojo);
                                    }
                                }
                            });
                        });
                    }
                }
            });
        }
        if(listData.size()>0){
            int count = 80;
            int number = listData.size() / count;
            if(listData.size() % count != 0){
                number = number + 1;
            }
            List<ST_PPTN_R_ABPojo> list = new ArrayList<>();
            for(int i = 0;i < number;i++){
                if(i == number - 1){
                    list = listData.subList(count * i,listData.size());
                }else {
                    list = listData.subList(count * i,count * ( i + 1));
                }
                integer += st_pptn_r_abData.insertAll(list);
            }
        }

        watch.stop();

        return commonUtills.returnJson("insert",integer,listData);


//        if(null != listData){
//            return commonUtills.returnJson("select",listData.size(),listData);
//        }else {
//            return commonUtills.returnJson("select", -1, listData);
//        }
    }


    //阈值异常二次筛选：筛选突跳的
    @RequestMapping("/arrangeMinutedataTsdbTwo")
    public Map<String, Object> arrangeMinutedataTsdbTwo(@RequestBody ColumnName param){
        StopWatch watch = new StopWatch();
        watch.start();
        List<ST_PPTN_RPojo> pptnList=new ArrayList<>();
        List<ST_PPTN_R_LOGPojo> pptnLogList=new ArrayList<>();
        List<ST_PPTN_R_ABPojo> listAbDel=new ArrayList<>();
        List<String> typeList=new ArrayList<>();
        if(param.getPathname()!=null){
            typeList=Arrays.asList(param.getPathname().split(","));
        }

        List<ST_PPTN_R_ABPojo> listAb=st_pptn_r_abData.selectList(null,null,null,typeList,null,null,null,null,null);
        listAb.forEach(u->{
            List<String> stcdList=Arrays.asList(u.getSTCD().split(","));
            String tm=u.getTM();
            String stime="",etime="";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(tm, formatter);
            stime = dateTime.minusMinutes(5).format(formatter);// 计算stime(减5分钟)
            etime = dateTime.plusMinutes(5).format(formatter);        // 计算etime(加5分钟)
            List<ST_PPTN_RPojo> listR=data.selectListLS(stcdList,stime,etime,null);

            //处理异常值：当前值与前后两个值相差50，取前后的两个数值的平均
            double bdrp=0;//前
            double adrp=0;//后
            double cdrp=u.getDRP();//当前值
            double drp=0;//纠正值
            if(listR.size()>2){
                bdrp=listR.get(0).getDRP();
                adrp=listR.get(2).getDRP();
                if(Math.abs(cdrp-bdrp)>50&&Math.abs(cdrp-adrp)>50&&adrp<25&&bdrp<25&&adrp>=0&&bdrp>=0){//同时满足和前后都差50
                    drp=(bdrp+adrp)/2;
                    new javalog().writelog("**********站点："+u.getSTCD()+"，时间："+tm+"，cdrp："+cdrp+"，bdrp："+bdrp+"("+listR.get(0).getTM()+")，adrp："+adrp+"("+listR.get(2).getTM()+")，drp："+drp,templatefilepath);
                    ST_PPTN_RPojo pojo=new ST_PPTN_RPojo();
                    pojo.setSTCD(u.getSTCD());
                    pojo.setTM(u.getTM());
                    pojo.setDRP(drp);
                    pojo.setWTH("2");//程序内部平均
                    pptnList.add(pojo);


                    listAbDel.add(u);//需要删除的



                    // 获取当前时间
                    LocalDateTime now = LocalDateTime.now();
                    // 格式化时间
                    String formattedDateTime = now.format(formatter);
                    ST_PPTN_R_LOGPojo pojoLog=new ST_PPTN_R_LOGPojo();
                    pojoLog.setId(UUID.randomUUID().toString().replaceAll("-","").substring(0,16));
                    pojoLog.setTm(formattedDateTime);//修改时间
                    pojoLog.setBvalue(cdrp);//修改之前的值
                    pojoLog.setAvalue(drp);//修改之后的值
                    pojoLog.setOwer("后台程序");//编辑人员，修改人员
                    pojoLog.setType("雨量");//类型，修改的是水位还是雨量等
                    pojoLog.setNote(u.getTYPE());
                    pojoLog.setContent(u.getCONTENT());
//                    pojoLog.setMarks();
                    pojoLog.setDatatm(u.getTM());//修改的数据时间
                    pojoLog.setStcd(u.getSTCD());//站码
                    pojoLog.setStnm(u.getSTNM());//站码

                    pptnLogList.add(pojoLog);
                }
            }
        });
        Integer integer=0;
//        if(pptnList.size()>0){
//            int count = 50;
//            int number = pptnList.size() / count;
//            if(pptnList.size() % count != 0){
//                number = number + 1;
//            }
//            List<ST_PPTN_RPojo> list = new ArrayList<>();
//            for(int i = 0;i < number;i++){
//                if(i == number - 1){
//                    list = pptnList.subList(count * i,pptnList.size());
//                }else {
//                    list = pptnList.subList(count * i,count * ( i + 1));
//                }
//                integer += data.updateALL(list);
//            }
//        }
//
//        if(pptnLogList.size()>0){
//            int count = 50;
//            int number = pptnLogList.size() / count;
//            if(pptnLogList.size() % count != 0){
//                number = number + 1;
//            }
//            List<ST_PPTN_R_LOGPojo> list = new ArrayList<>();
//            for(int i = 0;i < number;i++){
//                if(i == number - 1){
//                    list = pptnLogList.subList(count * i,pptnLogList.size());
//                }else {
//                    list = pptnLogList.subList(count * i,count * ( i + 1));
//                }
//                integer += st_pptn_r_logData.insertAll(list);
//            }
//        }

        if(listAbDel.size()>0){
            int count = 50;
            int number = listAbDel.size() / count;
            if(listAbDel.size() % count != 0){
                number = number + 1;
            }
            List<ST_PPTN_R_ABPojo> list = new ArrayList<>();
            for(int i = 0;i < number;i++){
                if(i == number - 1){
                    list = listAbDel.subList(count * i,listAbDel.size());
                }else {
                    list = listAbDel.subList(count * i,count * ( i + 1));
                }
                integer += st_pptn_r_abData.deleteAll(list);
            }
        }


        watch.stop();
//        if(null != pptnList){
//            return commonUtills.returnJson("select",pptnList.size(),pptnList);
//        }else {
//            return commonUtills.returnJson("select", -1, pptnList);
//        }

        return commonUtills.returnJson("insert",integer,pptnList);
    }

    //解决完突跳的数据之后，更新日雨量数据
    @RequestMapping("/arrangeDayLogDataTwo")
    public Map<String, Object> arrangeDayLogDataTwo(@RequestBody ColumnName param){
        StopWatch watch = new StopWatch();
        watch.start();
        List<String> stcdList=new ArrayList<>();
        List<ST_PSTAT_RPojo> listData=new ArrayList<>();//日数据
        List<ST_PSTAT_RPojo> listDataH=new ArrayList<>();//小时数据
        List<ST_PPTN_RPojo> pptnList=data.selectListLS(stcdList,null,null,"2");
        pptnList.forEach(st_pptn_rPojo -> {
            String tm=st_pptn_rPojo.getTM();
            //处理日数据************************************
            String boundaryTime = TimeRangeChecker.getBoundaryTime(tm);
            System.out.println("时间："+tm+"，日结尾时间："+boundaryTime);
            List<String> stcdListR= Arrays.asList(st_pptn_rPojo.getSTCD().split(","));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter formatterH = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00");
            LocalDateTime boundaryDateTime = LocalDateTime.parse(boundaryTime, formatter);
            String stime= boundaryDateTime.minusHours(24).format(formatter);
            List<ST_PPTN_RPojo> ListR=data.selectListByTime(stcdListR,stime,boundaryTime);
            List<ST_PSTAT_RPojo> listDataT=listData.stream().filter(pptnb -> pptnb.getSTCD().equals(st_pptn_rPojo.getSTCD())&&pptnb.getIDTM().equals(boundaryTime)&&pptnb.getSTTDRCD().equals("1")).collect(Collectors.toList());
            if(listDataT.size()==0){//不选择才加入
                double sumDRP = ListR.stream()
                        .filter(item -> item.getDRP() != null) // 过滤掉 DRP 为 null 的项
                        .mapToDouble(ST_PPTN_RPojo::getDRP)   // 将 DRP 提取为 double 类型
                        .sum();
                ST_PSTAT_RPojo pojop=new ST_PSTAT_RPojo();
                pojop.setSTCD(st_pptn_rPojo.getSTCD());
                pojop.setIDTM(boundaryTime);
                pojop.setACCP(sumDRP);
                pojop.setSTTDRCD("1");
                pojop.setWTH("2");
                listData.add(pojop);
            }
            //处理日数据************************************

            //处理小时数据************************************
            LocalDateTime tmDateTime = LocalDateTime.parse(tm, formatter);
            String tmStime= tmDateTime.minusHours(0).format(formatterH);
            String tmEtime= tmDateTime.plusHours(1).format(formatterH);//加上一个小时
            List<ST_PSTAT_RPojo>listDataHT=listDataH.stream().filter(pptnb -> pptnb.getSTCD().equals(st_pptn_rPojo.getSTCD())&&pptnb.getIDTM().equals(tmEtime)&&pptnb.getSTTDRCD().equals("h")).collect(Collectors.toList());
            if(listDataHT.size()==0) {//不选择才加入
                List<ST_PPTN_RPojo> ListRH=ListR.stream()
                        .filter(stPptn ->TimeRangeChecker.isInTimeRange(stPptn.getTM(), tmStime, tmEtime))
                        .collect(Collectors.toList());
                double sumDRP = ListRH.stream()
                        .filter(item -> item.getDRP() != null) // 过滤掉 DRP 为 null 的项
                        .mapToDouble(ST_PPTN_RPojo::getDRP)   // 将 DRP 提取为 double 类型
                        .sum();
                ST_PSTAT_RPojo pojop=new ST_PSTAT_RPojo();
                pojop.setSTCD(st_pptn_rPojo.getSTCD());
                pojop.setIDTM(tmEtime);
                pojop.setACCP(sumDRP);
                pojop.setSTTDRCD("h");
                pojop.setWTH("2");
                listDataH.add(pojop);
            }
            //处理小时数据************************************
        });
        int integer=0;
        listData.forEach(st_pstat_rPojo -> {
            int rows= st_pstat_rData.upDateOne(st_pstat_rPojo);
            if(rows==0){
                st_pstat_rData.insertOne(st_pstat_rPojo);
            }
            System.out.println("影响行数："+rows+"**************"+st_pstat_rPojo);
        });

 //       integer += st_pstat_rData.updateALL(listDataH);
//        if(listDataH.size()>0){
//            int count = 80;
//            int number = listDataH.size() / count;
//            if(listDataH.size() % count != 0){
//                number = number + 1;
//            }
//            List<ST_PSTAT_RPojo> list = new ArrayList<>();
//            for(int i = 0;i < number;i++){
//                if(i == number - 1){
//                    list = listDataH.subList(count * i,listDataH.size());
//                }else {
//                    list = listDataH.subList(count * i,count * ( i + 1));
//                }
//                integer += st_pstat_rData.updateALL(list);
//            }
//        }
//        if(listData.size()>0){
//            int count = 80;
//            int number = listData.size() / count;
//            if(listData.size() % count != 0){
//                number = number + 1;
//            }
//            List<ST_PSTAT_RPojo> list = new ArrayList<>();
//            for(int i = 0;i < number;i++){
//                if(i == number - 1){
//                    list = listData.subList(count * i,listData.size());
//                }else {
//                    list = listData.subList(count * i,count * ( i + 1));
//                }
//                integer += st_pstat_rData.updateALL(list);
//            }
//        }
        watch.stop();
        if(null != listData){
            return commonUtills.returnJson("select",listData.size(),listData);
        }else {
            return commonUtills.returnJson("select", -1, listData);
        }

//        return commonUtills.returnJson("insert",integer,listData);
    }


    //用临近站（距离最近的站）插补，确保 1、临近站数据没问题 2、临近站不要超过10公里
    //阈值或者缺测数据
    @RequestMapping("/arrangeMinutedataTsdbTree")
    public  Map<String,Object>  arrangeMinutedataTsdbTree(@RequestBody ColumnName param)
    {
        StopWatch watch = new StopWatch();
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stimes="",etimes="";
        watch.start();
        if(null != param.getStime()){
            stimes = param.getStime();
        }
        if(null != param.getEtime()){
            etimes = param.getEtime();
        }
        List<String> typeList=new ArrayList<>();
        if(param.getPathname()!=null){
            typeList=Arrays.asList(param.getPathname().split(","));
        }
        List<String> stcdList = new ArrayList<>();
        if(null != param.getStcd()){
            stcdList = Arrays.asList(param.getStcd().split(","));
        }

        List<ST_PPTN_R_LOGPojo> pptnLogList=new ArrayList<>();
        List<ST_PPTN_RPojo> listData=new ArrayList<>();
        String status="0";
        List<ST_PPTN_R_ABPojo> listAb=st_pptn_r_abData.selectList(stcdList,stimes,etimes,typeList,status,null,null,null,null);
        List<ST_STBPRP_B_DISTANCEPojo> listDistance= st_stbprp_b_distanceData.selectList(stcdList,"PP");
        listAb.forEach(u -> {
            String tm=u.getTM();
            String stime=tm,etime=tm;
            List<ST_STBPRP_B_DISTANCEPojo> listDistanceTemp=listDistance.stream().filter(dis -> dis.getStcd().equals(u.getSTCD())&&dis.getDistance()<=10).collect(Collectors.toList());//十公里内才考虑临近站
            List<String> stcdListD=listDistanceTemp.stream()
                    .map(ST_STBPRP_B_DISTANCEPojo::getStcddis)
                    .filter(stcd -> stcd != null) // 过滤掉 null
                    .collect(Collectors.toList());
            List<ST_PPTN_RPojo> pptnList=data.selectListLS(stcdListD,stime,etime,null);
            pptnList=pptnList.stream().filter(p->p.getDRP()>=0).collect(toList());//有-99是缺测的
            if(listDistanceTemp.size()>0){
                List<ST_PPTN_RPojo> pptnListDistance1=new ArrayList<>();//最近有数据的站点
                for (int i = 0; i < listDistanceTemp.size(); i++) {
                    ST_STBPRP_B_DISTANCEPojo distancePojo=listDistanceTemp.get(i);
                    //最近的一个有数据的站
                    List<ST_PPTN_RPojo> pptnListDistance=pptnList.stream().filter(pptnb -> pptnb.getSTCD().equals(distancePojo.getStcddis())&&pptnb.getTM().equals(tm)).collect(Collectors.toList());
                    if(pptnListDistance.size()>0){
                        pptnListDistance1.add(pptnListDistance.get(0));
                    }
                    if(pptnListDistance1.size()>=1){
                        break; // 直接跳出
                    }
                }
                if(pptnListDistance1.size()>0){
                    double sumDRP = pptnListDistance1.stream()
                            .filter(item -> item.getDRP() != null) // 过滤掉 DRP 为 null 的项
                            .mapToDouble(ST_PPTN_RPojo::getDRP)   // 将 DRP 提取为 double 类型
                            .sum();
                    if(sumDRP<50){//临近站也有问题，不做处理
                        // 计算非null值的数量
                        long validCount = pptnListDistance1.stream()
                                .filter(item -> item.getDRP() != null)
                                .count();
                        // 使用BigDecimal进行精确计算并四舍五入保留一位小数
                        BigDecimal average = BigDecimal.valueOf(sumDRP)
                                .divide(BigDecimal.valueOf(validCount), 1, RoundingMode.HALF_UP);
                        sumDRP=average.doubleValue();

                        ST_PPTN_RPojo pojo=new  ST_PPTN_RPojo();
                        pojo.setSTCD(u.getSTCD());
                        pojo.setTM(u.getTM());
                        pojo.setDRP(sumDRP);
                        pojo.setWTH("2");
                        listData.add(pojo);


                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        // 获取当前时间
                        LocalDateTime now = LocalDateTime.now();
                        // 格式化时间
                        String formattedDateTime = now.format(formatter);
                        ST_PPTN_R_LOGPojo pojoLog=new ST_PPTN_R_LOGPojo();
                        pojoLog.setId(UUID.randomUUID().toString().replaceAll("-","").substring(0,16));
                        pojoLog.setTm(formattedDateTime);//修改时间
                        pojoLog.setBvalue(u.getDRP());//修改之前的值
                        pojoLog.setAvalue(sumDRP);//修改之后的值
                        pojoLog.setOwer("后台程序");//编辑人员，修改人员
                        pojoLog.setType("雨量");//类型，修改的是水位还是雨量等
                        pojoLog.setNote(u.getTYPE());
                        pojoLog.setContent(u.getCONTENT());
                        pojoLog.setMarks(pptnListDistance1.get(0).getSTCD());
                        pojoLog.setDatatm(u.getTM());//修改的数据时间
                        pojoLog.setStcd(u.getSTCD());//站码
                        pojoLog.setStnm(u.getSTNM());//站码
                        pptnLogList.add(pojoLog);
                    }
                }
            }
        });
        watch.stop();
        int num=0;
        if(listData.size()>0){
            int count = 80;
            int number = listData.size() / count;
            if (listData.size() % count != 0){
                number += 1;
            }
            List<ST_PPTN_RPojo> list = new ArrayList<>();
            for(int i=0;i<number;i++){
                if(i == number - 1){
                    list = listData.subList(count * i,listData.size());
                }else {
                    list = listData.subList(count * i,count * (i + 1));
                }
                num += data.updateALL(list);
            }
        }
        if(pptnLogList.size()>0){
            int count = 50;
            int number = pptnLogList.size() / count;
            if(pptnLogList.size() % count != 0){
                number = number + 1;
            }
            List<ST_PPTN_R_LOGPojo> list = new ArrayList<>();
            for(int i = 0;i < number;i++){
                if(i == number - 1){
                    list = pptnLogList.subList(count * i,pptnLogList.size());
                }else {
                    list = pptnLogList.subList(count * i,count * ( i + 1));
                }
                num += st_pptn_r_logData.insertAll(list);
            }
        }
        return commonUtills.returnJson("insert",num,listData);
//        if(null != listData){
//            return commonUtills.returnJson("select",listData.size(),listData);
//        }else {
//            return commonUtills.returnJson("select", -1, listData);
//        }
    }


    //批量均值
    @RequestMapping("/neighborhoodav")
    public Map<String,Object>  neighborhoodav(@RequestBody List<ST_PPTN_R_ABPojo> quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer num = 0;
        List<ST_PPTN_R_ABPojo> listDataAB=new ArrayList<>();
        List<ST_PPTN_RPojo> listData=new ArrayList<>();
        List<ST_PPTN_R_LOGPojo> listDataLog=new ArrayList<>();
        if(quPojo.size()>0){
            String loginname=quPojo.get(0).getNAME();
            quPojo.forEach(st_pptn_r_abPojo -> {
                String stcddis=st_pptn_r_abPojo.getSTCDDIS();
                if(stcddis!=null){
                    List<String> stcdList = new ArrayList<>();
                    stcdList = Arrays.asList(stcddis.split(","));
                    List<ST_PPTN_RPojo>  pptnList= data.selectListLS(stcdList,st_pptn_r_abPojo.getTM(),st_pptn_r_abPojo.getTM(),null);
                    double sumDRP = pptnList.stream()
                            .filter(item -> item.getDRP() != null) // 过滤掉 DRP 为 null 的项
                            .mapToDouble(ST_PPTN_RPojo::getDRP)   // 将 DRP 提取为 double 类型
                            .sum();
                    double avgDrp=sumDRP/pptnList.size(); //求平均
                    // 计算非null值的数量
                    long validCount = pptnList.stream()
                            .filter(item -> item.getDRP() != null)
                            .count();
                    // 使用BigDecimal进行精确计算并四舍五入保留一位小数
                    BigDecimal average = BigDecimal.valueOf(sumDRP)
                            .divide(BigDecimal.valueOf(validCount), 1, RoundingMode.HALF_UP);
                    avgDrp=average.doubleValue();
                    ST_PPTN_RPojo pojoR=new  ST_PPTN_RPojo();
                    pojoR.setSTCD(st_pptn_r_abPojo.getSTCD());
                    pojoR.setTM(st_pptn_r_abPojo.getTM());
                    pojoR.setDRP(avgDrp);
                    pojoR.setWTH("1");
                    listData.add(pojoR);

                    ST_PPTN_R_ABPojo pojo=st_pptn_r_abPojo;
                    pojo.setSTATUS("1");
                    listDataAB.add(pojo);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    ST_PPTN_R_LOGPojo PojoLog=new ST_PPTN_R_LOGPojo();
                    PojoLog.setId(UUID.randomUUID().toString().replaceAll("-","").substring(0,16));
                    PojoLog.setDatatm(st_pptn_r_abPojo.getTM());
                    PojoLog.setOwer(loginname);
                    PojoLog.setType("雨量");
                    PojoLog.setStcd(st_pptn_r_abPojo.getSTCD());
                    PojoLog.setTm(LocalDateTime.now().format(formatter));
                    PojoLog.setNote(st_pptn_r_abPojo.getTYPE());
                    PojoLog.setContent(st_pptn_r_abPojo.getCONTENT());
                    PojoLog.setBvalue(st_pptn_r_abPojo.getDRP());
                    PojoLog.setAvalue(avgDrp);
                    listDataLog.add(PojoLog);
                }
            });

            //修改5分钟雨量
            if(listData.size()>0){
                int count = 80;
                int number = listData.size() / count;
                if (listData.size() % count != 0){
                    number += 1;
                }
                List<ST_PPTN_RPojo> list = new ArrayList<>();
                for(int i=0;i<number;i++){
                    if(i == number - 1){
                        list = listData.subList(count * i,listData.size());
                    }else {
                        list = listData.subList(count * i,count * (i + 1));
                    }
                    num += data.updateALL(list);
                }
            }

            //修改异常值的状态
            if(listDataAB.size()>0){
                int count = 80;
                int number = listDataAB.size() / count;
                if (listDataAB.size() % count != 0){
                    number += 1;
                }
                List<ST_PPTN_R_ABPojo> list = new ArrayList<>();
                for(int i=0;i<number;i++){
                    if(i == number - 1){
                        list = listDataAB.subList(count * i,listDataAB.size());
                    }else {
                        list = listDataAB.subList(count * i,count * (i + 1));
                    }
                    num += st_pptn_r_abData.updateALL(list);
                }
            }

            //日志入库
            if(listDataLog.size()>0){
                int count = 80;
                int number = listDataLog.size() / count;
                if (listDataLog.size() % count != 0){
                    number += 1;
                }
                List<ST_PPTN_R_LOGPojo> list = new ArrayList<>();
                for(int i=0;i<number;i++){
                    if(i == number - 1){
                        list = listDataLog.subList(count * i,listDataLog.size());
                    }else {
                        list = listDataLog.subList(count * i,count * (i + 1));
                    }
                    num += st_pptn_r_logData.insertAll(list);
                }
            }
        }

        watch.stop();
        return commonUtills.returnJson("insert",num,listData);
    }


    //给已知的异常找相邻站点
    @RequestMapping("/neighborhoodavmore")
    public  Map<String,Object>  neighborhoodavmore(@RequestBody ColumnName param)
    {
        StopWatch watch = new StopWatch();
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stimes="",etimes="";
        watch.start();
        if(null != param.getStime()){
            stimes = param.getStime();
        }
        if(null != param.getEtime()){
            etimes = param.getEtime();
        }
        List<String> typeList=new ArrayList<>();
        if(param.getPathname()!=null){
            typeList=Arrays.asList(param.getPathname().split(","));
        }
        List<String> stcdList = new ArrayList<>();
        if(null != param.getStcd()){
            stcdList = Arrays.asList(param.getStcd().split(","));
        }

        List<ST_PPTN_R_ABPojo> listData=new ArrayList<>();
        List<ST_PPTN_R_ABPojo> listAb=st_pptn_r_abData.selectList(stcdList,stimes,etimes,typeList,null,null,null,null,null);
        List<ST_STBPRP_B_DISTANCEPojo> listDistance= st_stbprp_b_distanceData.selectList(stcdList,"PP");
        listAb.forEach(u -> {
            String tm=u.getTM();
            String stime=tm,etime=tm;
            List<ST_STBPRP_B_DISTANCEPojo> listDistanceTemp=listDistance.stream().filter(dis -> dis.getStcd().equals(u.getSTCD())&&dis.getDistance()<=10).collect(Collectors.toList());//十公里内才考虑临近站
            List<String> stcdListD=listDistanceTemp.stream()
                    .map(ST_STBPRP_B_DISTANCEPojo::getStcddis)
                    .filter(stcd -> stcd != null) // 过滤掉 null
                    .collect(Collectors.toList());
            List<ST_PPTN_RPojo> pptnList=data.selectListLS(stcdListD,stime,etime,null);
            pptnList=pptnList.stream().filter(p->p.getDRP()>=0).collect(toList());//有-99是缺测的
            if(listDistanceTemp.size()>0){
                List<ST_PPTN_RPojo> pptnListDistance5=new ArrayList<>();//最近的5个站
                for (int i = 0; i < listDistanceTemp.size(); i++) {
                    ST_STBPRP_B_DISTANCEPojo distancePojo=listDistanceTemp.get(i);
                    //最近的一个有数据的站
                    List<ST_PPTN_RPojo> pptnListDistance=pptnList.stream().filter(pptnb -> pptnb.getSTCD().equals(distancePojo.getStcddis())&&pptnb.getTM().equals(tm)).collect(Collectors.toList());
                    if(pptnListDistance.size()>0){
                        pptnListDistance5.add(pptnListDistance.get(0));
                    }
                    if(pptnListDistance5.size()>=5){
                        break; // 直接跳出
                    }
                }
                if(pptnListDistance5.size()>0){
                    List<String> bStcdList =pptnListDistance5.stream()
                            .map(ST_PPTN_RPojo::getSTCD)
                            .filter(stcd -> stcd != null) // 过滤掉 null
                            .collect(Collectors.toList());
                    List<String> finalBStcdList = bStcdList;
                    String stcddis= String.join(",",finalBStcdList);


                    double sumDRP = pptnListDistance5.stream()
                            .filter(item -> item.getDRP() != null) // 过滤掉 DRP 为 null 的项
                            .mapToDouble(ST_PPTN_RPojo::getDRP)   // 将 DRP 提取为 double 类型
                            .sum();
                    double avgDrp=sumDRP/pptnListDistance5.size(); //求平均
                    // 计算非null值的数量
                    long validCount = pptnListDistance5.stream()
                            .filter(item -> item.getDRP() != null)
                            .count();
                    // 使用BigDecimal进行精确计算并四舍五入保留一位小数
                    BigDecimal average = BigDecimal.valueOf(sumDRP)
                            .divide(BigDecimal.valueOf(validCount), 1, RoundingMode.HALF_UP);
                    avgDrp=average.doubleValue();

                    ST_PPTN_R_ABPojo pojo=new  ST_PPTN_R_ABPojo();
                    pojo.setSTCD(u.getSTCD());
                    pojo.setTM(u.getTM());
                    pojo.setTYPE(u.getTYPE());
                    pojo.setCONTENT(u.getCONTENT());
                    pojo.setSTCDDIS(stcddis);
                    pojo.setSTCDDISDRP(avgDrp);
                    listData.add(pojo);
                }
            }
        });
        watch.stop();
        int num=0;
        if(listData.size()>0){
            int count = 80;
            int number = listData.size() / count;
            if (listData.size() % count != 0){
                number += 1;
            }
            List<ST_PPTN_R_ABPojo> list = new ArrayList<>();
            for(int i=0;i<number;i++){
                if(i == number - 1){
                    list = listData.subList(count * i,listData.size());
                }else {
                    list = listData.subList(count * i,count * (i + 1));
                }
                num += st_pptn_r_abData.updateALL(list);
            }
        }
        return commonUtills.returnJson("insert",num,listData);
//        if(null != listData){
//            return commonUtills.returnJson("select",listData.size(),listData);
//        }else {
//            return commonUtills.returnJson("select", -1, listData);
//        }
    }



    @RequestMapping("/selectListTsdb")
    public Map<String,Object> selectListTsdb(@RequestBody ColumnName param){
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime="",//DateUtil.dateFormat(date,"yyyy-MM-dd 08:00:00"),
                etime="",type="Minute";
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
        List<RTSQPojo> pptnList=getRTSQ_5MINXZYL(stcdList,stime,etime);
        if(null != pptnList){
            return commonUtills.returnJson("select",pptnList.size(),pptnList);
        }else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }


    //遥测库5分钟雨量数据：修正后的
    public List<RTSQPojo> getRTSQ_5MINXZYL(List<String> stcdList, String stime, String etime) {
        List<RTSQPojo> list = new ArrayList<>();
        List<TSDBPojo> listTsdb=tsdbData.selectList(stcdList,stime,etime);
        try {
            for (TSDBPojo tsdb : listTsdb) {
                Date time =DateUtil.strToDate( tsdb.getTime(),DateUtil.YMDHMS);
                String senid = tsdb.getSenid().toString();
                for (int i = 0; i < 12; i++) {
                    Date tm= new Date(time.getTime() + i * 5 * 60 * 1000);
                    String tmStr=DateUtil.dateFormat(tm,"yyyy-MM-dd HH:mm:ss"); // 增加分钟数
                    RTSQPojo dto = new RTSQPojo();
                    dto.setSENID(senid);
                    dto.setTIME(tmStr);
                    // 获取对应的 v0 到 v11 值
                    String fieldName = "v" + i;
                    Double factv =commonUtills.getFieldValue(tsdb, fieldName);
                    dto.setFACTV(factv);
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    //读取浦东提供的zrx文件并入库（st_pptn_r）
    @RequestMapping("/readZrx")
    public Map<String,Object> readZrx(@RequestBody ColumnName param){
        // 替换为你的.zrx文件路径
        String zrxFilePath =templatefilepath+ "/Rainfall.zrx";
        if(param.getStrExp()!=null){
            zrxFilePath =templatefilepath+"/"+param.getStrExp();
        }
        else {
            zrxFilePath =templatefilepath+ "/Rainfall.zrx";
        }
////        List<ZrxDataPojo> zrxDataList=new ArrayList<>();
////        try {
////            // 读取并解析zrx文件
////           zrxDataList=ZrxStructuredTextReader.readAndParseZrx(zrxFilePath);
////            // 输出解析结果
////            System.out.println("解析到 " + zrxDataList.size() + " 条数据：");
////            for (ZrxDataPojo data : zrxDataList) {
////                System.out.println(data);
////            }
////        } catch (IOException e) {
////            System.err.println("读取.zrx文件失败：" + e.getMessage());
////            e.printStackTrace();
////        }
//        if(null != zrxDataList){
//            return commonUtills.returnJson("select",zrxDataList.size(),zrxDataList);
//        }else {
//            return commonUtills.returnJson("select", -1, zrxDataList);
//        }

        String filePath =zrxFilePath;
        // 定义与字符串匹配的格式（注意：空格分隔日期和时间）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<ZrxRainfallParser.RainfallRecord> data=new ArrayList<>();
        try {
            // 设置时间范围（例如只读最近一年）
            LocalDateTime start = LocalDateTime.now().minusYears(1);
            LocalDateTime end = LocalDateTime.now();
            if(param.getStime()!=null){
                start=LocalDateTime.parse(param.getStime(),formatter);
            }
            if(param.getEtime()!=null){
                end=LocalDateTime.parse(param.getEtime(),formatter);
            }
            List<String> stcdList = new ArrayList<>();
            List<String> typeList = new ArrayList<>();
            if(param.getStcd()!=null){
                stcdList=Arrays.asList(param.getStcd().split(","));
            }
            if(param.getDatasource()!=null){
                typeList=Arrays.asList(param.getDatasource().split(","));
            }
            List<ST_STBPRP_B_STCDPojo> listStcd=st_stbprp_b_stcdData.selectList(stcdList,typeList,null);

//            for(int year=2013;year<=2025;year++){
//                start=LocalDateTime.parse(year+"-01-01 00:00:00",formatter);
//                end=LocalDateTime.parse(year+"-12-31 23:59:59",formatter);
                String outputPath  =templatefilepath+ "output/rainfall_"+start.getYear()+".csv";
                ZrxRainfallParser parser = new ZrxRainfallParser();
                data = parser.parseZrxFile(filePath, start, end,listStcd);
                // ✅ 关键步骤：按时间戳升序排序
                data.sort(Comparator.comparing(ZrxRainfallParser.RainfallRecord::getTimestamp));
//                parser.exportToExcel(data,outputPath);
                parser.exportToCsv(data,outputPath);
                System.out.println(start.getYear()+"年成功读取 " + data.size() + " 条有效雨量记录");
//            for (int i = 0; i < Math.min(10, data.size()); i++) {
////                System.out.println(data.get(i));
////            }
//            }
        } catch (IOException e) {
            System.err.println("文件读取失败：" + e.getMessage());
            e.printStackTrace();
        }
        if(null != data){
            return commonUtills.returnJson("select",data.size(),null);
        }else {
            return commonUtills.returnJson("select", -1, null);
        }
    }
}
