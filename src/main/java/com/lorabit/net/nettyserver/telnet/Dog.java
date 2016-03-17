package com.lorabit.net.nettyserver.telnet;

import java.io.Serializable;

/**
 * @author lorabit
 * @since 16-3-7
 */
public class Dog implements Serializable{


  private static final long serialVersionUID = 7513733563073010382L;
  public long time =234;

  public Dog(long l) {
    this.time = l;
  }

  public  Dog() {
  }

  public void bark() {
    System.out.println("bark bark bark..." + time);
  }

}
