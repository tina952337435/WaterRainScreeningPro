package api.pojo.swzzmxsq;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZrxDataPojo {
    private String timestamp; // 时间戳
    private String value;     // 数值
    private String sname;     // 新增：SNAME对应的值
}
