package com.lorabit.net.nioserver;

/**
 * @author lorabit
 * @since 16-2-18
 *
 * a nio server reply now
 */
public class TimeServer {

  public static void main(String[] args) {
    new Thread(new NioSelectableServer(8070),"nio time server 001").start();
  }

}
