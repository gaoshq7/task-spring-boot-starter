package cn.gsq.task.external;

import cn.hutool.core.util.IdUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Project : galaxy
 * Class : cn.gsq.task.external.TSSubmitInfo
 *
 * @author : gsq
 * @date : 2023-11-30 14:01
 * @note : It's not technology, it's art !
 **/
@Getter
@Setter
@Accessors(chain = true)
public class TSSubmitInfo extends TSBase {

    private String name;    // 任务名称（操作）

    private List<String> stages; // 任务所有步骤（下拉步骤展示）

    private ITType type;   // 任务类型（使用者自行实现该接口）

    /**
     * @Description : task stage构造器
     * @Param : [id]
     * @Return :
     * @Author : gsq
     * @Date : 17:55
     * @note : An art cell !
     */
    private TSSubmitInfo(String id) {
        super(id);
    }

    /**
     * @Description : 任务提交实例
     * @Param : [name, stages, type]
     * @Return : cn.gsq.task.external.TSSubmitInfo
     * @Author : gsq
     * @Date : 14:23
     * @note : An art cell !
    **/
    public static TSSubmitInfo build(String name, ITType type, List<String> stages) {
        String id = IdUtil.fastSimpleUUID();
        return new TSSubmitInfo(id).setName(name).setStages(stages).setType(type);
    }

}
