package com.company.courseManager.interceptor;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.company.security.utils.SecurityConst;
@Service("tokenInterceptor")
public class TokenInterceptor implements HandlerInterceptor,InitializingBean {
	//本届点允许访问
	private long WhiteList_Status_allow =1;
	//本届点不允许访问
	private long WhiteList_Status_notAllow =2;
	//子节点都允许访问
	private long WhiteList_Status_allChildAllow =3;
	//子节点条件允许访问
	private long WhiteList_Status_conditionChildAllow =4;
		
	private String userKeyPrefix = "!@#";
	
	
	@Value("${controller.urlWhiteList:/home/login}")  
	private String urlWhiteList;	
	
	@Value("${controller.onlyAuthAjax:0}")  
	private int onlyAuthAjax;	
	
	@Value("${controller.loginUrl:/home/login}")  
	private String loginUrl;	
	
	@Value("${controller.urlWhiteListUserkey:registerByCode,loginByPass,loginByAuthCode,getSmsValid,getRandom,getRsaPubKey,resetPassByAuthCode}")  
	private String urlWhiteListUserkey;
	
	@Resource (name = "redisTemplate")
	protected RedisTemplate<Object, Object> redisTemplate;
	
	@Value("${token.expireMillSeconds:1800000}")  
	private int tokenExpireSeconds;
	
	@Value("${token.redisDuration:36000}")  
	private int durationSeconds = 3600;
	/**
	 * 用户rest的白名单key
	 */
	
	/**
	 * Long 0 -- need token 1--not need token
	 */
	private Map<String,Long> controllerWhistMap = new java.util.concurrent.ConcurrentHashMap<String,Long>();
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 判断是否是ajax请求
	 * @param request
	 * @return true-是ajax请求
	 */
	protected boolean isAjaxRequest(HttpServletRequest request)
	{
		String ajaxRequest = request.getHeader("x-requested-with");
	    if(ajaxRequest.contains("XMLHttpRequest"))
	    {
	    	return true;
	    }
	    return false;
	}
	/**
	 * 
	 * @param token
	 * @return
	 */
	protected long getSessionAccessTime(String token)
	{
		try {
			String accessKey  =SecurityConst.getTokenRediskey(token);
			ValueOperations<Object, Object> opsForValue = redisTemplate.opsForValue();
			String accessTime = (String)(opsForValue.get(accessKey)); 
			if(StringUtils.isEmpty(accessTime))
			{
				return 0;
			}
			
			long tokenCreateTime = Long.parseLong(accessTime);
			
			if(System.currentTimeMillis() - tokenCreateTime>tokenExpireSeconds)
			{
				return 0;
			}
			if(System.currentTimeMillis() - tokenCreateTime>600000)
			{
				opsForValue.set(accessKey, String.valueOf(System.currentTimeMillis()),durationSeconds,TimeUnit.SECONDS);
			
			}
			return Long.parseLong(accessTime);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return 0;
	}
	/**
	 * 判断是否允许方位兄台那个资源
	 * @param request
	 * @return
	 */
	protected boolean allowAccess(HttpServletRequest request)
	{
		//绝大多数是有token的优先token
		String token = request.getHeader("token");
		long tokenTime = getSessionAccessTime(token);
		boolean isAuth = (tokenTime!=0);
		if(isAuth)
		{
			return true;
		}
		//判断是否仅仅需要拦截onlAuthAjax
		if(onlyAuthAjax==1)
		{
			if(!this.isAjaxRequest(request))
			{
				return true;
			}
		}
		else if(onlyAuthAjax==2)
		{
			return true;
		}
			
		//判断是否需要进行拦截
		String requestUrl = request.getRequestURI(); 
		//如果请求的是登录页面，直接允许访问
	    if(requestUrl.equalsIgnoreCase(this.loginUrl))
	    {
	    	return true;
	    }
		if(!this.isNeedToken(requestUrl))
	    {
	    	return true;
	    }
		if(requestUrl.startsWith("/user"))
		{
			String[] requestUrlKey = requestUrl.split("/");
			if(requestUrlKey.length>0)
			{
				String userKey = userKeyPrefix+ requestUrlKey[requestUrlKey.length-1].trim().toLowerCase();
				if(!this.isNeedToken(userKey))
			    {
			    	return true;
			    }
			}
		}
		return false;
	}

	
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		String requestUrl = request.getRequestURI();		
		//response.setHeader("Access-Control-Allow-Origin", "*");
	    //todo: not redirect
		if(allowAccess(request))
	    //if(true)
		{
	    	//System.out.print("return true");
	    	return true;
	    }
	    else
	    {
	    	response.sendRedirect(this.loginUrl);
	    	System.out.print("return false;");
	    	return false;
	    }
	    /*
		System.out.println("**********" + request.getHeaderNames());
		Enumeration headerNames = request.getHeaderNames();
	    while (headerNames.hasMoreElements()) {
	        String key = (String) headerNames.nextElement();
	        String value = request.getHeader(key);
	        System.out.println(key + ":" + value);
	    }
	    
	    System.out.println("request url:" +request.getServletPath());
	    System.out.println("request url1:" +requestUrl);
	    */
	    /**
	     *  public String defultLogin="/account/login";//默认登录页面  
	     *  response.sendRedirect(request.getContextPath()+defultLogin);
	     */
	    /**
	     * String url = request.getRequestURI();  
        //URL:login.jsp是公开的;这个demo是除了login.jsp是可以公开访问的，其它的URL都进行拦截控制  
        if(url.indexOf("login.action")>=0){  
            return true;  
        }  
        //获取Session  
        HttpSession session = request.getSession();  
        String username = (String)session.getAttribute("username");  
          
        if(username != null){  
            return true;  
        }  
        //不符合条件的，跳转到登录界面  
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);  
          response.sendRedirect("/login.jsp");
        return false;  
	     
	    System.out.println("**********" + response);
		
		System.out.println("**********" + handler);
		
		if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {  
			System.out.println("&&&&&&&&&&&" + handler.getClass());
            return true;  
        }  
		final HandlerMethod handlerMethod = (HandlerMethod) handler;  
        final Method method = handlerMethod.getMethod();  
        final Class<?> clazz = method.getDeclaringClass();  
		return false;
		*/
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		String[] controllerWhiteLists = urlWhiteList.split(",");
		for(int i=0;i<controllerWhiteLists.length;i++)
		{
			System.out.println(controllerWhiteLists[i]);
			controllerWhistMap.put(controllerWhiteLists[i].trim().toLowerCase(), new Long(1));	
		}
		controllerWhiteLists = this.urlWhiteListUserkey.split(",");
		
		for(int i=0;i<controllerWhiteLists.length;i++)
		{
			String userKey = userKeyPrefix+ controllerWhiteLists[i].trim().toLowerCase();
			System.out.println();
			controllerWhistMap.put(userKey, new Long(1));	
		}
		
				
	}
	/**
	 * 是否拦截，
	 * @param requestUrl
	 * @return true -- 必须登录过才能访问， false-- 不需要登录过
	 */
	protected  boolean isNeedToken(String requestUrl) {
		// TODO Auto-generated method stub
		System.out.println("request url:" +requestUrl);
		if(!controllerWhistMap.containsKey(requestUrl.toLowerCase().trim()))
		{
			return true;
		}
		Long needToken = controllerWhistMap.get(requestUrl);
		
		return (needToken.longValue()==0);
				
	}

}
