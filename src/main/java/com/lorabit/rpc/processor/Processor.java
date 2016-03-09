package com.lorabit.rpc.processor;

import com.lorabit.rpc.exception.RpcException;
import com.lorabit.rpc.handler.RpcHandler;

/**
 * @author lorabit
 * @since 16-3-8
 */
public interface Processor<T> {
  void process(RpcContext ctx, T ret) throws RpcException;

  void setHandler(RpcHandler handler);
}
