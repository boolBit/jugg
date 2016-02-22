package com.lorabit.base.support;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.security.SecureRandom;

/**
 * @author lorabit
 * @since 16-2-21
 */
public class CCT {

  private static Logger cctlog = Logger.getLogger("cct");

  private static ThreadLocal<TraceChainDO> chainHolder = new ThreadLocal<>();

  private static ObjectMapper mapper = new ObjectMapper();
  private static final SecureRandom numberGenerator = new SecureRandom();

  private static long baseline = 0L;

  public static TraceChainDO get() {
    TraceChainDO tc = chainHolder.get();
    return tc;
  }

  public static void setForcibly(TraceChainDO tc) {
    chainHolder.set(tc);
  }

  public static void mergeTraceChain(TraceChainDO remoteTc) {
    TraceChainDO tc = get();
    if (tc != null && remoteTc != null) {
      tc.seqMap().putAll(remoteTc.seqMap());
    }
  }

  public static void call(String target, boolean isRoot) {
    TraceChainDO tc = chainHolder.get();
    if (tc == null && isRoot == true) {
      tc = new TraceChainDO(generateToken());
      chainHolder.set(tc);
    }

    if (tc != null) {
      tc.push(target, currentTimeMillis(tc));
    }
  }

  public static void ret() {
    boolean islast = false;
    try {
      TraceChainDO tc = chainHolder.get();
      if (tc == null) return;

      int depth = tc.depth();
      Pair<String, Long> p = tc.pop();
      int parent = tc.getParent();
      long curr = currentTimeMillis(tc);
      long last = p.getRight();

      traceLog(
          p.getLeft(),
          p.getRight(),
          currentTimeMillis(tc),
          depth,
          parent
      );

      if (tc.isEmptyStack()) {
        islast = true;
        if (curr - last < baseline) return;
        for (String s : tc.getBuff()) {
          cctlog.warn(s);
        }
      }
    } finally {
      if (islast) chainHolder.remove();
    }
  }

  public static void traceLog(String target, long start, long end, int depth, int parent) {
    TraceInfoDO ti = new TraceInfoDO();
    TraceChainDO tt = get();
    ti.setToken(tt.getToken());
    ti.setDepth(depth + tt.getBaseDepth());
    ti.setElapsedTime(end - start);
    ti.setStartTime(start);
    ti.setEndTime(end);
    ti.setSeq(tt.seqMap().get(depth + tt.getBaseDepth()));
    ti.setTarget(target);
    ti.setParent(parent);
    ti.setSelf(Math.abs(target.hashCode()));
    try {
      tt.addTraceInfo(mapper.writeValueAsString(ti));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static long currentTimeMillis(TraceChainDO tc) {
    if (tc != null) return System.currentTimeMillis() + tc.getTimedelta();
    else return System.currentTimeMillis();
  }

  private static String generateToken() {
    String t = Long.toString(System.currentTimeMillis(), 36);
    return String.format("%s%04d", t, numberGenerator.nextInt(9999));
  }

  //spring专用
  public void setBaseline(long v) {
    CCT.baseline = v;
  }
}

