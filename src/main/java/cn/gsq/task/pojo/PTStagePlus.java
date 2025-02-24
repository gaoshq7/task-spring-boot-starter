package cn.gsq.task.pojo;

import lombok.Getter;

/**
 * Project : galaxy
 * Class : cn.gsq.task.pojo.PTStagePlus
 *
 * @author : gsq
 * @date : 2024-01-31 16:46
 * @note : It's not technology, it's art !
 **/
public class PTStagePlus extends PTStage {

    @Getter
    private final Long time;  // 任务步骤耗时（毫秒）

    /**
     * @Description : 构造器
     * @Param : [name, weight]
     * @Return :
     * @Author : gsq
     * @Date : 16:47
     * @note : An art cell !
    **/
    public PTStagePlus(String name, Integer weight, Long time) {
        super(name, weight);
        this.time = time;
    }

    /**
     * @Description : 构建
     * @Param : [name, weight]
     * @Return : cn.gsq.task.pojo.PTStagePlus
     * @Author : gsq
     * @Date : 16:47
     * @note : An art cell !
    **/
    public static PTStagePlus build(String name, Integer weight, Long time) {
        return new PTStagePlus(name, weight, time);
    }

}
