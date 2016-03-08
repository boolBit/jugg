package com.lorabit.rpc.base;

/**
 * @author lorabit
 * @since 16-3-7
 */
public interface ServiceFactory<T> {
  T create();

  void release();

  String getServiceInterfaceName();

  Class getServiceInterfaceClz();
}
