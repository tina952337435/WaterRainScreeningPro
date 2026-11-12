package api.data.swzzmxsq;

import api.pojo.swzzmxsq.ST_PPTN_R_QUPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ST_PPTN_R_QUData {
    List<ST_PPTN_R_QUPojo> selectList(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime);
    
    Integer insertAll(@Param(value = "quPojo") List<ST_PPTN_R_QUPojo> quPojo);

    Integer upDateOne(ST_PPTN_R_QUPojo bPojo);
}
