package com.luwak.spring.demo.aspect;

/**
 * @author wanggang
 * @date 2018年5月21日 下午3:01:52
 * 
 */
public class LogAspect {
	
	//在调用一个方法前执行
	public void before() {
		//这里的逻辑由自己实现
		System.out.println("Invoker before method.");
	}
	
	//在调用一个方法后执行
	public void after() {
		System.out.println("Invoker after method");
	}
	
}
