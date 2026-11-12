package api.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
@Data
public class DENGZHIXIANYQPojo {
    private String LEVELDATA ;
    private String LEVELRANGE;
}
