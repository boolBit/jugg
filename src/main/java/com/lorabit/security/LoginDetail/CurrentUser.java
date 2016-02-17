package com.lorabit.security.LoginDetail;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author com.lorabit
 * @since 16-2-15
 */
public class CurrentUser implements UserDetails {

  private List<GrantedAuthority> grantedAuthorities;
  private SessionId sessionId;
  private UserInfo userInfo;
  private boolean isValid;
  private boolean isApp;


  private int cookieAge = -1;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return grantedAuthorities;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return userInfo == null ? null : userInfo.getNickname();
  }

  @Override
  public boolean isAccountNonExpired() {
    return isValid;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isValid;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public List<GrantedAuthority> getGrantedAuthorities() {
    return grantedAuthorities;
  }

  public void setGrantedAuthorities(List<GrantedAuthority> grantedAuthorities) {
    this.grantedAuthorities = grantedAuthorities;
  }

  public SessionId getSessionId() {
    return sessionId;
  }

  public void setSessionId(SessionId sessionId) {
    this.sessionId = sessionId;
  }

  public UserInfo getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }

  public boolean isValid() {
    return isValid;
  }

  public void setIsValid(boolean isValid) {
    this.isValid = isValid;
  }

  public boolean isApp() {
    return isApp;
  }

  public void setIsApp(boolean isApp) {
    this.isApp = isApp;
  }

  public int getCookieAge() {
    return cookieAge;
  }

  public void setCookieAge(int cookieAge) {
    this.cookieAge = cookieAge;
  }
}
