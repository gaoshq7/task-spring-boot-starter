package cn.gsq.task.context;

import cn.gsq.task.external.TSEndInfo;
import cn.gsq.task.pojo.PTStagePlus;
import com.yomahub.liteflow.annotation.Operator;
import com.yomahub.liteflow.exception.LiteFlowException;
import lombok.extern.slf4j.Slf4j;

/**
 * Project : galaxy
 * Class : cn.gsq.task.context.StageEndAction
 *
 * @author : gsq
 * @date : 2023-12-14 11:51
 * @note : It's not technology, it's art !
 **/
@Slf4j
@Operator(id = "end", name = "步骤结束")
public class StageEndAction extends AbstractActuatorAction {

    /**
     * @Description : 记录任务步骤结束
     * @Param : []
     * @Return : void
     * @Author : gsq
     * @Date : 16:37
     * @note : An art cell ! 
    **/
    @Override
    public void onSuccess() throws Exception {
        super.onSuccess();
        // 完成步骤
        PTStagePlus stage = this.getContextBean(CalculateContext.class).completeStage();
        // 向外部接口发送步骤停止消息
        try {
            transport.end(TSEndInfo.build(
                    this.getContextBean(CalculateContext.class).getId(), stage.getName(), Boolean.TRUE,
                    stage.getTime(),this.getContextBean(CalculateContext.class).getPlan()
            ));
        } catch (Exception e) {
            log.error("结束任务步骤'{}'信息推送失败: ", stage.getName(), e);
            throw new LiteFlowException("结束任务步骤'" + stage.getName() + "'信息推送失败，请查看日志！");
        }
    }

    /**
     * @Description : 标志算子，拒接推送日志
     * @Param : []
     * @Return : void
     * @Author : gsq
     * @Date : 15:51
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
//        info(StrUtil.format("'{}'执行完成", this.getContextBean(CalculateContext.class).getCurrentStage()));
    }

}
