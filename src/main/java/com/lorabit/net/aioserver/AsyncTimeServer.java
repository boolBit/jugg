package com.lorabit.net.aioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author lorabit
 * @since 16-2-22
 */
public class AsyncTimeServer implements Runnable {
  private int port;
  CountDownLatch latch;
  public AsynchronousServerSocketChannel server;

  public AsyncTimeServer(int port) {
    this.port = port;
    try {
      server = AsynchronousServerSocketChannel.open();
      server.bind(new InetSocketAddress(port));
      System.out.println("server started...");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    latch = new CountDownLatch(1);
    try {
      doAccept();
      latch.await();
    } catch (InterruptedException e) {
      latch.countDown();
      e.printStackTrace();
    }
  }

  private void doAccept() {
    server.accept(this, new AcceptCompletionHandler());
  }
}
