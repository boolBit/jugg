package com.lorabit.rpc.router.impl;

import com.google.common.collect.Maps;

import com.lorabit.rpc.router.IOBalance;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class ModIOBalance implements IOBalance {

  private static Map<String, ModIOBalance> caches = Maps.newConcurrentMap();

  List<String> endPoints;
  int count;
  int faliureCount;

  public ModIOBalance() {
  }

  public ModIOBalance(List<String> endPoints) {
    this.endPoints = endPoints;
  }

  public static IOBalance getInstance(String group, List<String> urls) {
    ModIOBalance ioBalance = caches.get(group);
    if (ioBalance == null) {
      synchronized (ModIOBalance.class) {
        ioBalance = new ModIOBalance();
        ioBalance.endPoints = urls;
        ioBalance.count = 0;
        ioBalance.faliureCount = 0;
        caches.put(group, ioBalance);
      }
    }
    return ioBalance;
  }

  @Override
  public String next(String token) {
    int idx = (endPoints.size() - 1) % count;
    count++;
    if (count > (2 << 7))
      count = 0;
    return endPoints.get(idx);
  }

  @Override
  public void updateLoad(Map<String, Integer> load) {

  }

  @Override
  public void fail(String token) {
      faliureCount++;
  }
}
