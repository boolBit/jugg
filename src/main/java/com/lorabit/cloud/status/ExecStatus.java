package com.lorabit.cloud.status;

/**
 * @author lorabit
 * @since 16-2-18
 */
public enum ExecStatus {
  UNKNOWN(0),
  RUNNING(1),
  EXCEPTION(2),
  COMPLETED(3),
  TIMEOUT(4);

  public final int status;
  private ExecStatus(int s) {
    this.status = s;
  }

  public static ExecStatus of (String s) {
    for (ExecStatus es : ExecStatus.values()) {
      if (es.name().equalsIgnoreCase(s)) {
        return es;
      }
    }
    return UNKNOWN;
  }

  public static ExecStatus of (int s) {
    for (ExecStatus es : ExecStatus.values()) {
      if (es.status == s) {
        return es;
      }
    }
    return UNKNOWN;
  }
}
