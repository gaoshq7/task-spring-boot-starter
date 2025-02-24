package cn.gsq.task.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Project : galaxy
 * Class : cn.gsq.task.pojo.PTStage
 *
 * @author : gsq
 * @date : 2023-12-01 11:49
 * @note : It's not technology, it's art !
 **/
@Getter
@AllArgsConstructor
public class PTStage {

    private final String name;  // 步骤名称

    private final Integer weight;   // 步骤权重

    /**
     * @Description : 创建stage
     * @Param : [id, name, weight]
     * @Return : cn.gsq.task.pojo.TaskStage
     * @Author : gsq
     * @Date : 14:56
     * @note : An art cell !
    **/
    public static PTStage build(String name, Integer weight) {
        return new PTStage(name, weight);
    }

}
