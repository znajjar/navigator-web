package com.world.navigator.service;

import com.world.navigator.entity.User;
import com.world.navigator.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void addUser(User user) {
    userRepository.save(user);
  }

  public boolean isNameTaken(String name) {
    return userRepository.existsByName(name);
  }

  public User getUserByName(String name) {
    return userRepository.findByName(name);
  }
}
