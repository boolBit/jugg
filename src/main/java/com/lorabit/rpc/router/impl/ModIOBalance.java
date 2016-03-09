package com.lorabit.rpc.router.impl;

import com.lorabit.rpc.router.IORouter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class ModIOBalance implements IORouter {

  private static Map<String, ModIOBalance> caches = new ConcurrentHashMap<>();

  List<String> endPoints;
  int count ;
  int faliureCount;

  public ModIOBalance() {
  }

  public ModIOBalance(List<String> endPoints) {
    this.endPoints = endPoints;
  }

  public static IORouter getInstance(String group, List<String> urls) {
    ModIOBalance ioBalance;
    if (!caches.containsKey(group)) {
      synchronized (ModIOBalance.class) {
        ioBalance = new ModIOBalance();
        ioBalance.endPoints = urls;
        ioBalance.count = 1;
        ioBalance.faliureCount = 0;
        caches.put(group, ioBalance);
      }
    }
    return caches.get(group);
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
