package com.luwak.spring.foramework.webmvc.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.luwak.spring.foramework.annotation.RuController;
import com.luwak.spring.foramework.annotation.RuRequestMapping;
import com.luwak.spring.foramework.annotation.RuRequestParamter;
import com.luwak.spring.foramework.aop.RuAopProxyUtils;
import com.luwak.spring.foramework.context.LuwakApplicationContext;
import com.luwak.spring.foramework.webmvc.RuHandlerAdapter;
import com.luwak.spring.foramework.webmvc.RuHandlerMapping;
import com.luwak.spring.foramework.webmvc.RuModelAndView;
import com.luwak.spring.foramework.webmvc.RuViewResolver;

/**
 * @author wanggang
 * @date 2018年4月23日 下午2:29:56
 * 
 */
public class DispatcherServlet extends HttpServlet {
	
	private final String LOCATION = "contextConfigLocation";
	
	//思考一下这样设计的精妙之处
	//handlerMapping是spring最核心的设计，它厉害到直接干掉了struts、webwork等MVC框架
	private List<RuHandlerMapping> handlerMappings = new ArrayList<RuHandlerMapping>();
	
	private Map<RuHandlerMapping, RuHandlerAdapter> handlerAdapters = new HashMap<RuHandlerMapping, RuHandlerAdapter>();
	
	private List<RuViewResolver> viewResolvers = new ArrayList<RuViewResolver>();

	public void init(ServletConfig config) throws ServletException {
		
		//相当于把IOC容器初始化了
		LuwakApplicationContext context = new LuwakApplicationContext(config.getInitParameter(LOCATION));
		
		initStrategies(context);
	}

	protected void initStrategies(LuwakApplicationContext context) {
		
		//有九种策略
		//针对每个用户请求，都会经过一些策略的处理之后，最终才能有结果输出
		//每种策略都可以自定义干预，但是最终的结果都是一致
		//ModeAndView
		
		//***************** 这里说的就是传说中的九大组件
		initMultipartResolver(context); //文件上传解析，如果请求类型是multipart将通过MultipartResolver进行文件上传解析
		initLocaleResolver(context); //本地化解析
		initThemeResolver(context); //主题解析
		
		//自己实现，RuHandlerMapping，用来保存Controller中配置的RequestMapping和method的对应关系
		initHandlerMappings(context);
		//自己实现，RuHandlerAdapter，用来动态匹配method参数，包括类转换、动态赋值
		initHandlerAdapters(context);//通过HandlerAdapter进行多类型的参数动态匹配
		
		
		initHandlerExceptionResolvers(context); //如果执行过程中遇到异常，将交给HandlerExecptionResolver来解析
		initRequestToViewNameTranslator(context); //直接解析请求到视图名
		
		//自己实现，通过ViewResolver实现动态模板的解析，自己解析一套模板语言
		initViewResolvers(context); //通过ViewResolver解析逻辑视图到具体视图实现
		
		initFlashMapManager(context); //Flash映射管理器
	}

	private void initFlashMapManager(LuwakApplicationContext context) {}
	private void initRequestToViewNameTranslator(LuwakApplicationContext context) {}
	private void initHandlerExceptionResolvers(LuwakApplicationContext context) {}
	private void initThemeResolver(LuwakApplicationContext context) {}
	private void initLocaleResolver(LuwakApplicationContext context) {}
	private void initMultipartResolver(LuwakApplicationContext context) {}

