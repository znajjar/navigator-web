package com.world.navigator.service;

import com.world.navigator.model.User;
import com.world.navigator.security.AuthUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class WebSocketAuthenticatorService {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public WebSocketAuthenticatorService(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  public AuthUser getAuthenticatedOrFail(final String username, final String password)
      throws AuthenticationException {
    if (username == null || username.trim().isEmpty()) {
      throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
    }
    if (password == null || password.trim().isEmpty()) {
      throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
    }
    if (matchCredentials(username, password)) {
      return new AuthUser(username);
    }

    throw new BadCredentialsException("Bad credentials");
  }

  private boolean matchCredentials(String username, String password) {
    User user = userService.getUserByName(username);

    if (user == null) {
      return false;
    }

    String correctEncodedPassword = user.getPassword();

    return passwordEncoder.matches(password, correctEncodedPassword);
  }
}
