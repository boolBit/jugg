package com.lorabit.rpc.exception;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcTimeoutException extends RpcRuntimeException {

  private static final long serialVersionUID = 5176960197781220436L;

  public RpcTimeoutException(){
    super();
  }

  public RpcTimeoutException(String msg){
    super(msg);
  }

}
