package api.controller.swzzzjk;

import Equivalent.MyDZXUtils;
import Equivalent.ObjUtils;
import api.data.swzzzjk.RTEVData;
import api.pojo.swzzzjk.RTEVPojo;
import api.tools.ColumnName;
import api.tools.CommonUtills;
import api.tools.DateUtil;
import api.tools.FilePathUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/RTEV")
public class RTEVController {
    @Autowired
    private RTEVData data;

    //文件存储路径
    @Value("${file.path.templatefilepath}")
    private String templatefilepath;

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
        List<RTEVPojo> pptnList = new ArrayList<>();
        pptnList=data.selectList(stcdList,stime,etime);
        if(null != pptnList){
            return commonUtills.returnJson("select",pptnList.size(),pptnList);
        }else {
            return commonUtills.returnJson("select", -1, pptnList);
        }
    }
}
