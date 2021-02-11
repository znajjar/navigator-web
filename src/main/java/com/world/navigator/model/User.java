package com.world.navigator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Principal;

@Getter
@AllArgsConstructor
public class User implements Principal {
  String name;
}
