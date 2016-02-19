package com.lorabit.memcache;

import java.util.Map;

/**
 * @author lorabit
 * @since 16-2-19
 */
public interface ICatService {

  boolean setstring(String key, String value, int ttl);

  boolean addstring(String key, String value, int ttl);

  long incr(String key, long delta);

  public String getstring(String key);

  boolean delstring(String key);

  public Map<String, String> mgetstring(String keys);

  public byte[] getbytes(String key);

  boolean setbytes(String key, byte[] value, int ttl);

  boolean acquireLock(String key, int ttl);

  void releaseLock(String key);
}

