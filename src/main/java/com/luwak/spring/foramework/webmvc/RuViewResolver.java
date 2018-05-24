package com.luwak.spring.foramework.webmvc;

import java.io.File;

/**
 * @author wanggang
 * @date 2018年5月7日 上午11:38:50
 * 
 */
//设计这个类的主要目的
//1.将一个静态文件转变为动态文件
//2.根据用户传送参数不同，产生不同的结果
//最终输出字符串，交给response输出
public class RuViewResolver {
	
	private String viewName;
	private File templateFile;
	
	public RuViewResolver(String viewName, File templateFile) {
		this.viewName = viewName;
		this.templateFile = templateFile;
	}
	
	public String viewResolver(RuModelAndView mv) {
		return null;
	}
	
}
