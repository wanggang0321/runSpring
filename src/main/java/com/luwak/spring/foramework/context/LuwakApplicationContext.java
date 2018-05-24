package com.luwak.spring.foramework.context;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.luwak.spring.demo.action.MyAction;
import com.luwak.spring.foramework.annotation.RuAutowired;
import com.luwak.spring.foramework.annotation.RuController;
import com.luwak.spring.foramework.annotation.RuService;
import com.luwak.spring.foramework.beans.LuwakBeanDefinition;
import com.luwak.spring.foramework.beans.LuwakBeanPostProcessor;
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
	
	public LuwakApplicationContext(String... configLocations) {
		this.configLocations = configLocations;
		refresh();
	}
	
	public void refresh() {
		
		//定位
		this.reader = new BeanDefinitionReader(configLocations);
		
		//加载
		List<String> beanDefinitions = reader.loadBeanDefinitions();
		
		//注册
		doRegistry(beanDefinitions);
		
		//依赖注入（lazy-init=false）
		//在这里自动调用getBean
		doAutowire();
		
		MyAction action = (MyAction) this.getBean("myAction");
		action.query("Luwak");
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
					this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
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
		
		for(Map.Entry<String, LuwakBeanWrapper> entry : beanWrapperMap.entrySet()) {
			populateBean(entry.getKey(), entry.getValue().getWrappedInstance());
		}
		
	}
	
	private void populateBean(String beanName, Object instance) {
		
		Class<?> claxx = instance.getClass();
		
		if(!(claxx.isAnnotationPresent(RuController.class) || claxx.isAnnotationPresent(RuService.class))) {
			return;
		}
		
		Field[] fields = claxx.getDeclaredFields();
		
		for(Field f : fields) {
			
			if(!f.isAnnotationPresent(RuAutowired.class)) {
				//
				continue;
			}
			
			RuAutowired autowired = f.getAnnotation(RuAutowired.class);
			String autowiredBeanName = autowired.value().trim();
			if("".equals(autowiredBeanName)) {
				autowiredBeanName = f.getName();
			}
			
			f.setAccessible(true);
			
			try {
				f.set(instance, this.beanWrapperMap.get(autowiredBeanName).getWrappedInstance());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	//开始自动注入方法
	//装饰器模式
	//1.保留原来的OOP关系，
	//2.我需要对它扩展、增强（为了以后AOP打基础）
	public Object getBean(String beanName) {
		
		LuwakBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
		
		String className = beanDefinition.getBeanClassName();
		
		try {
			//生成通知事件
			LuwakBeanPostProcessor postProcessor = new LuwakBeanPostProcessor();
			
			Object instance = instantionBean(beanDefinition);
			if(null==instance) return null;
			
			//在实例化以前调用一次
			postProcessor.postProcessBeforeInitialization(instance, beanName);
			
			LuwakBeanWrapper beanWrapper = new LuwakBeanWrapper(instance);
			beanWrapper.setPostProcessor(postProcessor);
			this.beanWrapperMap.put(beanName, beanWrapper);
			
			postProcessor.postProcessAfterInitialization(instance, beanName);
			
			//通过这样一调用，相当于给我们自己留有了可操作空间
			return this.beanWrapperMap.get(beanName).getWrappedInstance();
		} catch (Exception e) {
			
		}
		
		return null;
	}
	
	private Object instantionBean(LuwakBeanDefinition beanDefinition) {
		
		Object instance = null;
		
		try {
			String className = beanDefinition.getBeanClassName();
			
			if(this.beanCacheMap.containsKey(className)) {
				instance = beanCacheMap.get(className);
			} else {
				Class<?> claxx = Class.forName(className);
				instance = claxx.newInstance();
				this.beanCacheMap.put(className, instance);
			}
		} catch (Exception e) {
			
		}
		
		return instance;
	}

	public String[] getBeanDefinitionNames() {
		return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
	}
	
	public int getDefinitionCount() {
		return this.beanCacheMap.size();
	}
	
	public Properties getConfig() {
		return this.reader.getConfig();
	}
	
}
