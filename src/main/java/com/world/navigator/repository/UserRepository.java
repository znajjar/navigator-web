package com.world.navigator.repository;

import com.world.navigator.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
  User findByName(String name);

  boolean existsByName(String name);
}
