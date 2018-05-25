package com.luwak.spring.foramework.webmvc;

import java.util.Map;

/**
 * @author wanggang
 * @date 2018年5月7日 上午11:12:28
 * 
 */
public class RuModelAndView {
	
	private String viewName;
	private Map<String, ?> model;
	
	public RuModelAndView(String viewName, Map<String, ?> model) {
		this.viewName = viewName;
		this.model = model;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Map<String, ?> getModel() {
		return model;
	}

	public void setModel(Map<String, ?> model) {
		this.model = model;
	}
	
}
