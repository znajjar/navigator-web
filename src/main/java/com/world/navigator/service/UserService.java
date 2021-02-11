package com.world.navigator.service;

import com.world.navigator.model.User;
import com.world.navigator.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  UserRepository userRepository;

  public UserService() {
    userRepository = new UserRepository();
  }

  public void addUser(User user) {
    userRepository.addUser(user);
  }

  public boolean isNameTaken(String name) {
    return userRepository.hasUser(name);
  }

  public User getUserByName(String name) {
    return userRepository.findByName(name);
  }
}
