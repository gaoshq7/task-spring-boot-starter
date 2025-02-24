package cn.gsq.task.external;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Project : galaxy
 * Class : cn.gsq.task.external.TSStartInfo
 *
 * @author : gsq
 * @date : 2023-11-30 14:25
 * @note : It's not technology, it's art !
 **/
@Getter
@Setter
@Accessors(chain = true)
public class TSStartInfo extends TSBase {

    private String name;    // 任务stage的名称

    /**
     * @Description : task stage构造器
     * @Param : [tid]
     * @Return :
     * @Author : gsq
     * @Date : 17:55
     * @note : An art cell !
     */
    private TSStartInfo(String id) {
        super(id);
    }

    /**
     * @Description : 创建task start info
     * @Param : [tid, sid, name]
     * @Return : cn.gsq.task.external.TSStartInfo
     * @Author : gsq
     * @Date : 14:51
     * @note : An art cell !
    **/
    public static TSStartInfo build(String id, String name) {
        return new TSStartInfo(id).setName(name);
    }

}
