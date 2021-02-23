package com.world.navigator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class NavigatorApplication {
  public static void main(String[] args) {
    SpringApplication.run(NavigatorApplication.class, args);
  }
}
