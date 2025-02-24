package cn.gsq.task.external;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * Project : galaxy
 * Class : cn.gsq.task.external.TSCompleteInfo
 *
 * @author : gsq
 * @date : 2023-11-30 14:53
 * @note : It's not technology, it's art !
 **/
@Getter
@Setter
@Accessors(chain = true)
public class TSCompleteInfo<T extends ITRHandler> extends TSBase {

    private Boolean success;    // 任务最终结果

    private Report report;  // 任务报告

    private T handler;       // 任务结果处理器

    /**
     * @Description : task stage构造器
     * @Param : [id]
     * @Return :
     * @Author : gsq
     * @Date : 17:55
     * @note : An art cell !
     */
    private TSCompleteInfo(String id) {
        super(id);
    }

    /**
     * @Description : 创建task stage complete实例
     * @Param : [tid, isSuccess, report, result]
     * @Return : cn.gsq.task.external.TSCompleteInfo<T>
     * @Author : gsq
     * @Date : 15:02
     * @note : An art cell !
    **/
    public static <X extends ITRHandler> TSCompleteInfo<X> build(String tid, Boolean isSuccess, Report report, X handler) {
        return new TSCompleteInfo<X>(tid).setSuccess(isSuccess).setReport(report).setHandler(handler);
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Report {

        private String id;  // 任务ID

        private String name;    // 任务名称

        private Boolean success;    // 是否成功

        private BigDecimal percent; // 任务完成度

        private String msg;     // 任务信息

        private List<Step> steps; // 任务步骤

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Step {

        private String name;    // 步骤名称

        private Boolean success;    // 是否成功

        private Long time;  // 运算耗时

        private List<Operation> operations; // 操作集合

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Operation {

        private String id;  // 运算id

        private String name;    // 运算名称

        private String sname;   // 所属步骤名称

        private String type;    // 运算类型

        private Boolean success;    // 是否成功

        private String start;     // 开始时间

        private String end;     // 结束时间

        private Long time;  // 运算耗时

        private String msg;     // 运算信息

    }

}
