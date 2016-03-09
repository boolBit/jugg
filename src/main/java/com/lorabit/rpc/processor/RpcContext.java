package com.lorabit.rpc.processor;

import com.lorabit.rpc.invoker.Invoker;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcContext {
  public String name;
  public String method;
  public Object[] params;
  public Invoker invoker;
  public Object ret;
  public Throwable ex;
}
