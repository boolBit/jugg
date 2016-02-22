package com.lorabit.base;

import com.lorabit.base.support.CCT;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author lorabit
 * @since 16-2-21
 */
@Aspect
@Component
public class AspectAuditor {

  @Around("execution(public * com.lorabit.dao..*.*(..))")
  public Object onProcess(ProceedingJoinPoint point) throws Throwable {
    return handler(point) ;
  }

  private Object handler(ProceedingJoinPoint point) throws Throwable {
    try {
      CCT.call(mkName(point), false);
      return point.proceed();
    }finally{
      CCT.ret();
    }
  }

  private String mkName(ProceedingJoinPoint point) {
    return point.getSignature().getDeclaringTypeName()+"::"+point.getSignature().getName();
  }


}

