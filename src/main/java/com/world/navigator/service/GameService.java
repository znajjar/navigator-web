package com.world.navigator.service;

import com.world.navigator.game.Game;
import com.world.navigator.game.GameEventListener;
import com.world.navigator.game.player.PlayerController;
import com.world.navigator.security.AuthUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Log4j2
@Service
public class GameService implements GameEventListener {
  private final ConcurrentHashMap<String, Game> games;
  private final PlayerService playerService;
  private final TaskScheduler taskScheduler;

  public GameService(PlayerService playerService, TaskScheduler taskScheduler) {
    this.playerService = playerService;
    this.taskScheduler = taskScheduler;
    games = new ConcurrentHashMap<>();
  }

  public boolean canCreateGame(AuthUser user) {
    return !playerService.isUserInGame(user);
  }

  public String newGame(AuthUser user) {
    Game game = Game.createDefaultDifficultyLevelGame(user.getName(), taskScheduler);
    games.put(game.getId(), game);
    PlayerController player = game.nextPlayer(user.getName());
    playerService.addPlayer(user, player);
    String gameId = game.getId();
    log.info("{} created {}", user.getName(), gameId);
    game.addListener(this);
    return gameId;
  }

  public boolean canAddUserToGame(AuthUser user, String gameId) {
    Game game = games.get(gameId);
    if (game == null) {
      return false;
    }

    return !playerService.isUserInGame(user);
  }

  public void addUserToGame(AuthUser user, String gameId) {
    Game game = games.get(gameId);
    if (game == null) {
      return;
    }

    if (playerService.isUserInGame(user)) {
      return;
    }

    PlayerController player = game.nextPlayer(user.getName());
    playerService.addPlayer(user, player);
    log.info("{} joined {}", user.getName(), gameId);
  }

  public boolean canStartGame(AuthUser user, String gameId) {
    Game game = games.get(gameId);
    if (game == null) {
      return false;
    }
    String gameHost = game.getHostName();
    return gameHost.equals(user.getName());
  }

  public void startGame(AuthUser user, @Payload String gameId) {
    Game game = games.get(gameId);
    if (game == null) {
      return;
    }

    String gameHost = game.getHostName();
    if (gameHost.equals(user.getName())) {
      log.info("{} started game {}", user.getName(), gameId);
      game.start();
    }
  }

  public List<String> listJoinableGames() {
    return games.values().stream()
            .filter((Game::isReady))
            .map(Game::getId)
            .collect(Collectors.toList());
  }

  @Override
  public void onGameEnd(String gameId) {
    games.remove(gameId);
  }
}
