package api.data.swzzmxsq;

import api.pojo.swzzmxsq.ST_STBPRP_BPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ST_STBPRP_BData {

    List<ST_STBPRP_BPojo> selectList(@Param(value = "STCDList") List<String> STCDList,@Param(value = "STNM") String STNM,@Param(value = "key") String key);

    List<ST_STBPRP_BPojo> selectLists(@Param(value = "STCDList") List<String> STCDList,@Param(value = "STNM") String STNM,@Param(value = "key") String key);

    Integer insertOne(ST_STBPRP_BPojo bPojo);

    Integer upDateOne(ST_STBPRP_BPojo bPojo);

    Integer deleteOne(@Param(value = "STCD") String STCD);
}
