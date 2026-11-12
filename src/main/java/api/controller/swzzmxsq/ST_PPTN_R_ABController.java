package api.controller.swzzmxsq;
import api.data.swzzmxsq.ST_PPTN_RData;
import api.data.swzzmxsq.ST_PPTN_R_ABData;
import api.pojo.swzzmxsq.ST_PPTN_RPojo;
import api.pojo.swzzmxsq.ST_PPTN_R_ABPojo;
import api.pojo.swzzmxsq.ST_PPTN_R_LOGPojo;
import api.tools.ColumnName;
import api.tools.CommonUtills;
import api.tools.DateUtil;
import api.tools.FilePathUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/ST_PPTN_R_AB")
public class ST_PPTN_R_ABController {
    @Autowired
    private ST_PPTN_R_ABData data;

    @Autowired
    private ST_PPTN_RData st_pptn_rData;


    //文件存储路径
    @Value("${file.path.templatefilepath}")
    private String templatefilepath;

    @Autowired
    private CommonUtills commonUtills;

    @RequestMapping("/findResult")
    public Map<String, Object> selectList(@RequestBody ColumnName param) {
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime = "",   etime = "",status="",pageindex = "",pagesize = "10",content="minute",datasource="";
        List<String> stcdList = new ArrayList<>(), typeList =new ArrayList<>();
        if (null != param.getStcd()&&!param.getStcd().equals("")) {
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if (null != param.getPathname()) {
            typeList = Arrays.asList(param.getPathname().split(","));
        }
        if (null != param.getStime()) {
            stime = param.getStime();
        }
        if (null != param.getEtime()) {
            etime = param.getEtime();
        }
        if(null!=param.getDatasource()){
            status=param.getDatasource();//状态：是否已经修正
        }
        if(null != param.getPageindex()){
            pageindex = param.getPageindex();
        }
        if(null != param.getPagesize()){
            pagesize = param.getPagesize();
        }
        Integer startindex = null;
        if(!"".equals(pageindex) && !"".equals(pagesize)){
            startindex = (Integer.parseInt(pageindex) - 1) * Integer.parseInt(pagesize);
        }
        if (null != param.getStrExp()) {
            content = param.getStrExp();
        }
        if (null != param.getPid()) {
            datasource = param.getPid();
        }


        List<ST_PPTN_R_ABPojo> pptnList = data.selectList(stcdList, stime, etime, typeList,status,content,datasource,startindex,Integer.valueOf(pagesize));

//        Integer integer = data.selectCount(stcdList, stime, etime, type,status);

        if (null != pptnList) {
            return commonUtills.returnJson("select", pptnList.size(), pptnList);
        } else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }

    @RequestMapping("/findResultpage")
    public Map<String, Object> selectListpage(@RequestBody ColumnName param) {
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime = "",   etime = "", type = "",status="",pageindex = "",pagesize = "10",strExp="minute";
        List<String> stcdList = new ArrayList<>();
        if (null != param.getStcd()&&!param.getStcd().equals("")) {
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if (null != param.getPathname()) {
            type = param.getPathname();
        }
        if (null != param.getStime()) {
            stime = param.getStime();
        }
        if (null != param.getEtime()) {
            etime = param.getEtime();
        }
        if(null!=param.getDatasource()){
            status=param.getDatasource();//状态：是否已经修正
        }
        if(null!=param.getStrExp()) {
            strExp=param.getStrExp();
        }
        if(null != param.getPageindex()){
            pageindex = param.getPageindex();
        }
        if(null != param.getPagesize()){
            pagesize = param.getPagesize();
        }
        Integer startindex = null;
        if(!"".equals(pageindex) && !"".equals(pagesize)){
            startindex = (Integer.parseInt(pageindex) - 1) * Integer.parseInt(pagesize);
        }
        List<ST_PPTN_R_ABPojo> pptnList = data.selectListpage(stcdList, stime, etime, type,status,strExp,startindex,Integer.valueOf(pagesize));
        Integer integer = data.selectCount(stcdList, stime, etime, type,status,strExp);
        if (null != pptnList) {
            if(!"".equals(pageindex) && !"".equals(pagesize)) {
                return commonUtills.returnJsonPage("select", integer, pptnList,Integer.valueOf(pagesize),Integer.valueOf(pageindex));
            }
            else {
                return commonUtills.returnJson("select", integer, pptnList);
            }
        } else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }

    @RequestMapping("/findResultByRules")
    public Map<String, Object> selectListByRules(@RequestBody ColumnName param) {
        String stime = "", etime = "",pathname="",stlc="";
        List<String> stcdList = new ArrayList<>();
        if (null != param.getStcd()&&!param.getStcd().equals("")) {
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if (null != param.getStime()) {
            stime = param.getStime();
        }
        if (null != param.getEtime()) {
            etime = param.getEtime();
        }
        if (null != param.getPathname()) {
            pathname = param.getPathname();
        }
        if (null != param.getDatasource()) {//分区
            stlc = param.getDatasource();
        }
        List<ST_PPTN_R_ABPojo> pptnList = data.selectListByRulesAddDay(stcdList, stime, etime,pathname,stlc);
        if (null != pptnList) {
            return commonUtills.returnJson("select", pptnList.size(), pptnList);
        } else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }

    @RequestMapping("/findResultByStlc")
    public Map<String, Object> selectListByStlc(@RequestBody ColumnName param) {
        String stime = "", etime = "",pathname="",stlc="";
        List<String> stcdList = new ArrayList<>();
        if (null != param.getStcd()&&!param.getStcd().equals("")) {
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if (null != param.getStime()) {
            stime = param.getStime();
        }
        if (null != param.getEtime()) {
            etime = param.getEtime();
        }
        if (null != param.getPathname()) {
            pathname = param.getPathname();
        }
        if (null != param.getDatasource()) {//分区
            stlc = param.getDatasource();
        }
        List<ST_PPTN_R_ABPojo> pptnList = data.selectListByStlcAddDay(stcdList, stime, etime,pathname,stlc);
        if (null != pptnList) {
            return commonUtills.returnJson("select", pptnList.size(), pptnList);
        } else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }

    @RequestMapping("/findResultByYear")
    public Map<String, Object> selectListByYear(@RequestBody ColumnName param) {
        String stime = "", etime = "",pathname="",stlc="";
        List<String> stcdList = new ArrayList<>();
        if (null != param.getStcd()&&!param.getStcd().equals("")) {
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if (null != param.getStime()) {
            stime = param.getStime();
        }
        if (null != param.getEtime()) {
            etime = param.getEtime();
        }
        if (null != param.getPathname()) {
            pathname = param.getPathname();
        }if (null != param.getDatasource()) {//分区
            stlc = param.getDatasource();
        }
        List<ST_PPTN_R_ABPojo> pptnList = data.selectListByYearAddDay(stcdList, stime, etime,pathname,stlc);
        if (null != pptnList) {
            return commonUtills.returnJson("select", pptnList.size(), pptnList);
        } else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }

    @RequestMapping("/modify")
    public Map<String, Object> upDateOne(@RequestBody ST_PPTN_R_ABPojo quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.upDateOne(quPojo);
        watch.stop();
        return commonUtills.returnJson("insert",integer,null);
    }

    @RequestMapping("/addAll")
    public Map<String,Object> addAll(@RequestBody List<ST_PPTN_R_ABPojo> quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.insertAll(quPojo);
        watch.stop();
        return commonUtills.returnJson("insert",integer,quPojo);
    }

    @RequestMapping("/modifyAll")
    public Map<String,Object>  modifyAll(@RequestBody List<ST_PPTN_R_ABPojo> quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer num = 0;
        int count = 80;
        int number = quPojo.size() / count;
        if (quPojo.size() % count != 0){
            number += 1;
        }
        List<ST_PPTN_R_ABPojo> list = new ArrayList<>();
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
}