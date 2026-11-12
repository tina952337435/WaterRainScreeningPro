package api.pojo.swzzmxsq;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ST_PPTN_RPojo {
    /**
     *站码
     */
    private String STCD;
    /**
     *时间
     */
    private String TM;
    /**
     *降雨量
     */
    private Double DRP;
    private Double INTV;
    private Double PDR;
    private Double DYP;
    private String WTH;

    private String STNM;
    /**
     *区镇
     */
    private String ADMAUTH;
    /**
     *河流名称
     */
    private String RVNM;
    /**
     *流域名称
     */
    private String BSNM;

    private Double LGTD;
    private Double LTTD;
}
