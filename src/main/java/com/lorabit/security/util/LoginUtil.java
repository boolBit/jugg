package com.lorabit.security.util;

import com.lorabit.security.ISessionManager;
import com.lorabit.security.LoginDetail.CurrentUser;
import com.lorabit.security.LoginDetail.SessionId;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author com.lorabit
 * @since 16-2-15
 */
public class LoginUtil {
  public static long getUserId() {
    CurrentUser user = getCurrentUser();
    if (user == null || user.getUserInfo() == null) {
      return 0;
    }
    return user.getUserInfo().getUserId();
  }

  public static CurrentUser getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return (CurrentUser) auth.getPrincipal();
  }

  public static SessionId getSessionId(HttpServletRequest request) {
    CurrentUser user = getCurrentUser();
    if (user != null && user.getSessionId() != null) {
      return user.getSessionId();
    }
    if (request.getAttribute(ISessionManager.SESSIONID) != null) {
      return (SessionId) request.getAttribute("session_id");
    }
    return null;
  }
}
