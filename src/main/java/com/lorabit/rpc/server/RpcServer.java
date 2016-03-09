package com.lorabit.rpc.server;

import com.lorabit.rpc.processor.Processor;

/**
 * @author lorabit
 * @since 16-3-9
 */
public interface RpcServer {

  void start();

  void setProcess(Processor processor);

  void stop();
}
