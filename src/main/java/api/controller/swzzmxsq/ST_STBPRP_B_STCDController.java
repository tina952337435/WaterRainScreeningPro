package api.controller.swzzmxsq;

import api.data.swzzmxsq.ST_STBPRP_B_STCDData;
import api.pojo.swzzmxsq.ST_PPTN_R_LOGPojo;
import api.pojo.swzzmxsq.ST_STBPRP_B_STCDPojo;
import api.tools.ColumnName;
import api.tools.CommonUtills;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/ST_STBPRP_B_STCD")
public class ST_STBPRP_B_STCDController {
    @Autowired
    private ST_STBPRP_B_STCDData data;

    @Autowired
    private CommonUtills commonUtills;
    @RequestMapping("/findResult")
    public Map<String, Object> selectList(@RequestBody ColumnName param){
        StopWatch watch = new StopWatch();
        watch.start();
        String STCD = "",STNM = "",PID = "";
        List<String> stcdList = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        List<String> sourceList = new ArrayList<>();
        if(null != param.getStcd()){
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if(null != param.getPathname()){
            typeList = Arrays.asList(param.getPathname().split(","));
        }
        if(null != param.getDatasource()){
            sourceList = Arrays.asList(param.getDatasource().split(","));
        }
        List<ST_STBPRP_B_STCDPojo> treeList = data.selectList(stcdList,typeList,sourceList);
        watch.stop();
        if(null != treeList){
            return commonUtills.returnJson("select",treeList.size(),treeList);
        }else {
            return commonUtills.returnJson("select", -1, treeList);
        }
    }

    @RequestMapping("/modify")
    public Map<String, Object> upDateOne(@RequestBody ST_STBPRP_B_STCDPojo quPojo){
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
        String stcd = "";
        if(null != param.getStcd()){
            stcd = param.getStcd();
        }
        String type = "";
        if(null != param.getPathname()){
            type = param.getPathname();
        }

        Integer integer = 0;
        if(StringUtils.isNotEmpty(stcd) && StringUtils.isNotEmpty(type)){
            integer = data.deleteOne(stcd,type);
        }
        watch.stop();
        return commonUtills.returnJson("insert",integer,null);
    }

}
