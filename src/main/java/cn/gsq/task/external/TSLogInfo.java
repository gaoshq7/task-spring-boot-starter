package cn.gsq.task.external;

import com.yomahub.liteflow.enums.NodeTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Project : galaxy
 * Class : cn.gsq.task.external.TSLogInfo
 *
 * @author : gsq
 * @date : 2023-11-30 15:02
 * @note : It's not technology, it's art !
 **/
@Getter
@Setter
@Accessors(chain = true)
public class TSLogInfo extends TSBase {

    private final String sname;   // 步骤名称

    private final String oname;   // 算子名称

    private final NodeTypeEnum otype;   // 算子类型

    private ETLevel level;    // 详情等级

    private String msg; // 日志内容

    /**
     * @Description : task stage构造器
     * @Param : [id]
     * @Return :
     * @Author : gsq
     * @Date : 17:55
     * @note : An art cell !
     */
    private TSLogInfo(String id, String sname, String oname, String code) {
        super(id);
        this.sname = sname;
        this.oname = oname;
        this.otype = NodeTypeEnum.getEnumByCode(code);
    }

    /**
     * @Description : 创建task stage log
     * @Param : [tid, sid, level, msg]
     * @Return : cn.gsq.task.external.TSLogInfo
     * @Author : gsq
     * @Date : 15:42
     * @note : ⚠️ 步骤日志是关联在任务步骤下面的 !
    **/
    public static TSLogInfo build(String id, String sname, String oname, String code, ETLevel level, String msg) {
        return new TSLogInfo(id, sname, oname, code).setLevel(level).setMsg(msg);
    }

}
