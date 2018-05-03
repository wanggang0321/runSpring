package com.luwak.spring.foramework.webmvc.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.luwak.spring.foramework.context.LuwakApplicationContext;

/**
 * @author wanggang
 * @date 2018年4月23日 下午2:29:56
 * 
 */
public class DispatcherServlet extends HttpServlet {
	
	private final String LOCATION = "contextConfigLocation";

	public void init(ServletConfig config) throws ServletException {
		
		//相当于把IOC容器初始化了
		LuwakApplicationContext context = new LuwakApplicationContext(config.getInitParameter(LOCATION));
		
		initStrategies(context);
	}

	protected void initStrategies(LuwakApplicationContext context) {
		
		//有九种策略
		//针对每个用户请求，都会经过一些策略的处理之后，最终才能有结果输出
		//每种策略都可以自定义干预，但是最终的结果都是一致
		//ModeAndView
		
		initMultipartResolver(context);
		initLocaleResolver(context);
		initThemeResolver(context);
		initHandlerMappings(context);
		initHandlerAdapters(context);
		initHandlerExceptionResolvers(context);
		initRequestToViewNameTranslator(context);
		initViewResolvers(context);
		initFlashMapManager(context);
	}

	private void initFlashMapManager(LuwakApplicationContext context) {}
	private void initViewResolvers(LuwakApplicationContext context) {}
	private void initRequestToViewNameTranslator(LuwakApplicationContext context) {}
	private void initHandlerExceptionResolvers(LuwakApplicationContext context) {}
	private void initHandlerAdapters(LuwakApplicationContext context) {}
	private void initHandlerMappings(LuwakApplicationContext context) {}
	private void initThemeResolver(LuwakApplicationContext context) {}
	private void initLocaleResolver(LuwakApplicationContext context) {}
	private void initMultipartResolver(LuwakApplicationContext context) {}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		super.doGet(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		super.doPost(req, resp);
	}

}
