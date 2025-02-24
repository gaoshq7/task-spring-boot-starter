package cn.gsq.task.external;

/**
 * Project : galaxy
 * Class : cn.gsq.task.external.TMTransport
 *
 * @author : gsq
 * @date : 2023-11-28 16:14
 * @note : It's not technology, it's art !
 **/
public interface TMTransport {

    /**
     * @Description : 提交任务
     * @Param : [submitInfo]
     * @Return : void
     * @Author : gsq
     * @Date : 16:53
     * @note : An art cell ! 
    **/
    void submit(TSSubmitInfo submitInfo);

    /**
     * @Description : 执行任务步骤
     * @Param : [startInfo]
     * @Return : void
     * @Author : gsq
     * @Date : 16:53
     * @note : An art cell !
    **/
    void start(TSStartInfo startInfo);

    /**
     * @Description : 输出任务步骤日志
     * @Param : [logInfo]
     * @Return : void
     * @Author : gsq
     * @Date : 16:53
     * @note : An art cell !
    **/
    void log(TSLogInfo logInfo);

    /**
     * @Description : 结束任务步骤
     * @Param : [endInfo]
     * @Return : void
     * @Author : gsq
     * @Date : 16:54
     * @note : An art cell !
    **/
    void end(TSEndInfo endInfo);

    /**
     * @Description : 结束任务
     * @Param : [completeInfo]
     * @Return : void
     * @Author : gsq
     * @Date : 16:55
     * @note : An art cell !
    **/
    void complete(TSCompleteInfo<? extends ITRHandler> completeInfo);

}
