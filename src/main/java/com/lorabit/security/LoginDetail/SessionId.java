package com.lorabit.security.LoginDetail;

import java.util.UUID;

/**
 * @author com.lorabit
 * @since 16-2-15
 */
public class SessionId {
  private String sessionId;

  public SessionId(String s) {
    this.sessionId = s;
  }

  public static SessionId generate() {
    return new SessionId(UUID.randomUUID().toString());
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
}
