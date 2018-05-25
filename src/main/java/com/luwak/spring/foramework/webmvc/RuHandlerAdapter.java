package com.luwak.spring.foramework.webmvc;

import java.util.Arrays;
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
	 * @param handler 为什么要把handler传进来，因为handler中包含了Controller、method、url信息
	 * @return
	 */
	public RuModelAndView handle(HttpServletRequest req, HttpServletResponse resp, RuHandlerMapping handler) throws Exception {
		
		//根据用户请求的参数信息，跟method中的参数信息进行动态匹配
		//resp传进来的目的只有一个，只是为了将其赋值给方法参数
		
		//只有当用户传过来的ModelAndView为空的时候，才会new一个默认的
		
		//1.要准备好这个方法的形参列表
		//方法重载的因素：参数列表，个数、类型、顺序
		Class<?>[] paramTypes = handler.getMethod().getParameterTypes();
		
		//2.拿到
		//用户通过url传过来的参数列表，这个reqParameterMap是只读的map，如何实现？
		Map<String, String[]> reqParameterMap = req.getParameterMap();
		
		//3.构造实参列表
		Object[] paramValues = new Object[paramTypes.length];
		for(Map.Entry<String, String[]> param : reqParameterMap.entrySet()) {
			String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]","").replaceAll("\\s","");
			if(!this.paramMapping.containsKey(param.getKey())) {
				continue;
			}
			
			int index = this.paramMapping.get(param.getKey());
			
			//需要对value转换类型
			paramValues[index] = caseString(paramTypes[index], value);
		}
		
		if(this.paramMapping.containsKey(HttpServletRequest.class)) {
			int indexReq = this.paramMapping.get(HttpServletRequest.class);
			paramValues[indexReq] = req;
		}
		
		if(this.paramMapping.containsKey(HttpServletResponse.class)) {
			int indexResp = this.paramMapping.get(HttpServletResponse.class);
			paramValues[indexResp] = resp;
		}
		
		//从handler中获取Controller、method，利用反射进行调用
		Object result = handler.getMethod().invoke(handler.getController(), paramValues);
		if(null==result) return null;
		
		boolean isModelAndView = handler.getMethod().getReturnType() == RuModelAndView.class;
		if(isModelAndView) {
			return (RuModelAndView) result;
		}
		
		return null;
	}
	
	private Object caseString(Class<?> clazz, String value) {
		
		if(clazz == String.class) {
			return value;
		} else if(clazz == Integer.class) {
			return Integer.valueOf(value);
		} else if(clazz == int.class) {
			return Integer.valueOf(value).intValue();
		}
		
		return null;
	}

}
