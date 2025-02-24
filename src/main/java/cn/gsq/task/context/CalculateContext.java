package cn.gsq.task.context;

import cn.gsq.task.pojo.PTStage;
import cn.gsq.task.pojo.PTStagePlus;
import cn.hutool.core.util.NumberUtil;
import com.yomahub.liteflow.exception.LiteFlowException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Project : galaxy
 * Class : cn.gsq.task.context.CalculateContext
 *
 * @author : gsq
 * @date : 2023-12-01 09:57
 * @note : It's not technology, it's art !
 **/
@Slf4j
@Getter
public class CalculateContext extends BaseContext {

    private final Queue<PTStage> stages; // 任务计划步骤

    protected final Integer total;    // 需要运行的步骤的权重总和

    private transient Integer current;  // 当前完成的步骤权重总和

    private transient volatile Long stime;    // 步骤开始时间戳

    private transient volatile String failed;     // 失败的stage

    private final transient Queue<PTStage> summary;    // 任务总结

    /**
     * @Description : 初始化task上下文
     * @Param : [id, total]
     * @Return :
     * @Author : gsq
     * @Date : 11:24
     * @note : An art cell !
    **/
    protected CalculateContext(String id, String name, List<PTStage> stages) {
        super(id, name);
        this.stages = new LinkedBlockingQueue<>();
        this.summary = new LinkedBlockingQueue<>();
        this.total = stages.stream().mapToInt(PTStage::getWeight).sum();
        this.stages.addAll(stages); // 压入队列，装载要运行的步骤
        this.summary.addAll(stages); // 压入队列，结果统计时使用
        this.current = 0;
        this.stime = System.currentTimeMillis();    // 记录任务开始时间戳
    }

    /**
     * @Description : 计算当前任务的进度
     * @Param : []
     * @Return : java.math.BigDecimal
     * @Author : gsq
     * @Date : 11:24
     * @note : An art cell !
    **/
    public BigDecimal getPlan() {
        return NumberUtil.round(NumberUtil.div(current, total).floatValue(), 2);
    }

    /**
     * @Description : 获取当前正在运行的stage
     * @Param : []
     * @Return : java.lang.String
     * @Author : gsq
     * @Date : 16:13
     * @note : An art cell !
    **/
    public String getCurrentStage() {
        return stages.peek() == null ? null : stages.peek().getName();
    }

    /**
     * @Description : 累加步骤完成的权重
     * @Param : []
     * @Return : void
     * @Author : gsq
     * @Date : 11:26
     * @note : An art cell !
    **/
    public synchronized PTStagePlus completeStage() {
        if(isFinished()) {
            log.error("{}任务所有步骤已经结束，不能再继续记录！", this.getName());
            throw new LiteFlowException(this.getName() + "任务所有步骤已经结束，不能再继续记录！");
        }
        PTStage stage = stages.poll();  // 当前stage出栈
        assert stage != null;
        this.current = this.current + stage.getWeight();
        return PTStagePlus.build(stage.getName(), stage.getWeight(), timeCompute());
    }

    /**
     * @Description : 清空执行步骤计划，终止任务
     * @Param : []
     * @Return : void
     * @Author : gsq
     * @Date : 17:24
     * @note : ⚠️ 当任务遇到异常时终止任务 !
    **/
    public synchronized PTStagePlus interrupt() {
        PTStagePlus stage = completeStage();    // 让失败的步骤出栈
        this.failed = stage.getName();  // 记录失败的步骤名称
        this.getStages().clear();   // 清空步骤队列
        return stage;
    }

    /**
     * @Description : 计算步骤完成耗时
     * @Param : []
     * @Return : java.lang.Long
     * @Author : gsq
     * @Date : 09:59
     * @note : An art cell !
    **/
    private Long timeCompute() {
        Long ctime = System.currentTimeMillis();
        Long takeTime = ctime - this.stime;  // 计算步骤用时
        this.stime = ctime;     // 刷新开始时间
        return takeTime;
    }

    /**
     * @Description : 任务步骤是否全部完成
     * @Param : []
     * @Return : java.lang.Boolean
     * @Author : gsq
     * @Date : 11:14
     * @note : An art cell !
     **/
    private Boolean isFinished() {
        return this.stages.isEmpty();
    }

}
