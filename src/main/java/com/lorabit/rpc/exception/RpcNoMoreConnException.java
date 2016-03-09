package com.lorabit.rpc.exception;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcNoMoreConnException extends RpcException {
  public RpcNoMoreConnException(String s) {
    super(s);
  }

  public RpcNoMoreConnException(Throwable tb){
    super(tb);
  }
}
