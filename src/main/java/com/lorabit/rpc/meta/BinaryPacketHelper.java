package com.lorabit.rpc.meta;

import com.lorabit.rpc.base.RpcConfig;
import com.lorabit.rpc.exception.RpcException;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class BinaryPacketHelper {

  public static BinaryPacketData fromRawToData(BinaryPacketRaw raw) throws RpcException {
    BinaryPacketData ret = new BinaryPacketData();
    ret.flag = raw.flag;
    ret.version = raw.version;

    ret.conf = (RpcConfig) bufToObj(raw.conf);
    ret.uuid = raw.uuid;
    ret.domain = bufToStr(raw.domainName);
    ret.method = bufToStr(raw.methodName);
    ret.param = (Object[]) bufToObj(raw.parameter);
    if (ret.param == null) {
      ret.param = new Object[0];
    }
    ret.ret = bufToObj(raw.ret);
    ret.ex = (Throwable) bufToObj(raw.error);
    return ret;
  }


  static private String bufToStr(ByteBuffer buf) {
    if (buf == null) {
      return null;
    }
    return new String(buf.array(), buf.arrayOffset(), buf.remaining());
  }

  static private Object bufToObj(ByteBuffer buf) throws RpcException {
    if (buf == null) {
      return null;
    }
    ByteArrayInputStream bis = new ByteArrayInputStream(buf.array(), buf.arrayOffset(), buf.remaining());
    ObjectInputStream ois;
    try {
      ois = new ObjectInputStream(bis);
      return ois.readObject();
    } catch (Exception e) {
      throw new RpcException(e);
    }
  }
}
