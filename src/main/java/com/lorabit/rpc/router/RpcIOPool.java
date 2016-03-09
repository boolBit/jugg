package com.lorabit.rpc.router;

import com.lorabit.rpc.base.LifeCycle;
import com.lorabit.rpc.exception.RpcException;
import com.lorabit.rpc.exception.RpcNoMoreConnException;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcIOPool implements LifeCycle {

  private ConcurrentHashMap<String, GenericObjectPool<RpcIOSession>> pools = new ConcurrentHashMap<>();
  private boolean closed = false;
  private long timeout = 2000;

  public RpcIOSession getSession(String url) throws RpcException {
    if (closed) {
      throw new RpcException("io session has been closed...");
    }

    GenericObjectPool<RpcIOSession> pool = pools.get(url);
    if (pool == null) {
      pool = createPool(url);
      pools.putIfAbsent(url, pool);
      pool = pools.get(url);
    }
    try {
      return pool.borrowObject(timeout);
    } catch (Exception e) {
      throw new RpcNoMoreConnException(e);
    }
  }

  private GenericObjectPool<RpcIOSession> createPool(String url) {
    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
    config.setMaxIdle(20);
    config.setMinIdle(0);
    config.setMaxTotal(50);
    config.setTestWhileIdle(false);
    config.setBlockWhenExhausted(true);
    config.setMaxWaitMillis(timeout);
    config.setMinEvictableIdleTimeMillis(120000);
    config.setTestOnBorrow(true);
    return new GenericObjectPool<>(new IOSessionFactory(url), config);
  }

  public void releaseIOSession(RpcIOSession session) {
    if (session == null) {
      return;
    }
    GenericObjectPool<RpcIOSession> pool = pools.get(session.getConnHostPort());
    if (session.isAlive() && pool != null) {
      pool.returnObject(session);
    } else {
      if (pool == null) {
        try {
          session.close();
        } catch (IOException e) {
        }
        return;
      }
      try {
        pool.invalidateObject(session);
      } catch (Exception e) {
      }
    }
  }


  class IOSessionFactory implements PooledObjectFactory<RpcIOSession> {

    String url;

    public IOSessionFactory(String url) {
      this.url = url;
    }

    @Override
    public PooledObject<RpcIOSession> makeObject() throws Exception {
      RpcIOSession session = new RpcIOSession(url, timeout);
      return new DefaultPooledObject<>(session);
    }

    @Override
    public void destroyObject(PooledObject<RpcIOSession> p) throws Exception {
      p.getObject().close();
    }

    @Override
    public boolean validateObject(PooledObject<RpcIOSession> p) {
      return p.getObject().isAlive();
    }

    @Override
    public void activateObject(PooledObject<RpcIOSession> p) throws Exception {
      RpcIOSession obj = p.getObject();
      obj.init();
    }

    @Override
    public void passivateObject(PooledObject<RpcIOSession> p) throws Exception {

    }
  }


  @Override
  public void init() {
  }

  @Override
  public boolean isAlive() {
    return !closed;
  }

  @Override
  public void close() throws IOException {
      closed = true;
    if (pools == null) {
      return;
    }
    for (Map.Entry<String, GenericObjectPool<RpcIOSession>> en : pools.entrySet()) {
      en.getValue().close();
    }
  }


}
