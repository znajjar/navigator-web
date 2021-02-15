package com.world.navigator.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.repository.NoRepositoryBean;

@Getter
@NoRepositoryBean
@AllArgsConstructor
public class GameJoinEvent {
  String gameId;
  boolean isHost;
}
