package api.controller.swzzmxsq;

import api.data.swzzmxsq.ST_STBPRP_B_DISTANCEData;
import api.pojo.swzzmxsq.ST_STBPRP_B_DISTANCEPojo;
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
@RequestMapping("/ST_STBPRP_B_DISTANCE")
public class ST_STBPRP_B_DISTANCEController {
    @Autowired
    private ST_STBPRP_B_DISTANCEData data;

    @Autowired
    private CommonUtills commonUtills;

    @RequestMapping("/findResult")
    public Map<String, Object> findResult(@RequestBody ColumnName param){
        StopWatch watch = new StopWatch();
        watch.start();
        String sttp = "";
        List<String> STCDList = new ArrayList<>();
        if(null != param.getStcd()){
            STCDList = Arrays.asList(param.getStcd().split(","));
        }
        if(null != param.getPathname()){
            sttp = param.getPathname();
        }
        List<ST_STBPRP_B_DISTANCEPojo> treeList = data.selectList(STCDList,sttp);
        watch.stop();
        if(null != treeList){
            return commonUtills.returnJson("select",treeList.size(),treeList);
        }else {
            return commonUtills.returnJson("select", -1, treeList);
        }
    }
    @RequestMapping("/add")
    public Map<String, Object> insertOne(@RequestBody ST_STBPRP_B_DISTANCEPojo quPojo){
        StopWatch watch = new StopWatch();
        watch.start();
        Integer integer = data.insertOne(quPojo);
        watch.stop();
        return commonUtills.returnJson("insert",integer,null);
    }
    @RequestMapping("/modify")
    public Map<String, Object> upDateOne(@RequestBody ST_STBPRP_B_DISTANCEPojo quPojo){
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
}
