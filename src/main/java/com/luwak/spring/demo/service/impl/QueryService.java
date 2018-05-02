package com.luwak.spring.demo.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.luwak.spring.demo.service.IQueryService;

/**
 * @author wanggang
 * @date 2018年5月2日 上午9:45:10
 * 
 */
public class QueryService implements IQueryService {

	public String query(String name) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String json = "{\"name\":\"" + name + "\", \"time\" : \"" + sdf.format(date) + "\"}";
		return json;
	}

}
