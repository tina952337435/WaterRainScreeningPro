package api.pojo.swzzzjk;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RTSQPojo {
    private String SENID;//站码
    private String TIME;//时间
    private Double FACTV;//值
    private Double IFCH;
    private Double CYCLE;
    private Double STATE;
    private Double TS;
}