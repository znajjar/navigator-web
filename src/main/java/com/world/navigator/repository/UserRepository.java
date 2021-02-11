package com.world.navigator.repository;

import com.world.navigator.model.User;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {
  ConcurrentHashMap<String, User> users;

  public UserRepository() {
    users = new ConcurrentHashMap<>();
  }

  public User findByName(String name) {
    return users.get(name);
  }

  public boolean hasUser(String name) {
    return users.containsKey(name);
  }

  public void addUser(User user) {
    users.put(user.getName(), user);
  }
}
