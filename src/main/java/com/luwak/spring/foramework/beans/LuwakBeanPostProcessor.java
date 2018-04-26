package com.luwak.spring.foramework.beans;

/**
 * @author wanggang
 * @date 2018年4月25日 上午8:56:10
 * 用作事件监听的
 */
public class LuwakBeanPostProcessor {
	
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		return bean;
	}
	
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		return bean;
	}
	
}
