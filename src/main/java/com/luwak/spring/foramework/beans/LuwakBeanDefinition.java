package com.luwak.spring.foramework.beans;

/**
 * @author wanggang
 * @date 2018年4月24日 下午1:34:38
 * 用来存储配置文件中的信息
 * 相当于保存在内存中的配置
 */
public class LuwakBeanDefinition {
	
	private String beanClassName;
	//lazy-init="false"（默认false），表示立即加载，表示在spring启动时，立刻进行实例化
	private boolean lazyInit = false;
	private String factoryBeanName;
	
	public String getBeanClassName() {
		return beanClassName;
	}
	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}
	public boolean isLazyInit() {
		return lazyInit;
	}
	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}
	public String getFactoryBeanName() {
		return factoryBeanName;
	}
	public void setFactoryBeanName(String factoryBeanName) {
		this.factoryBeanName = factoryBeanName;
	}
	
}
