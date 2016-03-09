package com.lorabit.rpc.server;

import com.lorabit.rpc.demo.impl.DemoService;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author lorabit
 * @since 16-3-8
 */
@Data
public class ServiceExporter {
  private List<Object> services;
  private List<String> exportedInterfaces;  //used for node register zk...
  private RpcServerBootstrap boot;
  private int port;
  private long maxLatency;

  public void init() {
    boot = new RpcServerBootstrap();
    for (Object service : services) {
      Class[] interfaces = service.getClass().getInterfaces();
      Class serviceClz = null;
      for (Class itf : interfaces) {
        if (itf.getName().startsWith("I")) {
          serviceClz = itf;
          break;
        }
      }
      if (serviceClz == null) {
        serviceClz = interfaces[0];
      }
      exportedInterfaces.add(serviceClz.getName());
      boot.addService(serviceClz, service);
    }
    boot.startUp(port);
    System.out.println("started server at " + port + "successfully");
  }

  public static void main(String[] args) {
    ServiceExporter exporter = new ServiceExporter();
    DemoService service = new DemoService();

    exporter.services = new ArrayList<>();
    exporter.services.add(service);
    exporter.exportedInterfaces = new ArrayList<>();
    exporter.port = 8044;
    exporter.init();
  }
}
