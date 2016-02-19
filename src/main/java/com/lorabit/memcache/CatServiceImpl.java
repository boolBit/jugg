package com.lorabit.memcache;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author lorabit
 * @since 16-2-19
 */
public class CatServiceImpl implements ICatService {

  private static Logger logger = org.slf4j.LoggerFactory.getLogger(CatServiceImpl.class);

  private static final byte[] empty_bytes = new byte[0];

  @Resource
  MemcachedClient memcachedClient;


  @Override
  public boolean setstring(String key, String value, int ttl) {
    try {
      return memcachedClient.set(key, ttl, value);
    } catch (Exception e) {
      logger.error("缓存异常, key: " + key, e);
    }
    return false;
  }

  @Override
  public boolean addstring(String key, String value, int ttl) {
    try {
      return memcachedClient.add(key, ttl, value);
    } catch (Exception e) {
      logger.error("", e);
    }
    return false;
  }

  @Override
  public long incr(String key, long delta) {
    try {
      return memcachedClient.incr(key, delta);
    } catch (Exception e) {
      logger.error("", e);
    }
    return -1L;
  }

  @Override
  public String getstring(String key) {
    try {
      return (String) memcachedClient.get(key);
    } catch (Exception e) {
      logger.error("", e);
    }
    return null;
  }

  @Override
  public boolean delstring(String key) {
    try {
      return memcachedClient.delete(key);
    } catch (Exception e) {
      logger.error("", e);
    }
    return false;
  }

  public Map<String, String> getmultistring(List<String> key) {
    Map<String, String> ret = new HashMap<String, String>();
    try {
      Map<String, GetsResponse<String>> ret0;
      ret0 = memcachedClient.gets(key);
      for (Map.Entry<String, GetsResponse<String>> en : ret0.entrySet()) {
        if (en.getValue() != null) {
          ret.put(en.getKey(), en.getValue().getValue());
        }
      }
    } catch (Exception e) {
      logger.error("", e);
    }
    return ret;
  }

  @Override
  public byte[] getbytes(String key) {
    long ts = System.currentTimeMillis();
    try {
      Object ret = memcachedClient.get(key);
      if (ret == null) {
        return empty_bytes;
      }
      return (byte[]) ret;
    } catch (Exception e) {
      logger.error("", e);
    } finally {
      ts = System.currentTimeMillis() - ts;
      logger.debug("getbytes:  --> " + ts + "ms");
    }
    return empty_bytes;
  }

  @Override
  public boolean setbytes(String key, byte[] value, int ttl) {
    if (value == null) {
      return false;
    }
    long ts = System.currentTimeMillis();
    try {
      return memcachedClient.set(key, ttl, value);
    } catch (Exception e) {
      logger.error("", e);
    } finally {
      ts = System.currentTimeMillis() - ts;
      logger.debug("setbytes: " + value.length + " --> " + ts + "ms");
    }
    return false;
  }

  @Override
  public Map<String, String> mgetstring(String keys) {
    List<String> param = new ArrayList<String>();
    if (keys != null) {
      param.addAll(Arrays.asList(keys.split("\n")));
    }
    return getmultistring(param);
  }

  @Override
  public boolean acquireLock(String key, int ttl) {
    try {
      long r = memcachedClient.incr(key, 1L, 0L, 500, ttl);
      if (r == 0L) return true;
    } catch (Exception e) {
      logger.warn("acquireLock_failed:", e);
    }
    return false;
  }

  @Override
  public void releaseLock(String key) {
    try {
      memcachedClient.delete(key);
    } catch (Exception e) {
      logger.warn("releaseLock_failed:", e);
    }
  }

}
