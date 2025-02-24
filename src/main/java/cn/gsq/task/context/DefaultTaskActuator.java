package cn.gsq.task.context;

import cn.gsq.common.config.CommonAsyncProcessor;
import cn.gsq.task.TaskActuator;
import cn.gsq.task.external.*;
import cn.gsq.task.pojo.PTStage;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ArrayUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Project : galaxy
 * Class : cn.gsq.task.context.AbstractTaskActuator
 *
 * @author : gsq
 * @date : 2023-11-30 17:20
 * @note : It's not technology, it's art !
 **/
@Slf4j
public class DefaultTaskActuator implements TaskActuator {

    @Autowired
    protected CommonAsyncProcessor processor;     // 环境中必须存在common线程池

    @Autowired
    protected FlowExecutor executor;    // liteflow执行器

    @Autowired
    protected TMTransport transport;    // 与外部程序通讯的接口

    /**
     * @Description : 任务执行入口
     * @Param : [id, name, param, type, stages, contexts]
     * @Return : java.lang.String
     * @Author : gsq
     * @Date : 11:00
     * @note : An art cell !
    **/
    @Override
    public String execute(String id, String name, Object param, ITType type, List<PTStage> stages, Object... contexts) {
        TSSubmitInfo submitInfo = TSSubmitInfo.build(
                name, type, CollUtil.map(stages, PTStage::getName, true)
        );
        // 异步提交任务
        processor.submitTask(new CommonAsyncProcessor.ExceptionProcessor() {
            @Override
            public void actuator() {
                transport.submit(submitInfo);   // 使用外部接口通知任务提交信息
                executor.reloadRule();          // 每次执行任务刷新规则
                LiteflowResponse response = executor.execute2Resp(
                        id, // chainID
                        param,  // 任务参数
                        proContext(CollUtil.newArrayList(contexts), submitInfo, stages)
                );
                // 处理liteflow返回值并制作任务完成实例
                TSCompleteInfo<ITRHandler> completeInfo = TSCompleteInfo.build(
                        submitInfo.getId(),
                        response.isSuccess(),
                        report(response),
                        () -> response.getContextBean(ConclusionContext.class).getStream()
                );
                transport.complete(completeInfo);
            }

            @Override
            public void error(Throwable error) {
                log.error("'{}'任务提交线程 {} 出现异常：{}", name, Thread.currentThread().getName(), error.getMessage(), error);
            }
        });
        // 异步返回任务ID
        return submitInfo.getId();
    }

    /**
     * @Description : 整理任务上下文集合
     * @Param : [contexts, submitInfo, stages]
     * @Return : java.lang.Object[]
     * @Author : gsq
     * @Date : 17:39
     * @note : An art cell !
    **/
    private Object[] proContext(List<Object> contexts, TSSubmitInfo submitInfo, List<PTStage> stages) {
        contexts.add(new CalculateContext(submitInfo.getId(), submitInfo.getName(), stages));   // 默认上下文（计算任务进度）
        contexts.add(new ConclusionContext(submitInfo.getId(), submitInfo.getName()));          // 默认上下文（统计任务结果）
        return ArrayUtil.toArray(contexts, Object.class);
    }

    /**
     * @Description : 制作任务执行报告
     * @Param : [response]
     * @Return : cn.gsq.task.external.TSCompleteInfo.Report
     * @Author : gsq
     * @Date : 15:54
     * @note : An art cell !
    **/
    private TSCompleteInfo.Report report(LiteflowResponse response) {
        ConclusionContext resultContext = response.getContextBean(ConclusionContext.class);
        CalculateContext planContext = response.getContextBean(CalculateContext.class);
        Queue<PTStage> summary = planContext.getSummary();  // 获取任务步骤集合
        AtomicReference<PTStage> currentStep = new AtomicReference<>(summary.poll());   // 出栈第一步骤
        List<TSCompleteInfo.Operation> operations = CollUtil.map(
                response.getExecuteStepQueue(),
                s -> {
                    TSCompleteInfo.Operation operation = new TSCompleteInfo.Operation()
                            .setId(s.getNodeId())
                            .setName(s.getNodeName())
                            .setSname(currentStep.get().getName())
                            .setSuccess(s.isSuccess())
                            .setType(s.getRefNode().getType().getName())
                            .setStart(DatePattern.NORM_DATETIME_FORMAT.format(s.getStartTime()))
                            .setEnd(DatePattern.NORM_DATETIME_FORMAT.format(s.getEndTime()))
                            .setTime(s.getTimeSpent())
                            .setMsg(s.getException() == null ? null : s.getException().getMessage());
                    // 任务步骤结束更新当前步骤信息
                    if (s.getNodeId().equals("end")) {
                        currentStep.set(summary.poll());
                    }
                    return operation;
                },
                true
        );
        // 过滤掉开始和结束算子
        CollUtil.filter(operations, o -> !o.getId().equals("begin") && !o.getId().equals("end"));
        // 根据步骤名称进行分组统计
        List<List<TSCompleteInfo.Operation>> group = CollUtil.groupByField(operations, "sname");
        List<TSCompleteInfo.Step> steps = CollUtil.map(
                group,
                os -> new TSCompleteInfo.Step()
                        .setName(os.get(0).getSname())
                        .setSuccess(!CollUtil.contains(os, o -> !o.getSuccess()))
                        .setOperations(os)
                        .setTime(os.stream().mapToLong(TSCompleteInfo.Operation::getTime).sum()),
                true
        );
        // 当任务异常的时候补齐没有执行的步骤信息
        if (!summary.isEmpty()) {
            summary.forEach(s -> steps.add(
                    new TSCompleteInfo.Step()
                            .setName(s.getName())
                            .setTime(0L)
                            .setOperations(CollUtil.newArrayList()))
            );
            summary.clear();
        }
        return new TSCompleteInfo.Report()
                .setId(resultContext.getId())
                .setName(resultContext.getName())
                .setSuccess(response.isSuccess())
                .setPercent(planContext.getPlan())
                .setMsg(response.getMessage())
                .setSteps(steps);
    }

}
