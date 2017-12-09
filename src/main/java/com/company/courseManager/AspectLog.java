/**
 * 
 */
package com.company.courseManager;

import java.util.Arrays;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

/**
 * 日志切面类 
 */
//申明是个切面
@Aspect
//申明是个spring管理的bean
@Component
@Order(2)
public class AspectLog {
  private Logger log = LoggerFactory.getLogger(getClass());
  private Gson gson = new Gson();
  //申明一个切点 里面是 execution表达式 com.company.coursestudent
  @Pointcut("execution(public * com.company.courseManager.service..*.*(..)) ||  execution(public * com.company.courseManager.controller.rest..*.*(..)) || execution(public * com.company.courseManager.fastDfs..*.*(..)) || execution(public * com.company.coursestudent..*.*(..))")
  public void mylogPoint(){}
  
  /**
   * 请求前打印内容
   * @param joinPoint
   */
  @Before(value = "mylogPoint()")
  public void methodBefore(JoinPoint joinPoint){	  
//   ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//   HttpServletRequest request = requestAttributes.getRequest();
   log.info("before "+joinPoint.getSignature().getName()+" ,args:"+ Arrays.toString(joinPoint.getArgs()));
  }
  
  /**
   * 在方法执行完结后打印返回内容
   * @param joinPoint
   * @param o
   */
  @AfterReturning(returning = "o",pointcut = "mylogPoint()")
  public void methodAfterReturing(JoinPoint joinPoint,Object o){
   log.info("after "+joinPoint.getSignature().getName()+", Response:"+o);
  }
  
}