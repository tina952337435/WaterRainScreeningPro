package api.data.swzzmxsq;

import api.pojo.swzzmxsq.ST_STBPRP_B_DISTANCEPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ST_STBPRP_B_DISTANCEData {

    List<ST_STBPRP_B_DISTANCEPojo> selectList(@Param(value = "STCDList") List<String> STCDList,@Param(value = "sttp") String sttp);

    Integer insertOne(ST_STBPRP_B_DISTANCEPojo bPojo);

    Integer upDateOne(ST_STBPRP_B_DISTANCEPojo bPojo);

    Integer deleteOne(@Param(value = "stcd") String stcd);
}
