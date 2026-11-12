package api.pojo.swzzmxsq;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ST_PPTN_R_LOGPojo {
    private String id;
    private String tm;//修改时间
    private Double  bvalue;//修改之前的值
    private Double avalue;//修改之后的值
    private String  ower;//编辑人员，修改人员
    private String type;//类型，修改的是水位还是雨量等
    private String note;
    private String content;
    private String marks;
    private String datatm;//修改的数据时间
    private String stcd;//站码
    private String stnm;//站码
}