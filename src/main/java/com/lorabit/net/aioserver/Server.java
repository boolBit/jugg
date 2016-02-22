package com.lorabit.net.aioserver;

/**
 * @author lorabit
 * @since 16-2-22
 */
public class Server {

  public static void main(String[] args) {
    new Thread(new AsyncTimeServer(8040),"aio server").start();
  }
}
