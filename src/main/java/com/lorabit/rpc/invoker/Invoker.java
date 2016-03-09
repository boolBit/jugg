package com.lorabit.rpc.invoker;

import com.lorabit.rpc.exception.RpcException;

/**
 * @author lorabit
 * @since 16-3-8
 */
public interface Invoker {

  public Object invoke(String name, Object[] parameters) throws RpcException;

  public Class[] lookupParameterTypes(String name) throws RpcException;

  public Class[][] lookupParameterizedType(String name) throws RpcException;

}
