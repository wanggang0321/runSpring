package com.luwak.spring.foramework.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wanggang
 * @date 2018年5月21日 下午2:59:04
 * 只是对applicationContext.properties中配置的封装
 * 目标代理对象的一个方法要增强
 * 由用自己实现的业务逻辑去增强
 * 配置文件的作用只是告诉spring哪些类的哪些方法需要增强，增强的内容是什么
 * 对配置文件中所体现的内容进行封装
 */
public class RuAopConfig {
	
	//以目标对象需要被增强的Method做为key，增强后的代码内容做为value
	private Map<Method, RuAspect> points = new HashMap<Method, RuAspect>();
	
	public void put(Method target, Object aspect, Method[] points) {
		this.points.put(target, new RuAspect(aspect, points));
	}
	
	public RuAspect get(Method method) {
		return this.points.get(method);
	}
	
	public boolean contains(Method method) {
		return this.points.containsKey(method);
	}
	
	//被增强的代码的封装
	public class RuAspect {
		
		private Object aspect; //会将LogAspect这个对象赋值给它
		private Method[] points; //会将LogAspect的before方法和after方法赋值进来
		
		public RuAspect(Object aspect, Method[] points) {
			this.aspect = aspect;
			this.points = points;
		}
		
		public Object getAspect() {
			return this.aspect;
		}
		
		public Method[] getPoints() {
			return this.points;
		}
		
	}
	
}
