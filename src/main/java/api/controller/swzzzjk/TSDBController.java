package api.controller.swzzzjk;

import api.data.swzzzjk.TSDBData;
import api.pojo.swzzzjk.TSDBPojo;
import api.tools.ColumnName;
import api.tools.CommonUtills;
import api.tools.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/TSDB")
public class TSDBController {
    @Autowired
    private TSDBData data;

    @Autowired
    private CommonUtills commonUtills;

    @RequestMapping("/findResult")
    public Map<String, Object> findResult(@RequestBody ColumnName param){
        Date date = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
        String stime=DateUtil.dateFormat(date,"yyyy-MM-dd 08:00:00"),etime="";
        List<String> stcdList = new ArrayList<>();
        if(null != param.getStcd()){
            stcdList = Arrays.asList(param.getStcd().split(","));
        }
        if(null != param.getStime()){
            stime = param.getStime();
        }
        if(null != param.getEtime()){
            etime = param.getEtime();
        }
        List<TSDBPojo> pptnList =data.selectList(stcdList,stime,etime);
        if(null != pptnList){
            return commonUtills.returnJson("select",pptnList.size(),pptnList);
        }else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }
}
