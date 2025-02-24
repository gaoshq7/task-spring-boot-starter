package com.yomahub.liteflow.spi.spring;

import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.spi.ContextCmpInit;
import com.yomahub.liteflow.spring.ComponentScanner;

/**
 * Spring环境容器上下文组件初始化实现
 *
 * @author Bryan.Zhang
 * @since 2.6.11
 */
public class SpringContextCmpInit implements ContextCmpInit {

	@Override
	public void initCmp() {
		ComponentScanner.nodeComponentSet.add("begin");	// 添加起始步骤
		ComponentScanner.nodeComponentSet.add("end");	// 添加结束步骤
		ComponentScanner.nodeComponentSet.forEach(FlowBus::addManagedNode);
	}

	@Override
	public int priority() {
		return 1;
	}

}
