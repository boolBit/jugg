package com.lorabit.rpc.meta;

import java.nio.ByteBuffer;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author lorabit
 * @since 16-3-8
 */
public class BinaryPacketRaw {
  protected int total = -1;
  protected float version = 1.0f;
  protected int flag = -1;
  protected long uuid = 0;
  protected int szConf = -1;
  protected ByteBuffer conf;
  protected int szDomainName = -1;
  protected ByteBuffer domainName;
  protected int szMethodName = -1;
  protected ByteBuffer methodName;
  protected int szParameter = -1;
  protected ByteBuffer parameter;
  protected int szRet = -1;
  protected ByteBuffer ret;
  protected int szError = -1;
  protected ByteBuffer error;

  public ChannelHandlerContext ctx;

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public float getVersion() {
    return version;
  }

  public void setVersion(float version) {
    this.version = version;
  }

  public int getFlag() {
    return flag;
  }

  public void setFlag(int flag) {
    this.flag = flag;
  }

  public long getUuid() {
    return uuid;
  }

  public void setUuid(long uuid) {
    this.uuid = uuid;
  }

  public int getSzConf() {
    return szConf;
  }

  public void setSzConf(int szConf) {
    this.szConf = szConf;
  }

  public ByteBuffer getConf() {
    return conf;
  }

  public void setConf(ByteBuffer conf) {
    this.conf = conf;
  }

  public int getSzDomainName() {
    return szDomainName;
  }

  public void setSzDomainName(int szDomainName) {
    this.szDomainName = szDomainName;
  }

  public ByteBuffer getDomainName() {
    return domainName;
  }

  public void setDomainName(ByteBuffer domainName) {
    this.domainName = domainName;
  }

  public int getSzMethodName() {
    return szMethodName;
  }

  public void setSzMethodName(int szMethodName) {
    this.szMethodName = szMethodName;
  }

  public ByteBuffer getMethodName() {
    return methodName;
  }

  public void setMethodName(ByteBuffer methodName) {
    this.methodName = methodName;
  }

  public int getSzParameter() {
    return szParameter;
  }

  public void setSzParameter(int szParameter) {
    this.szParameter = szParameter;
  }

  public ByteBuffer getParameter() {
    return parameter;
  }

  public void setParameter(ByteBuffer parameter) {
    this.parameter = parameter;
  }

  public int getSzRet() {
    return szRet;
  }

  public void setSzRet(int szRet) {
    this.szRet = szRet;
  }

  public ByteBuffer getRet() {
    return ret;
  }

  public void setRet(ByteBuffer ret) {
    this.ret = ret;
  }

  public int getSzError() {
    return szError;
  }

  public void setSzError(int szError) {
    this.szError = szError;
  }

  public ByteBuffer getError() {
    return error;
  }

  public void setError(ByteBuffer error) {
    this.error = error;
  }

}
