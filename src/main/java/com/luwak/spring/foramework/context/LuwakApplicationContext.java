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
		doRegistry(beanDefinitions);
		
		//依赖注入（lazy-init=false）
		//在这里自动调用getBean
		
		
	}
	
	private void doRegistry(List<String> beanDefinitions) {
		
		//beanName有三种情况
		//1.默认是类名首字母小写
		//2.自定义名称
		//3.接口注入？？？？？
		try {
			for(String className : beanDefinitions) {
			
				Class<?> claxx = Class.forName(className);
				
				if(claxx.isInterface()) {
					continue;
				}
				
				LuwakBeanDefinition beanDefinition = reader.register(className);
				if(beanDefinition!=null) {
					this.beanDefinitionMap.put(className, beanDefinition);
				}
				
				Class<?>[] interfaces = claxx.getInterfaces();
				for(Class<?> i : interfaces) {
					//如果是多个实现类，只能覆盖，因为Spring没那么智能，就是这么傻，这里需要验证
					//这个时候，可以自定义名字
					this.beanDefinitionMap.put(i.getSimpleName(), beanDefinition);
				}
				
				//到这里为止，容器初始化完毕
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	//自动注入
	public void doAutowire() {
		
		for(Map.Entry<String, LuwakBeanDefinition> entry : beanDefinitionMap.entrySet()) {
			String beanName = entry.getKey();
			
			if(!entry.getValue().isLazyInit()) {
				getBean(beanName);
			}
		}
		
	}
	
	//开始自动注入方法
	//装饰器模式
	//保留原来的OOP关系，
	public Object getBean(String name) {
		
		
		
		return null;
	}

}
