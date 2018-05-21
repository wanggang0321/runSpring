package com.luwak.spring.foramework.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wanggang
 * @date 2018年5月21日 下午3:49:32
 * 默认使用JDK动态代理实现
 */
public class RuAopProxy implements InvocationHandler {
	
	private RuAopConfig config;
	private Object target;
	
	//原生对象做为入参
	public Object getProxy(Object instance) {
		this.target = instance;
		Class<?> clazz = instance.getClass();
		return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
	}
	
	public void setConfig(RuAopConfig config) {
		this.config = config;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return null;
	}

}
