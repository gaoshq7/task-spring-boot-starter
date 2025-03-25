package cn.gsq.task.context;

import cn.gsq.task.external.TSStartInfo;
import com.yomahub.liteflow.annotation.Operator;
import com.yomahub.liteflow.exception.LiteFlowException;
import lombok.extern.slf4j.Slf4j;

/**
 * Project : galaxy
 * Class : cn.gsq.task.context.StageBeginAction
 *
 * @author : gsq
 * @date : 2023-12-14 11:50
 * @note : It's not technology, it's art !
 **/
@Slf4j
@Operator(id = "begin", name = "步骤开始")
public class StageBeginAction extends AbstractActuatorAction {

    @Override
    public void beforeProcess() {
        super.beforeProcess();
        // 向外部接口发送步骤开始消息
        String currentStage = this.getContextBean(CalculateContext.class).getCurrentStage();
        try {
            String taskId = this.getContextBean(CalculateContext.class).getId();
            TaskContext.set(taskId,currentStage);

            transport.start(TSStartInfo.build(taskId, currentStage));
        } catch (Exception e) {
            log.error("启动任务步骤'{}'信息推送失败: ", currentStage, e);
            throw new LiteFlowException("启动任务步骤'" + currentStage + "'信息推送失败，请查看日志！");
        }
    }

    /**
     * @Description : 标志算子，拒接推送日志
     * @Param : []
     * @Return : void
     * @Author : gsq
     * @Date : 15:52
     * @note : An art cell !
    **/
    @Override
    public void process() throws Exception {
        operate();
    }

    /**
     * @Description : 运行算子
     * @Param : []
     * @Return : void
     * @Author : gsq
     * @Date : 15:52
     * @note : An art cell !
    **/
    @Override
    public void operate() {
//        info(StrUtil.format("'{}'开始执行", this.getContextBean(CalculateContext.class).getCurrentStage()));
    }

}
