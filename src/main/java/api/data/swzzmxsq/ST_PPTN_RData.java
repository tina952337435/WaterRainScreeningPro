package api.data.swzzmxsq;

import api.pojo.swzzmxsq.ST_PPTN_RPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ST_PPTN_RData {
    List<ST_PPTN_RPojo> selectListIsDay(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime);

    List<ST_PPTN_RPojo> selectListIsLast(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime);

    List<ST_PPTN_RPojo> selectList(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime);

    List<ST_PPTN_RPojo> selectListByTime(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime);


    List<ST_PPTN_RPojo> selectLists(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime);

    List<ST_PPTN_RPojo> selectListLS(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime,@Param(value = "wth") String wth);

    /*
    *  过滤掉降雨量为0的记录
    * */
    List<ST_PPTN_RPojo> selectListByTimeNull(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime);

    List<ST_PPTN_RPojo> selectListTotal(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime);

    List<ST_PPTN_RPojo> selectListHourly(@Param(value = "stcdList") List<String> stcdList,@Param(value = "stime") String stime,@Param(value = "etime") String etime);

    Integer insertAll(@Param(value = "quPojo") List<ST_PPTN_RPojo> quPojo);

    Integer upDateOne(ST_PPTN_RPojo bPojo);

    Integer updateALL(@Param(value = "objList")List<ST_PPTN_RPojo> objList);
}
