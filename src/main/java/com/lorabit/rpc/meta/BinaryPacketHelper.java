package com.lorabit.rpc.meta;

import com.lorabit.rpc.base.RpcConfig;
import com.lorabit.util.KryoUtil;

import java.nio.ByteBuffer;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class BinaryPacketHelper {

  public static BinaryPacketData fromRawToData(BinaryPacketRaw raw) throws Exception {
    BinaryPacketData ret = new BinaryPacketData();
    ret.flag = raw.flag;
    ret.version = raw.version;

    ret.conf = (RpcConfig) bufToObjUseKryo(raw.conf);
    ret.uuid = raw.uuid;
    ret.domain = (String) bufToObjUseKryo(raw.domainName);
    ret.method = (String) bufToObjUseKryo(raw.methodName);
    ret.param = (Object[]) bufToObjUseKryo(raw.parameter);
    if (ret.param == null) {
      ret.param = new Object[0];
    }
    ret.ret = bufToObjUseKryo(raw.ret);
    ret.ex = (Throwable) bufToObjUseKryo(raw.error);
    return ret;
  }


  private static String bufToStr(ByteBuffer buf) {
    if (buf == null) {
      return null;
    }
    return new String(buf.array(), buf.arrayOffset(), buf.remaining());
  }

//  private static Object bufToObj(ByteBuffer buf) throws RpcException {
//    if (buf == null) {
//      return null;
//    }
//    ByteArrayInputStream bis = new ByteArrayInputStream(buf.array(), buf.arrayOffset(), buf.remaining());
//    ObjectInputStream ois;
//    try {
//      ois = new ObjectInputStream(bis);
//      return ois.readObject();
//    } catch (Exception e) {
//      throw new RpcException(e);
//    }
//  }

  private static Object bufToObjUseKryo(ByteBuffer buf) {
    if (buf == null) {
      return null;
    }
    return KryoUtil.deserialize(buf.array(), buf.arrayOffset(), buf.remaining());
  }



}
