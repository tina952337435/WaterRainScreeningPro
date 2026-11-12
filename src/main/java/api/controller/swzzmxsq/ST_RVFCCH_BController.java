package api.controller.swzzmxsq;

import api.data.swzzmxsq.ST_RVFCCH_BData;
import api.pojo.swzzmxsq.ST_RVFCCH_BPojo;
import api.tools.ColumnName;
import api.tools.CommonUtills;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ST_RVFCCH_B")
public class ST_RVFCCH_BController {

    @Autowired
    private ST_RVFCCH_BData data;

    @Autowired
    private CommonUtills commonUtills;

    @RequestMapping("/selectList")
    public Map<String, Object> selectList(@RequestBody ST_RVFCCH_BPojo param){
        StopWatch watch = new StopWatch();
        watch.start();
        String STCD = "";
        if(null != param.getSTCD()){
            STCD = param.getSTCD();
        }
        List<ST_RVFCCH_BPojo> treeList = data.selectList(STCD);
        watch.stop();
        if(null != treeList){
            return commonUtills.returnJson("select",treeList.size(),treeList);
        }else {
            return commonUtills.returnJson("select", -1, null);
        }
    }
    @RequestMapping("/add")
    public Map<String, Object> insertOne(@RequestBody ST_RVFCCH_BPojo quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.insertOne(quPojo);
        watch.stop();
        return commonUtills.returnJson("select",integer, null);
    }
    @RequestMapping("/modify")
    public Map<String, Object> upDateOne(@RequestBody ST_RVFCCH_BPojo quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.upDateOne(quPojo);
        watch.stop();
        return commonUtills.returnJson("select" ,integer, null);
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
        return commonUtills.returnJson("select",integer, null);
    }
}
