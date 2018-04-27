package com.luwak.spring.foramework.context.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.luwak.spring.foramework.beans.LuwakBeanDefinition;

/**
 * @author wanggang
 * @date 2018年4月24日 下午1:40:04
 * 对配置文件查找、读取、解析
 */
public class BeanDefinitionReader {
	
	private Properties config = new Properties();
	private List<String> registryBeanClasses = new ArrayList<String>();
	//在配置文件中，用来获取自动扫描的报名的key
	private String SCAN_PACKAGE = "scanPackage";
	
	public BeanDefinitionReader(String... locations) {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
		try {
			config.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(is!=null) is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		doScan(config.getProperty(SCAN_PACKAGE));
	}
	
	public List<String> loadBeanDefinitions() {
		return this.registryBeanClasses;
	}
	
	public void doScan(String packageName) {
		URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.","/"));
		File file = new File(url.getFile());
		File[] files = file.listFiles();
		for(File f : files) {
			if(f.isDirectory()) {
				doScan(f.getName());
			} else {
				registryBeanClasses.add(packageName + "/" + f.getName().replace(".class", ""));
			}
		}
	}
	
	//没注册一个className，就返回一个BeanDefinition，自己包装
	//只是为了对配置信息进行一个包装
	public LuwakBeanDefinition register(String className) {
		if(this.registryBeanClasses.contains(className)) {
			LuwakBeanDefinition beanDefinition = new LuwakBeanDefinition();
			beanDefinition.setBeanClassName(className);
			beanDefinition.setFactoryBeanName(lowerFirstCase(className.substring(className.indexOf(".") + 1)));
			return beanDefinition;
		}
		return null;
	}
	
	private String lowerFirstCase(String className) {
		char[] chars = className.toCharArray();
		chars[0] += 32;
		return String.valueOf(chars);
	}
	
}
