package cn.gsq.task.context;

import lombok.Getter;

/**
 * Project : galaxy
 * Class : cn.gsq.task.context.BaseContext
 *
 * @author : gsq
 * @date : 2023-12-01 17:42
 * @note : It's not technology, it's art !
 **/
@Getter
public abstract class BaseContext {

    protected final String id;   // 任务唯一标识

    protected final String name; // 任务名称

    /**
     * @Description : 构造器
     * @Param : [tid]
     * @Return :
     * @Author : gsq
     * @Date : 17:44
     * @note : An art cell !
    **/
    protected BaseContext(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
