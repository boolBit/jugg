package com.lorabit.rpc.base;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author lorabit
 * @since 16-3-8
 */
public interface LifeCycle  extends Closeable{

  void init() throws IOException;

  boolean isAlive();

}
