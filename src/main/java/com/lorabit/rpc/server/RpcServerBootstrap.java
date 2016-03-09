package com.lorabit.rpc.server;

import com.lorabit.rpc.handler.JavaReflectRPCHandler;
import com.lorabit.rpc.meta.BinaryPacketRaw;
import com.lorabit.rpc.processor.Processor;
import com.lorabit.rpc.processor.ThreadPoolProcessor;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-3-9
 */
@Data
public class RpcServerBootstrap {

  private ServiceConfig cfg = new ServiceConfig();
  private Processor<BinaryPacketRaw> processor;
  private JavaReflectRPCHandler handler;

  public void startUp(int port) {
    initJavaHandler(cfg);
    initThreadPoolProcessor();
    TcpServer tcpServer = new TcpServer();

    tcpServer.setProcessor(processor);
    tcpServer.start(port);
  }

  private void initJavaHandler(ServiceConfig cfg) {
    handler = new JavaReflectRPCHandler();
    handler.setCfg(cfg);
    handler.init();
  }

  private void initThreadPoolProcessor() {
    processor = new ThreadPoolProcessor();
    processor.setHandler(handler);
  }

  public void addService(Class serviceClz, Object service) {
    cfg.addService(serviceClz, service);
  }


}
