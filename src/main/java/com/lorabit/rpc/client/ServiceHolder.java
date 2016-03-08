package com.lorabit.rpc.client;

import com.lorabit.rpc.base.RpcClientFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-3-7
 */
@Data
public class ServiceHolder {

  private String interfaceName;
  private Class interfaceClz;
  private RpcClientFactory factory;


  public void init() {
    try {
      interfaceClz = Class.forName(interfaceName);
      createRpcStub();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void createRpcStub() {
    RpcClientFactory factory = RpcClientFactory.createFactory(interfaceClz);
    this.factory = factory;
  }

  public Object create() {
    if (!interfaceClz.isInterface()) {
      throw new IllegalArgumentException(interfaceName + "is not a interface");
    }
    return Proxy.newProxyInstance(null, new Class[]{interfaceClz}, new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(get(), args);
      }
    });
  }

  private Object get() {
    return factory.create();
  }


}
