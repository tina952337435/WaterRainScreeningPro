package api.pojo.swzzmxsq;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ST_STBPRP_B_DISTANCEPojo {
    private String stcd;
    private String stcddis;
    private Double distance;
    private String sttp;
    private String note;
    private String content;
    private String marks;
}
