package com.lorabit.rpc.client;

import com.lorabit.rpc.base.ServiceFactory;

import java.util.List;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-3-7
 */
@Data
public abstract class RpcClientFactory<T> implements ServiceFactory<T> {

  private String group ="demo";
  private List<String> serviceEndPoints;
  private long timeout = 4000L;


  public static <T1> RpcClientFactory<T1> createFactory(final Class<T1> interfaceClz) {
    final String name = interfaceClz.getName();
    return new RpcClientFactory<T1>() {
      @Override
      public String getServiceInterfaceName() {
        return name;
      }

      @Override
      public Class getServiceInterfaceClz() {
        return interfaceClz;
      }
    };
  }

  @Override
  public T create() {
    ClientProxyCtx<T> proxyCtx = ClientProxyCtx.createFactoryCtx(
        getServiceInterfaceClz(),
        serviceEndPoints,
        group,
        timeout);
    return proxyCtx.getProxy();
  }

  @Override
  public void release() {

  }



}
