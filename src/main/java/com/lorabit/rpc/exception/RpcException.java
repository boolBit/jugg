package com.lorabit.rpc.exception;

import java.io.IOException;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcException extends Exception {
  public RpcException(String s) {
    super(s);
  }

  public RpcException(IOException e) {
    super(e);
  }

  public RpcException(Exception e) {
    super(e);
  }
}
