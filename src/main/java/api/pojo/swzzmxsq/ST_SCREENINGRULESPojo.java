package api.pojo.swzzmxsq;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ST_SCREENINGRULESPojo {
    private String  id;
    private String  name;
    private int hour;
    private Double  wvalue;
    private String  type;
    private String  note;
    private String  content;
    private String  marks;
    private Double  wvaluemax;
    private int ordernum;
}
