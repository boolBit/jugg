package com.lorabit.net.nettyserver.telnet;

/**
 * @author lorabit
 * @since 16-3-7
 */
public class Dog {
  long time;

  public Dog(long l) {
    this.time = l;
  }

  public void Dog() {
  }

  public void bark() {
    System.out.println("bark bark bark..." + time);
  }
}
