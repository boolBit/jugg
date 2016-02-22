package com.lorabit.net.aioserver;

/**
 * @author lorabit
 * @since 16-2-22
 */
public class Client {
  public static void main(String[] args) {
    new Thread(new AsyncTimeClient("localhost",8040)).start();
  }
}
