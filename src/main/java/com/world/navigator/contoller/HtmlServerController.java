package com.world.navigator.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlServerController {
  @GetMapping("/world-navigator")
  public String getWebSocketWithSockJs() {
    return "world-navigator";
  }
}
