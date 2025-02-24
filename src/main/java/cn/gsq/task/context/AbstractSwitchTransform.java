package cn.gsq.task.context;

import cn.gsq.task.external.ETLevel;
import cn.gsq.task.external.TMTransport;
import cn.gsq.task.external.TSEndInfo;
import cn.gsq.task.external.TSLogInfo;
import cn.gsq.task.pojo.PTStagePlus;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yomahub.liteflow.core.NodeSwitchComponent;
import com.yomahub.liteflow.exception.LiteFlowException;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * Project : galaxy
 * Class : cn.gsq.task.context.AbstractSwitchTransform
 *
 * @author : gsq
 * @date : 2023-12-05 17:51
 * @note : It's not technology, it's art !
 **/
@Slf4j
public abstract class AbstractSwitchTransform extends NodeSwitchComponent {

    protected TMTransport transport;    // 与外部程序通讯的接口

    protected String chainId;   // 所属任务ID

    protected String id;    // 组件唯一标识

    protected String name;    // 组件名称

    /**
     * @Description : 根据上下文中的起始nodeId启动任务步骤
     * @Param : []
     * @Return : void
     * @Author : gsq
     * @Date : 17:27
     * @note : ⚠️ 如果不是起始nodeId则不会有逻辑操作 !
     **/
    @Override
    public void beforeProcess() {
        super.beforeProcess();
        // 初始化需要的变量
        this.transport = SpringUtil.getBean(TMTransport.class);
        this.chainId = getChainId();
        this.id = getNodeId();
        this.name = getName();
    }

    /**
     * @Description : 封装执行逻辑
     * @Param : []
     * @Return : java.lang.String
     * @Author : gsq
     * @Date : 16:11
     * @note : An art cell !
    **/
    @Override
    public String processSwitch() throws Exception {
        String next = operateSwitch();
        this.info("执行成功");
        return next;
    }

    /**
     * @Description : 遇到错误终止任务步骤
     * @Param : [e]
     * @Return : void
     * @Author : gsq
     * @Date : 17:30
     * @note : An art cell !
     **/
    @Override
    public void onError(Exception e) throws Exception {
        super.onError(e);
        CalculateContext calculateContext = this.getContextBean(CalculateContext.class);
        PTStagePlus errorStage = calculateContext.interrupt();  // 中断任务
        try {
            error(errorStage.getName()+"执行出错！"+ (ObjectUtil.isNull(e.getMessage())?"":e.getMessage())
                    +(ObjectUtil.isNull(e.getCause())?"":": "+e.getCause()));
            transport.end(TSEndInfo.build(
                    calculateContext.getId(), errorStage.getName(), Boolean.FALSE, errorStage.getTime(), calculateContext.getPlan()
            ));
        } catch (Exception exception) {
            log.error("结束任务步骤'{}'信息推送失败: ", errorStage, exception);
            throw new LiteFlowException("结束任务步骤'" + errorStage + "'信息推送失败，请查看日志！");
        }
    }

    /**
     * @Description : 写入结果
     * @Param : [details]
     * @Return : void
     * @Author : gsq
     * @Date : 15:12
     * @note : An art cell !
     **/
    protected void writeResult(String details) {
        try {
            this.getContextBean(ConclusionContext.class).writeResult(details);
        } catch (Exception e) {
            log.error("算子'{}'写入结果失败: ", this.name, e);
            throw new LiteFlowException("算子'" + this.name + "'写入结果失败: " + e.getMessage());
        }
    }

    /**
     * @Description : 写入结果
     * @Param : [stream]
     * @Return : void
     * @Author : gsq
     * @Date : 15:20
     * @note : An art cell !
     **/
    protected void writeResult(InputStream stream) {
        try {
            this.getContextBean(ConclusionContext.class).setStream(stream);
        } catch (Exception e) {
            log.error("算子'{}'写入结果失败: ", this.name, e);
            throw new LiteFlowException("算子'" + this.name + "'写入结果失败: " + e.getMessage());
        }
    }

    /**
     * @Description : 发送info消息
     * @Param : [msg]
     * @Return : void
     * @Author : gsq
     * @Date : 17:38
     * @note : An art cell !
     **/
    protected void info(String msg) {
        CalculateContext calculateContext = this.getContextBean(CalculateContext.class);
        String current = calculateContext.getCurrentStage();
        current = StrUtil.isBlank(current) ? calculateContext.getFailed() : current;
        try {
            transport.log(
                    TSLogInfo.build(
                            calculateContext.getId(), current, this.name, this.getType().getCode(), ETLevel.INFO, msg
                    )
            );
        } catch (Exception e) {
            log.error("算子'{}'发送外部info信息失败: ", this.name, e);
            throw new LiteFlowException("结束任务步骤'" + current + "'info日志信息推送失败，请查看日志！");
        }
    }

    /**
     * @Description : 发送warn消息
     * @Param : [msg]
     * @Return : void
     * @Author : gsq
     * @Date : 17:39
     * @note : An art cell !
     **/
    protected void warn(String msg) {
        CalculateContext calculateContext = this.getContextBean(CalculateContext.class);
        String current = calculateContext.getCurrentStage();
        current = StrUtil.isBlank(current) ? calculateContext.getFailed() : current;
        try {
            transport.log(
                    TSLogInfo.build(
                            calculateContext.getId(), current, this.name, this.getType().getCode(), ETLevel.WARN, msg
                    )
            );
        } catch (Exception e) {
            log.error("算子'{}'发送外部warn信息失败: ", this.name, e);
            throw new LiteFlowException("结束任务步骤'" + current + "'warn日志信息推送失败，请查看日志！");
        }
    }

    /**
     * @Description : 发送error消息
     * @Param : [msg]
     * @Return : void
     * @Author : gsq
     * @Date : 17:39
     * @note : An art cell !
     **/
    protected void error(String msg) {
        CalculateContext calculateContext = this.getContextBean(CalculateContext.class);
        String current = calculateContext.getCurrentStage();
        current = StrUtil.isBlank(current) ? calculateContext.getFailed() : current;
        try {
            transport.log(
                    TSLogInfo.build(
                            calculateContext.getId(), current, this.name, this.getType().getCode(), ETLevel.ERROR, msg
                    )
            );
        } catch (Exception e) {
            log.error("算子'{}'发送外部error信息失败: ", this.name, e);
            throw new LiteFlowException("结束任务步骤'" + current + "'error日志信息推送失败，请查看日志！");
        }
    }

    /**
     * @Description : 执行算子
     * @Param : []
     * @Return : java.lang.String
     * @Author : gsq
     * @Date : 16:12
     * @note : ⚠️ 有异常向上抛出 !
    **/
    protected abstract String operateSwitch() throws Exception;

}
