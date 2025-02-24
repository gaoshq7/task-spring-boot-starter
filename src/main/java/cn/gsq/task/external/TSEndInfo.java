package cn.gsq.task.external;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Project : galaxy
 * Class : cn.gsq.task.external.TSEndInfo
 *
 * @author : gsq
 * @date : 2023-11-30 14:40
 * @note : It's not technology, it's art !
 **/
@Getter
@Setter
@Accessors(chain = true)
public class TSEndInfo extends TSBase {

    private final String sname;   // 任务步骤名称

    private Boolean success; // 运行结果

    private Long time;  // 运行时间

    private BigDecimal percent; // 运行任务完成的百分比

    /**
     * @Description : task stage构造器
     * @Param : [id]
     * @Return :
     * @Author : gsq
     * @Date : 17:55
     * @note : An art cell !
     */
    private TSEndInfo(String id, String sname) {
        super(id);
        this.sname = sname;
    }

    /**
     * @Description : 创建stage end info
     * @Param : [tid, sid, isSuccess, percent]
     * @Return : cn.gsq.task.external.TSEndInfo
     * @Author : gsq
     * @Date : 14:50
     * @note : An art cell !
    **/
    public static TSEndInfo build(String id, String sname, Boolean isSuccess, Long time,BigDecimal percent) {
        return new TSEndInfo(id, sname).setSuccess(isSuccess).setTime(time).setPercent(percent);
    }

}
