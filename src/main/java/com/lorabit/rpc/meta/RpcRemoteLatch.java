package com.lorabit.rpc.meta;

import com.lorabit.rpc.exception.RpcRuntimeException;
import com.lorabit.rpc.exception.RpcTimeoutException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.netty.util.AttributeKey;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcRemoteLatch {

  final static public String LATCH_NAME = "RPCRemoteLatch";
  final static public AttributeKey<RpcRemoteLatch> LATCH_KEY = AttributeKey.valueOf(LATCH_NAME);

  private CountDownLatch latch;
  private long timeout;
  private Object ret;
  private Throwable ex;
  private long uuid;

  public RpcRemoteLatch() {
    this(500);
  }

  public RpcRemoteLatch(long time) {
    latch = new CountDownLatch(1);
    this.timeout = time;
  }

  public void offerResult(Object ret) {
    this.ret = ret;
    latch.countDown();
  }

  public void offerError(Throwable ex) {
    this.ex = ex;
    latch.countDown();
  }

  public Object getResult() throws Throwable {
    try {
      if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
        throw new RpcTimeoutException();
      }
      if (this.ex != null)
        throw this.ex;
    } catch (InterruptedException e) {
      throw new RpcRuntimeException("Interrupted", e);
    }
    return this.ret;
  }

  public long getUuid() {
    return uuid;
  }

  public void setUuid(long uuid) {
    this.uuid = uuid;
  }
}
