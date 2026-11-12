package api.data.swzzzjk;

import api.pojo.swzzzjk.TSDBPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TSDBData {
    List<TSDBPojo> selectList(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime);
}
