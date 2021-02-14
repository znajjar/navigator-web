package com.world.navigator.model;

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
