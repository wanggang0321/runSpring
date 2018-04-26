package com.luwak.spring.foramework.beans;

import com.luwak.spring.foramework.core.FactoryBean;

/**
 * @author wanggang
 * @date 2018年4月25日 上午8:55:19
 * 
 */
public class LuwakBeanWrapper extends FactoryBean {
	
	//支持事件响应，会有一个监听
	private LuwakBeanPostProcessor postProcessor;
	private Object wrapperInstance;
	//原始的通过反射new出来
	private Object originalInstance;
	
	public LuwakBeanWrapper(Object instance) {
		this.wrapperInstance = instance;
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
	
}
