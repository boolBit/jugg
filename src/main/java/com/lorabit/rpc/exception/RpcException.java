package com.lorabit.rpc.exception;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcException extends Exception {
  public RpcException(String s) {
    super(s);
  }

  public RpcException(Exception e) {
    super(e);
  }

  public RpcException(Throwable tb) {
    super(tb);
  }

  public RpcException(String s, Exception e) {
    super(s, e);
  }
}
