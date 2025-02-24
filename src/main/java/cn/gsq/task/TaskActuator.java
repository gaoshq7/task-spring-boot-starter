package cn.gsq.task;

import cn.gsq.task.external.ITType;
import cn.gsq.task.pojo.PTStage;

import java.util.List;

/**
 * Project : galaxy
 * Class : cn.gsq.task.TaskActuator
 *
 * @author : gsq
 * @date : 2023-11-28 13:40
 * @note : It's not technology, it's art !
 **/
public interface TaskActuator {

    /**
     * @Description : 任务执行入口
     * @Param : [id, name, type, stages]
     * @Return : void
     * @Author : gsq
     * @Date : 15:00
     * @note : ⚠️ id为任务唯一标识；
     *            name为任务名称；
     *            param为任务参数；
     *            type为任务类型；
     *            stages为任务步骤计划；
     *            contexts为附加上下文（可有可无） !
    **/
    String execute(String id, String name, Object param, ITType type, List<PTStage> stages, Object... contexts);

}
