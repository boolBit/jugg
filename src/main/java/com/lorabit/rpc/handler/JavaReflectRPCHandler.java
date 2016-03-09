package com.lorabit.rpc.handler;

import com.lorabit.rpc.base.ClientId;
import com.lorabit.rpc.exception.RpcException;
import com.lorabit.rpc.invoker.Invoker;
import com.lorabit.rpc.invoker.JavaReflectInvoker;
import com.lorabit.rpc.processor.RpcContext;
import com.lorabit.rpc.server.ServiceConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class JavaReflectRPCHandler implements RpcHandler {

  private ServiceConfig cfg;

  public void setCfg(ServiceConfig cfg) {
    this.cfg = cfg;
  }

  private Map<String, Invoker> invokers = new HashMap<>();

  public void init() {
    for (Map.Entry<Class, Object> entry : cfg.getServices().entrySet()) {
      String name = entry.getKey().getName();
      Invoker invoker = new JavaReflectInvoker(new ClientId(name, false), entry.getKey(), entry.getValue());
      invokers.put(name, invoker);
    }
  }

  @Override
  public void lookUp(RpcContext ctx) throws RpcException {
    Invoker invoker = invokers.get(ctx.name);
    if (invoker == null)
      throw new RpcException("cant find service for" + ctx.name);
    ctx.invoker = invoker;
  }

  @Override
  public void invoke(RpcContext ctx) throws RpcException {
    ctx.ret = ctx.invoker.invoke(ctx.method, ctx.params);
  }
}
