package com.lorabit.rpc.router;

import com.lorabit.rpc.base.LifeCycle;

import java.io.IOException;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcIOPool implements LifeCycle {

  @Override
  public void init() {

  }

  @Override
  public boolean isAlive() {
    return false;
  }

  @Override
  public void close() throws IOException {

  }


}
