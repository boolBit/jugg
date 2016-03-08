package com.lorabit.rpc.client;

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author lorabit
 * @since 16-3-7
 */
public class ClientInterceptor implements MethodInterceptor{

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    return null;
  }
}
