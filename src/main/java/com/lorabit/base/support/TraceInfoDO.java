package com.lorabit.base.support;

/**
 * @author lorabit
 * @since 16-2-21
 */
public class TraceInfoDO {
  private String token;
  private String target;
  private int depth;
  private int seq;
  private long startTime;
  private long endTime;
  private long elapsedTime;
  private int parent;
  private int self;

  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }
  public String getTarget() {
    return target;
  }
  public void setTarget(String target) {
    this.target = target;
  }
  public int getDepth() {
    return depth;
  }
  public void setDepth(int depth) {
    this.depth = depth;
  }
  public int getSeq() {
    return seq;
  }
  public void setSeq(int seq) {
    this.seq = seq;
  }
  public long getStartTime() {
    return startTime;
  }
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
  public long getEndTime() {
    return endTime;
  }
  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }
  public long getElapsedTime() {
    return elapsedTime;
  }
  public void setElapsedTime(long elapsedTime) {
    this.elapsedTime = elapsedTime;
  }
  public int getParent() {
    return parent;
  }
  public void setParent(int parent) {
    this.parent = parent;
  }
  public int getSelf() {
    return self;
  }
  public void setSelf(int self) {
    this.self = self;
  }

}
