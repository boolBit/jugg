package com.lorabit.rpc.server;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author lorabit
 * @since 16-3-8
 *
 * 暴露的接口和对应的服务提供者
 */
public class ServiceConfig {

  private Map<Class, Object> services = Maps.newHashMap();


  public void addService(Class clz, Object obj) {
    services.put(clz, obj);
  }

  public Map<Class, Object> getServices() {
    return services;
  }

  public void setServices(Map<Class, Object> services) {
    this.services = services;
  }
}
