package com.luwak.spring.foramework.webmvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wanggang
 * @date 2018年5月7日 上午11:22:48
 * 专人干专事
 */
public class RuHandlerAdapter {
	
	private Map<String, Integer> paramMapping;
	
	public RuHandlerAdapter(Map<String, Integer> paramMapping) {
		this.paramMapping = paramMapping;
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @param handler
	 * @return
	 */
	public RuModelAndView handle(HttpServletRequest req, HttpServletResponse resp, RuHandlerMapping handler) {
		
		//根据用户请求的参数信息，跟method中的参数信息进行动态匹配
		//resp传进来的目的只有一个，只是为了将其赋值给方法参数
		
		//只有当用户传过来的ModelAndView为空的时候，才会new一个默认的
		
		//1.要准备好这个方法的形参列表
		//方法重载的因素：参数列表，个数、类型、顺序
		Class<?>[] paramTypes = handler.getMethod().getParameterTypes();
		
		//2.拿到
		//用户通过url传过来的参数列表
		Map<String, String[]> reqParameterMap = req.getParameterMap();
		
		return null;
	}

}