	//将Controller中配置的RequestMapping和method进行一一映射
	private void initHandlerMappings(LuwakApplicationContext context) {
		//按照通常的理解，应该是一个map
		//Map<String, Method> map
		//Map<url, method>
		
		//首先从容器中取到所有的实例
		String[] beanNames = context.getBeanDefinitionNames();
		try {
			for(String beanName : beanNames) {
				//到了MVC层，对外提供的方法只有一个getBean
				//返回的对象不是BeanWrapper，怎么办？为什么会不是BeanWrapper？
				Object proxy = context.getBean(beanName);
				//这里为什么要取到原生对象
				Object controller = RuAopProxyUtils.getTargetObject(proxy);
				Class<?> clazz = controller.getClass();
				
				if(!clazz.isAnnotationPresent(RuController.class)) {
					continue;
				}
				
				String baseUrl = "";
				if(clazz.isAnnotationPresent(RuRequestMapping.class)) {
					RuRequestMapping mapping = clazz.getAnnotation(RuRequestMapping.class);
					baseUrl = mapping.value();
				}
				
				//扫描所有的public方法
				Method[] methods = clazz.getMethods();
				for(Method m : methods) {
					if(!m.isAnnotationPresent(RuRequestMapping.class)) {
						continue;
					}
					RuRequestMapping requestMapping = m.getAnnotation(RuRequestMapping.class);
					String regex = ("/" + baseUrl + requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
					Pattern pattern = Pattern.compile(regex);
					this.handlerMappings.add(new RuHandlerMapping(controller, m, pattern));
					
					System.out.println("Mapping : " + regex + " , " + m);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void initHandlerAdapters(LuwakApplicationContext context) {
		
		//在初始化阶段，我们能做的就是，将参数的名字或者类型保存下来
		
		for(RuHandlerMapping m : handlerMappings) {
			
			//每个方法都有一个参数列表
			Map<String, Integer> params = new HashMap<String, Integer>();
			
			Annotation[][] anno = m.getMethod().getParameterAnnotations();
			for(int i=0;i<anno.length;i++) {
				for(Annotation a : anno[i]) {
					if(a instanceof RuRequestParamter) {
						String paramName = ((RuRequestParamter) a).value();
						if(!"".equals(paramName)) {
							params.put(paramName, i);
						}
					}
				}
			}
			
			//只取request和response
			Class<?>[] pt = m.getMethod().getParameterTypes();
			for(int i=0;i<pt.length;i++) {
				Class<?> type = pt[i];
				if(type==HttpServletRequest.class || type==HttpServletResponse.class) {
					params.put(type.getName(), i);
				}
			}
			
			handlerAdapters.put(m, new RuHandlerAdapter(params));
		}
	}
	
	private void initViewResolvers(LuwakApplicationContext context) {
		//http://localhost/first.html
		//解决页面名字和模板文件的关联问题
		String templateRoot = context.getConfig().getProperty("templateRoot");
		String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
		
		File templateRootDir = new File(templateRootPath);
		for(File template : templateRootDir.listFiles()) {
			this.viewResolvers.add(new RuViewResolver(template.getName(), template));
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		this.doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			doDispatch(req, resp);
		} catch (Exception e) {
			resp.getWriter().write("<font size='25' color='blue'>500 Exception</font><br/>Details:<br/>" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]","")
                    .replaceAll("\\s","\r\n") +  "<font color='green'><i>Copyright@GupaoEDU</i></font>");
			e.printStackTrace();
		}
	}
	
	private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		//根据用户请求的url来获得一个handler
		RuHandlerMapping handler = getHandler(req);
		if(handler == null){
            resp.getWriter().write("<font size='25' color='red'>404 Not Found</font><br/><font color='green'><i>Copyright@GupaoEDU</i></font>");
            return;
        }
		
		RuHandlerAdapter adapter = getHandlerAdapter(handler);
		
		//获得处理结果
		RuModelAndView mv = adapter.handle(req, resp, handler);
		
		//输出结果
		processDispatchResult(resp, mv);
	}
	
	private RuHandlerMapping getHandler(HttpServletRequest req) {
		
		if(this.handlerMappings.isEmpty()) {
			return null;
		}
		
		String url = req.getRequestURI();
		String contextPath = req.getContextPath();
		url = url.replace(contextPath, "").replaceAll("/+", "/");
		for(RuHandlerMapping handler : handlerMappings) {
			Matcher matcher = handler.getPattern().matcher(url);
			if(!matcher.matches()) {
				continue;
			}
			return handler;
		}
		return null;
	}
	
	private RuHandlerAdapter getHandlerAdapter(RuHandlerMapping handler) {
		if(this.handlerAdapters.isEmpty()) {
			return null;
		}
		return this.handlerAdapters.get(handler);
	}
	
	private void processDispatchResult(HttpServletResponse resp, RuModelAndView mv) throws Exception {
		
		if(mv==null) {
			return;
		}
		
		if(this.viewResolvers.isEmpty()) {
			return;
		}
		
		for(RuViewResolver v : this.viewResolvers) {
			if(!v.getViewName().equals(mv.getViewName())) {
				continue;
			}
			String out = v.viewResolver(mv);
			if(null!=out) {
				resp.getWriter().write(out);
				break;
			}
		}
	}
	
}
