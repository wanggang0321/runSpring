package com.luwak.spring.demo.action;

import com.luwak.spring.demo.service.IQueryService;
import com.luwak.spring.foramework.annotation.RuAutowired;
import com.luwak.spring.foramework.annotation.RuController;
import com.luwak.spring.foramework.annotation.RuRequestMapping;
import com.luwak.spring.foramework.annotation.RuRequestParamter;

/**
 * @author wanggang
 * @date 2018年5月2日 上午9:44:02
 * 
 */
@RuController
@RuRequestMapping("/web")
public class MyAction {
	
	@RuAutowired
	private IQueryService queryService;
	
	@RuRequestMapping("/query.json")
	public void query(@RuRequestParamter("name") String name) {
		String result = queryService.query(name);
		System.out.println(result);
	}
	
}
