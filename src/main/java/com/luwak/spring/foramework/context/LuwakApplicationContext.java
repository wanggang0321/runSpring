package com.luwak.spring.foramework.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.luwak.spring.foramework.beans.LuwakBeanDefinition;
import com.luwak.spring.foramework.beans.LuwakBeanWrapper;
import com.luwak.spring.foramework.context.support.BeanDefinitionReader;
import com.luwak.spring.foramework.core.BeanFactory;

/**
 * @author wanggang
 * @date 2018年4月24日 下午1:31:07
 * 
 */
public class LuwakApplicationContext implements BeanFactory {
	
	private String[] configLocations;
	
	private BeanDefinitionReader reader;
	
	//用来保存配置信息
	private Map<String, LuwakBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, LuwakBeanDefinition>();
	
	//用来保证注册式单例的容器
	private Map<String, Object> beanCacheMap = new HashMap<String, Object>();
	
	//用来存储所有的被代理过的对象
	private Map<String, LuwakBeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, LuwakBeanWrapper>();

	public void refresh() {
		
		//定位
		this.reader = new BeanDefinitionReader(configLocations);
		
		//加载
		List<String> beanDefinitions = reader.loadBeanDefinitions();
		
		//注册
		
	}
	
	private void doRegistry(List<String> beanDefinitions) {
		
	}
	
	public Object getBean(String name) {
		
		return null;
	}

}
