package com.world.navigator.contoller;

import com.world.navigator.entity.SignupRequest;
import com.world.navigator.entity.User;
import com.world.navigator.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@Controller
public class WebController {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public WebController(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/")
  public String getDefault() {
    return "redirect:world-navigator";
  }

  @GetMapping("/world-navigator")
  public String getWorldNavigator() {
    return "world-navigator";
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signupUser(SignupRequest signUpRequest) {
    String username = signUpRequest.getUsername();
    String password = signUpRequest.getPassword();

    if (!validCredentials(username, password)) {
      return ResponseEntity.ok("Invalid Credentials!");
    }

    if (userService.isNameTaken(signUpRequest.getUsername())) {
      return ResponseEntity.ok("Username is already taken!");
    }

    String encodedPassword = passwordEncoder.encode(password);

    User user = new User(username, encodedPassword);
    userService.addUser(user);
    return ResponseEntity.ok("Signed up successfully!");
  }

  private boolean validCredentials(String username, String password) {
    if (username == null || username.isEmpty()) {
      return false;
    }

    return password != null && !password.isEmpty();
  }
}
