package com.world.navigator.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;

public class AuthUser extends UsernamePasswordAuthenticationToken {
  public AuthUser(String username) {
    super(username, null, Collections.singleton((GrantedAuthority) () -> "USER"));
  }
}
