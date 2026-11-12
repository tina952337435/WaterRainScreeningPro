package api.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class WQBASEWATERPojo {
    /**
     * 圩区编号
     */
    private String WQID;
    /**
     * 圩区实时水位（智能调度时启用）
     */
    private Double UPZ;
    /**
     * 圩区安全水位
     */
    private Double WQGRZ;
    /**
     * 圩区起排水位
     */
    private Double WQWRZ;
    /**
     * 工程排水量
     */
    private Double SUMPS;
    /**
     * 累计降雨
     */
    private Double SUMDRP;
    /**
     * 圩区纳雨能力（纳雨量）
     */
    private Double WQDRP;
    /**
     * 预报降雨
     */
    private String YBDRPLIST;
    /**
     * 径流系数（智能调度时启用）
     */
    private String WQJLXS;
    /**
     * 排水时间
     */
    private String PSSJ;
    /**
     * 智能调度（智能调度时启用）
     */
    private String ZNDDSUMPS;
    /**
     * 工情
     */
    private String GATERLIST;

    /**
     * 开始时间、结束时间
     */
    private String stime;
    private String etime;

    /**
     *  预报、智能调度
     */
    private String pathname;
}
