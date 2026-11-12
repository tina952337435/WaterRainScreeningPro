package api.data.swzzzjk;

import api.pojo.swzzzjk.RTEVPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RTEVData {
    List<RTEVPojo> selectList(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime);
}
