package com.lorabit.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author com.lorabit
 * @since 16-2-14
 */
public class LoginEntryPoint implements AuthenticationEntryPoint {
  private String loginUrl;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    try {
      if (request.getRequestURI().startsWith("/napi")) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"status\":\"2\"}");
        writer.flush();
        writer.close();
        return;
      } else {
        response.sendRedirect(loginUrl);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }
}
