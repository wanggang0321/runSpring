package com.luwak.spring.foramework.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.luwak.spring.foramework.beans.LuwakBeanDefinition;
import com.luwak.spring.foramework.core.BeanFactory;

/**
 * @author wanggang
 * @date 2018年4月24日 下午1:31:07
 * 
 */
public class LuwakApplicationContext implements BeanFactory {
	
	private String[] locationConfigs;
	
	private Map<String, LuwakBeanDefinition> beanMap = new HashMap<String, LuwakBeanDefinition>();
	
	private List<String> beanNames = new ArrayList<String>();

	public Object getBean(String name) {
		
		return null;
	}

}
