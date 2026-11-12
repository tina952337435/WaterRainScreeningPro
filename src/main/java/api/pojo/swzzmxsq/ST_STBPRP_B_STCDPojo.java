package api.pojo.swzzmxsq;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ST_STBPRP_B_STCDPojo {

    private String STCD;
    private String STNM;
    private String ZSTCD;
    private Double UPZ;
    private Double DWZ;
    private String ZTM;
    private String ZD;
    private Integer TIMEINTERVAL;
    private String TYPE;
    private String SOURCE;
    private String ISSTATUS;
    private String LASTTM;
    private String REMARK;

    private String DAYLASTTM;//日数据最后统计时间
    private String MONTHLASTTM;//月数据最后统计时间
    private String XUNLASTTM;//旬数据最后统计时间
    private String YEARLASTTM;//年数据最后统计时间
}
