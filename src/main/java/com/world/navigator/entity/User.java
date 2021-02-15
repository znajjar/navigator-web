package com.world.navigator.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.Principal;

@Getter
@Document("users")
public class User implements Principal {
  @Id String id;
  private final String name;
  private final String password;

  public User(String name, String password) {
    this.name = name;
    this.password = password;
  }
}
