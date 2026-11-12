package api.data.swzzmxsq;

import api.pojo.swzzmxsq.ST_SCREENINGRULESPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ST_SCREENINGRULESData {

    List<ST_SCREENINGRULESPojo> selectList(@Param(value = "STCDList") List<String> STCDList,@Param(value = "pathname") String pathname,@Param(value = "key") String key);

    Integer insertOne(ST_SCREENINGRULESPojo bPojo);

    Integer upDateOne(ST_SCREENINGRULESPojo bPojo);

    Integer deleteOne(@Param(value = "STCD") String STCD);
}
