package com.lorabit.rpc.client;

import com.lorabit.rpc.demo.IDemoService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

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

  private List<String> staticEndPoints;

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
    factory.setServiceEndPoints(staticEndPoints);
    this.factory = factory;
  }

  public Object create() {
    if (!interfaceClz.isInterface()) {
      throw new IllegalArgumentException(interfaceName + "is not a interface");
    }
    return Proxy.newProxyInstance(ServiceHolder.class.getClassLoader(), new Class[]{interfaceClz}, new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(get(), args);
      }
    });
  }

  private Object get() {
    return factory.create();
  }


  public static void main(String[] args) {
    ServiceHolder holder = new ServiceHolder();
    holder.interfaceName = "com.lorabit.rpc.demo.IDemoService";
    holder.staticEndPoints = new ArrayList<>();
    holder.staticEndPoints.add("localhost:8044");
    holder.init();

    Thread.currentThread().setName("ServiceHolder");

    IDemoService service = (IDemoService) holder.create();
    String now = service.now();


    System.out.println(now);
//    now = service.now();
    System.out.println(now);
  }


}
