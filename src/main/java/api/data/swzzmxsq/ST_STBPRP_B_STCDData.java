package api.data.swzzmxsq;

import api.pojo.swzzmxsq.ST_PPTN_R_LOGPojo;
import api.pojo.swzzmxsq.ST_STBPRP_B_STCDPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ST_STBPRP_B_STCDData {
    List<ST_STBPRP_B_STCDPojo> selectList(@Param(value = "stcdList") List<String> stcdList,
                                          @Param(value = "typeList") List<String> typeList,
                                          @Param(value = "sourceList") List<String> sourceList);

    Integer deleteOne(@Param(value = "STCD") String STCD,@Param(value = "TYPE") String TYPE);

    Integer  upDateOne(ST_STBPRP_B_STCDPojo bPojo);
}
