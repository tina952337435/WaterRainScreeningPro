package api.pojo.swzzmxsq;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TsvDataPojo {
    private String senId;
    private Date time;
    private double[] vValues = new double[12];
    private int[] sValues = new int[12];
    private String rowId;
    private String source;//来源
}
