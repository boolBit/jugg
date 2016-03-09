package com.lorabit.rpc.exception;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class RpcRuntimeException extends RuntimeException {
  public RpcRuntimeException(){
    super();
  }

  public RpcRuntimeException(String msg){
    super(msg);
  }
  public RpcRuntimeException(Throwable t){
    super(t);
  }

  public RpcRuntimeException(String msg, Throwable t1){
    super(msg,t1);
  }
}
