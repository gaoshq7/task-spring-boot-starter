package com.yomahub.liteflow.springboot.config;

import cn.gsq.task.TaskActuator;
import cn.gsq.task.context.DefaultTaskActuator;
import cn.gsq.task.context.StageBeginAction;
import cn.gsq.task.context.StageEndAction;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.monitor.MonitorBus;
import com.yomahub.liteflow.property.LiteflowConfig;
import com.yomahub.liteflow.spi.spring.SpringAware;
import com.yomahub.liteflow.spring.ComponentScanner;
import com.yomahub.liteflow.spring.DeclBeanDefinition;
import com.yomahub.liteflow.springboot.LiteflowExecutorInit;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 主要的业务装配器 在这个装配器里装配了执行器，执行器初始化类，监控器
 * 这个装配前置条件是需要LiteflowConfig，LiteflowPropertyAutoConfiguration以及SpringAware
 *
 * @author Bryan.Zhang
 */
@Configuration
@AutoConfigureAfter({ LiteflowPropertyAutoConfiguration.class })
@ConditionalOnBean(LiteflowConfig.class)
@ConditionalOnProperty(prefix = "liteflow", name = "enable", havingValue = "true")
@Import(SpringAware.class)
public class LiteflowMainAutoConfiguration {

	@Bean
	public DeclBeanDefinition declBeanDefinition(){
		return new DeclBeanDefinition();
	}

	// 实例化ComponentScanner
	// 多加一个SpringAware的意义是，确保在执行这个的时候，SpringAware这个bean已经被初始化
	@Bean
	public ComponentScanner componentScanner(LiteflowConfig liteflowConfig, SpringAware springAware) {
		return new ComponentScanner(liteflowConfig);
	}

	// 实例化FlowExecutor
	// 多加一个SpringAware的意义是，确保在执行这个的时候，SpringAware这个bean已经被初始化
	@Bean
	@ConditionalOnMissingBean
	public FlowExecutor flowExecutor(LiteflowConfig liteflowConfig, SpringAware springAware) {
		FlowExecutor flowExecutor = new FlowExecutor();
		flowExecutor.setLiteflowConfig(liteflowConfig);
		return flowExecutor;
	}

	// FlowExecutor的初始化工作，和实例化分开来
	@Bean
	@ConditionalOnProperty(prefix = "liteflow", name = "parse-on-start", havingValue = "true")
	public LiteflowExecutorInit liteflowExecutorInit(FlowExecutor flowExecutor) {
		return new LiteflowExecutorInit(flowExecutor);
	}

	// 实例化MonitorBus
	// 多加一个SpringAware的意义是，确保在执行这个的时候，SpringAware这个bean已经被初始化
	@Bean("monitorBus")
	@ConditionalOnProperty(prefix = "liteflow", name = "monitor.enable-log", havingValue = "true")
	public MonitorBus monitorBus(LiteflowConfig liteflowConfig, SpringAware springAware) {
		return new MonitorBus(liteflowConfig);
	}

	/**
	 * @Description : 添加封装的执行器
	 * @Param : [liteflowConfig, springAware]
	 * @Return : com.yomahub.liteflow.monitor.MonitorBus
	 * @Author : gsq
	 * @Date : 17:45
	 * @note : An art cell !
	 **/
	@Bean("taskActuator")
	public TaskActuator taskActuator() {
		return new DefaultTaskActuator();
	}

	/**
	 * @Description : 添加起始算子
	 * @Param : []
	 * @Return : cn.gsq.task.context.StageBeginAction
	 * @Author : gsq
	 * @Date : 11:04
	 * @note : An art cell !
	**/
	@Bean("begin")
	public StageBeginAction beginAction() {
		return new StageBeginAction();
	}

	/**
	 * @Description : 添加终止算子
	 * @Param : []
	 * @Return : cn.gsq.task.context.StageEndAction
	 * @Author : gsq
	 * @Date : 11:04
	 * @note : An art cell !
	**/
	@Bean("end")
	public StageEndAction endAction() {
		return new StageEndAction();
	}

}
