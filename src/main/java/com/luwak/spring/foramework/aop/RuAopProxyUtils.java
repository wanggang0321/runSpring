package com.luwak.spring.foramework.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @author wanggang
 * @date 2018年5月21日 下午2:35:35
 * 
 */
public class RuAopProxyUtils {
	
	public static  Object getTargetObject(Object proxy) throws Exception{
        //先判断一下，这个传进来的这个对象是不是一个代理过的对象
        //如果不是一个代理对象，就直接返回
        if(!isAopProxy(proxy)){ return proxy; }
        return getProxyTargetObject(proxy);
    }

    private static boolean isAopProxy(Object object){
        return Proxy.isProxyClass(object.getClass());
    }

    private static Object getProxyTargetObject(Object proxy) throws Exception{
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        RuAopProxy aopProxy = (RuAopProxy) h.get(proxy);
        Field target = aopProxy.getClass().getDeclaredField("target");
        target.setAccessible(true);
        return  target.get(aopProxy);
    }
	
}
