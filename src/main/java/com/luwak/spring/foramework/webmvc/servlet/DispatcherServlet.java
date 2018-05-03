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
		
		initStrategies();
	}

	protected void initStrategies() {
		
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		super.doGet(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		super.doPost(req, resp);
	}

}
