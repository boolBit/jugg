package com.lorabit.security;

import com.google.common.collect.Lists;

import com.lorabit.security.LoginDetail.Authorities;
import com.lorabit.security.LoginDetail.CurrentUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author com.lorabit
 * @since 16-2-15
 */
public class AuthService implements AuthenticationUserDetailsService {


  @Override
  public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
    CurrentUser user = (CurrentUser) token.getPrincipal();
    if (!user.isValid()) {
      token.setAuthenticated(false);
    } else {
      List<GrantedAuthority> lst = Lists.newArrayList();
      lst.add(new Authorities.Common());
      user.setGrantedAuthorities(lst);
//            du = userRepo.expandLoginedUser(du);
      token.setAuthenticated(true);
    }
    return user;
  }
}
