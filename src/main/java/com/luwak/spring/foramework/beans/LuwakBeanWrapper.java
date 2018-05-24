package com.luwak.spring.foramework.beans;

import com.luwak.spring.foramework.aop.RuAopConfig;
import com.luwak.spring.foramework.aop.RuAopProxy;
import com.luwak.spring.foramework.core.FactoryBean;

/**
 * @author wanggang
 * @date 2018年4月25日 上午8:55:19
 * 
 */
public class LuwakBeanWrapper extends FactoryBean {
	
	private RuAopProxy aopProxy = new RuAopProxy();
	
	//支持事件响应，会有一个监听
	private LuwakBeanPostProcessor postProcessor;
	private Object wrapperInstance;
	//原始的通过反射new出来
	private Object originalInstance;
	
	public LuwakBeanWrapper(Object instance) {
		//从这里开始，要把动态的代码添加进来了
		this.wrapperInstance = aopProxy.getProxy(instance);
		this.originalInstance = instance;
	}
	
	public Object getWrappedInstance() {
		return this.wrapperInstance;
	}
	
	//返回代理以后的Class
	public Class<?> getWrappedClass() {
		return this.wrapperInstance.getClass();
	}

	public LuwakBeanPostProcessor getPostProcessor() {
		return postProcessor;
	}

	public void setPostProcessor(LuwakBeanPostProcessor postProcessor) {
		this.postProcessor = postProcessor;
	}

	public void setAopConfig(RuAopConfig config) {
		this.aopProxy.setConfig(config);
	}
	
	public Object getOriginalInstance() {
		return originalInstance;
	}
	
}
