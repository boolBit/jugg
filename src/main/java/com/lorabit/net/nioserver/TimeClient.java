package com.lorabit.net.nioserver;

/**
 * @author lorabit
 * @since 16-2-18
 *
 * a simple round call response style nio client
 */
public class TimeClient {

  public static void main(String[] args) {
    new Thread(new NioClient(8070),"nio timeclient 001").start();
  }
}
