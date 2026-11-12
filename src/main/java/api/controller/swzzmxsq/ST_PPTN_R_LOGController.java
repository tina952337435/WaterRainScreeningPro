package api.controller.swzzmxsq;

import api.data.swzzmxsq.ST_PPTN_R_LOGData;
import api.pojo.swzzmxsq.ST_PPTN_R_ABPojo;
import api.pojo.swzzmxsq.ST_PPTN_R_LOGPojo;
import api.tools.ColumnName;
import api.tools.CommonUtills;
import api.tools.DateUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/ST_PPTN_R_LOG")
public class ST_PPTN_R_LOGController {
    @Autowired
    private ST_PPTN_R_LOGData data;

    @Autowired
    private CommonUtills commonUtills;

    @RequestMapping("/findResult")
    public Map<String, Object> findResult(@RequestBody ColumnName param){
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime="",etime="";
        StopWatch watch = new StopWatch();
        watch.start();
        String sttp = "",key="",datatm="";
        List<String> STCDList = new ArrayList<>();
        if(null != param.getStcd()){
            STCDList = Arrays.asList(param.getStcd().split(","));
        }
        if(null != param.getPathname()){
            sttp = param.getPathname();
        }
        if(null != param.getKey()){
            key = param.getKey();
        }
        if(null != param.getStime()){
            stime = param.getStime();
        }
        if(null != param.getEtime()){
            etime = param.getEtime();
        }
        if(null != param.getTm()){
            datatm = param.getTm();
        }

        List<ST_PPTN_R_LOGPojo> treeList = data.selectList(STCDList,sttp,key,stime,etime,datatm);
        watch.stop();
        if(null != treeList){
            return commonUtills.returnJson("select",treeList.size(),treeList);
        }else {
            return commonUtills.returnJson("select", -1, treeList);
        }
    }

    @RequestMapping("/findResultpage")
    public Map<String, Object> selectListpage(@RequestBody ColumnName param) {
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime = "",   etime = "", type = "",note="",pageindex = "",pagesize = "10",datatm="";
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
            note=param.getDatasource();//规则类型
        }
        if(null!=param.getTm()) {
            datatm=param.getTm();//被修改的数据所在时刻
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
        List<ST_PPTN_R_LOGPojo> pptnList = data.selectListpage(stcdList, type,note,stime,etime,datatm,startindex,Integer.valueOf(pagesize));
        Integer integer = data.selectCount(stcdList, type,note,stime, etime,datatm);
        if (null != pptnList) {
            if(!"".equals(pageindex) && !"".equals(pagesize)){
                return commonUtills.returnJsonPage("select", integer, pptnList,Integer.valueOf(pagesize),Integer.valueOf(pageindex));
            }
            else{
                return commonUtills.returnJson("select", integer, pptnList);
            }

        } else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }


    @RequestMapping("/add")
    public Map<String, Object> insertOne(@RequestBody ST_PPTN_R_LOGPojo quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.insertOne(quPojo);
        watch.stop();
        return commonUtills.returnJson("insert",integer,null);
    }
    @RequestMapping("/modify")
    public Map<String, Object> upDateOne(@RequestBody ST_PPTN_R_LOGPojo quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.upDateOne(quPojo);
        watch.stop();
        return commonUtills.returnJson("insert",integer,null);
    }
    @RequestMapping("/remove")
    public Map<String, Object> deleteOne(@RequestBody ColumnName param){
        StopWatch watch = new StopWatch();
        watch.start();
        String id = "";
        if(null != param.getStcd()){
            id = param.getStcd();
        }
        Integer integer = data.deleteOne(id);
        watch.stop();
        return commonUtills.returnJson("insert",integer,null);
    }

    @RequestMapping("/addAll")
    public Map<String,Object> addAll(@RequestBody List<ST_PPTN_R_LOGPojo> quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.insertAll(quPojo);
        watch.stop();
        return commonUtills.returnJson("insert",integer,quPojo);
    }
}
