package api.data.swzzmxsq;

import api.pojo.swzzmxsq.ST_PPTN_RPojo;
import api.pojo.swzzmxsq.ST_PPTN_R_ABPojo;
import api.pojo.swzzmxsq.ST_PPTN_R_LOGPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ST_PPTN_R_ABData {
    List<ST_PPTN_R_ABPojo> selectList(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime,
                                      @Param(value = "pathname") List<String> pathname,@Param(value = "status") String status,@Param(value = "content") String content,
                                      @Param(value = "datasource") String datasource,
                                      @Param(value = "startindex") Integer startindex,@Param(value = "pagesize") Integer pagesize);


    List<ST_PPTN_R_ABPojo> selectListpage(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime,
                                          @Param(value = "pathname") String pathname,@Param(value = "status") String status,@Param(value = "datasource") String datasource,
                                      @Param(value = "startindex") Integer startindex,@Param(value = "pagesize") Integer pagesize);


    Integer  selectCount(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime,
                         @Param(value = "pathname") String pathname,@Param(value = "status") String status,@Param(value = "datasource") String datasource);

    List<ST_PPTN_R_ABPojo>  selectListByRules(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime,@Param(value = "pathname") String pathname,@Param(value = "stlc") String stlc);
    List<ST_PPTN_R_ABPojo>  selectListByStlc(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime,@Param(value = "pathname") String pathname,@Param(value = "stlc") String stlc);
    List<ST_PPTN_R_ABPojo>  selectListByYear(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime,@Param(value = "pathname") String pathname,@Param(value = "stlc") String stlc);

//    临近站和相邻站偏差用日数据

    List<ST_PPTN_R_ABPojo>  selectListByRulesAddDay(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime,@Param(value = "pathname") String pathname,@Param(value = "stlc") String stlc);

    List<ST_PPTN_R_ABPojo>  selectListByStlcAddDay(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime,@Param(value = "pathname") String pathname,@Param(value = "stlc") String stlc);

    List<ST_PPTN_R_ABPojo>   selectListByYearAddDay(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime,@Param(value = "pathname") String pathname,@Param(value = "stlc") String stlc);


    Integer upDateOne(ST_PPTN_R_ABPojo bPojo);
    Integer insertAll(@Param(value = "quPojo") List<ST_PPTN_R_ABPojo> quPojo);

    Integer updateALL(@Param(value = "objList")List<ST_PPTN_R_ABPojo> objList);

    Integer deleteOne(@Param(value = "STCD") String STCD,@Param(value = "STCD") String  TM,@Param(value = "TYPE") String  TYPE,@Param(value = "CONTENT") String CONTENT );

    Integer deleteAll(@Param(value = "list") List<ST_PPTN_R_ABPojo> list);
}
