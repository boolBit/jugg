package com.lorabit.rpc.invoker;

/**
 * @author lorabit
 * @since 16-3-8
 */
public interface Invoker {

  public Object invoke(String name, Object[] parameters) throws Exception;

  public Class[] lookupParameterTypes(String name) throws Exception;

  public Class[][] lookupParameterizedType(String name) throws Exception;

}
