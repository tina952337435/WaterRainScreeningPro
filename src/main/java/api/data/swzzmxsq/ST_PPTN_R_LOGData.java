package api.data.swzzmxsq;

import api.pojo.swzzmxsq.ST_PPTN_R_ABPojo;
import api.pojo.swzzmxsq.ST_PPTN_R_LOGPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ST_PPTN_R_LOGData {

    List<ST_PPTN_R_LOGPojo> selectList(@Param(value = "STCDList") List<String> STCDList,
                                       @Param(value = "pathname") String pathname,
                                       @Param(value = "key") String key,
                                       @Param(value = "stime") String stime,
                                       @Param(value = "etime") String etime,
                                       @Param(value = "datatm") String datatm);

    List<ST_PPTN_R_LOGPojo> selectListpage(@Param(value = "STCDList") List<String> STCDList,
                                           @Param(value = "pathname") String pathname,
                                           @Param(value = "key") String key,
                                           @Param(value = "stime") String stime,
                                           @Param(value = "etime") String etime,
                                           @Param(value = "datatm") String datatm,
                                           @Param(value = "startindex") Integer startindex,
                                           @Param(value = "pagesize") Integer pagesize);

    Integer  selectCount(@Param(value = "STCDList") List<String> STCDList,
                         @Param(value = "pathname") String pathname,
                         @Param(value = "key") String key,
                         @Param(value = "stime") String stime,
                         @Param(value = "etime") String etime,
                         @Param(value = "datatm") String datatm);

    Integer insertOne(ST_PPTN_R_LOGPojo bPojo);

    Integer upDateOne(ST_PPTN_R_LOGPojo bPojo);

    Integer deleteOne(@Param(value = "STCD") String STCD);

    Integer insertAll(@Param(value = "quPojo") List<ST_PPTN_R_LOGPojo> quPojo);
}
