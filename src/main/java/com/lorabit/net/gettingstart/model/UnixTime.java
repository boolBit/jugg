package com.lorabit.net.gettingstart.model;

import java.util.Date;

/**
 * @author lorabit
 * @since 16-2-29
 */
public class UnixTime {

  private final long value;

  public UnixTime() {
    this(System.currentTimeMillis() / 1000L + 2208988800L);
  }

  public UnixTime(long value) {
    this.value = value;
  }

  public long value() {
    return value;
  }

  @Override
  public String toString() {
    return new Date((value() - 2208988800L) * 1000L).toString();
  }
}