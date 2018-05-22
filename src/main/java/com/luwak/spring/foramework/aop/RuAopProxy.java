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
		
		Method m = this.target.getClass().getMethod(method.getName(), method.getParameterTypes());
		
		//？？？
		
		//在原始方法调用以前需要执行增强的代码
		//这里需要通过原生方法去找，通过代理方法是找不到的
		if(config.contains(m)) {
			RuAopConfig.RuAspect aspect = config.get(m);
			aspect.getPoints()[0].invoke(aspect.getAspect());
		}
		
		//反射调用原始方法
		Object obj = m.invoke(this.target, args);
		System.out.println(args);
		
		//在原始方法调用以后要执行增强的代码
		if(config.contains(m)) {
			RuAopConfig.RuAspect aspect = config.get(m);
			aspect.getPoints()[1].invoke(aspect.getAspect());
		}
		
		//将最原始的返回值返回出去
		return obj;
	}

}
