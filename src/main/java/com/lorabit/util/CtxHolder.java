package com.lorabit.util;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author lorabit
 * @since 16-2-18
 */
public class CtxHolder implements ApplicationContextAware {

  private static  ApplicationContext applicationContext;

  public final static ObjectMapper mapper = new ObjectMapper();

  {
    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext =applicationContext;
  }

  public static ApplicationContext getCtx(){
    return applicationContext;
  }

  public static Object getBean(String name){
    return applicationContext.getBean(name);
  }


}
