package com.lorabit.rpc.base;

import com.lorabit.rpc.exception.RpcException;
import com.lorabit.rpc.meta.BinaryPacketData;
import com.lorabit.rpc.meta.RpcRemoteLatch;
import com.lorabit.rpc.router.IOBalance;
import com.lorabit.rpc.router.impl.ModIOBalance;

import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lorabit
 * @since 16-3-7
 */
public class ClientProxyCtx<T> implements MethodInterceptor {
  private long timeout;
  private List<String> methodNames = new ArrayList<>();
  private String domainName;
  private IOBalance ioBalance;

  private T serviceProxy;

  private ClientProxyCtx(Class clazz, String group, List<String> urls) {
    this.domainName = clazz.getName();
    this.ioBalance = ModIOBalance.getInstance(group, urls);
    for (Method method : clazz.getDeclaredMethods()) {
      methodNames.add(method.getName());
    }
  }


  public static <T> ClientProxyCtx<T> createFactoryCtx(
      Class clazz,
      List<String> urls,
      String group,
      long timeout) {
    ClientProxyCtx client = new ClientProxyCtx<T>(clazz, group, urls);
    client.timeout = timeout;
    client.serviceProxy = (T) Enhancer.create(null, new Class[]{clazz}, client);
    return client;
  }

  public T getProxy() {
    return serviceProxy;
  }

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
      throws Throwable {
    String name = method.getName();
    if (!methodNames.contains(name)) {
      throw new RpcException("no such proxy method " + name);
    }

    BinaryPacketData packet = new BinaryPacketData();
    RpcConfig config = new RpcConfig();
    config.addConf("timebase", System.currentTimeMillis());
    packet.conf = config;
    packet.domain = domainName;
    packet.param = args;
    packet.method = name;

    Object ret;
    String url;
    RpcRemoteLatch latch = new RpcRemoteLatch(timeout);
    boolean failure = false;
    try {
      url = this.ioBalance.next(null);

    } catch (Exception e) {

    }


    return null;
  }
}
