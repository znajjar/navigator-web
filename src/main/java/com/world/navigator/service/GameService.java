package com.world.navigator.service;

import com.world.navigator.game.Game;
import com.world.navigator.game.GameEventListener;
import com.world.navigator.game.player.PlayerController;
import com.world.navigator.repository.GameRepository;
import com.world.navigator.security.AuthUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Log4j2
@Service
public class GameService implements GameEventListener {
  private final PlayerService playerService;
  private final GameRepository gameRepository;
  private final TaskScheduler taskScheduler;

  public GameService(PlayerService playerService, TaskScheduler taskScheduler) {
    this.playerService = playerService;
    this.taskScheduler = taskScheduler;
    this.gameRepository = new GameRepository();
  }

  public boolean canCreateGame(AuthUser user) {
    return !playerService.isUserInGame(user);
  }

  public String newGame(AuthUser user) {
    Game game = Game.createDefaultDifficultyLevelGame(user.getName(), taskScheduler);
    gameRepository.addGame(game);
    PlayerController player = game.nextPlayer(user.getName());
    playerService.addPlayer(user, player);
    String gameId = game.getId();
    log.info("{} created {}", user.getName(), gameId);
    game.addListener(this);
    return gameId;
  }

  public boolean canAddUserToGame(AuthUser user, String gameId) {
    Game game = gameRepository.findGameById(gameId);
    if (game == null) {
      return false;
    }

    return !playerService.isUserInGame(user);
  }

  public void addUserToGame(AuthUser user, String gameId) {
    Game game = gameRepository.findGameById(gameId);
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
    Game game = gameRepository.findGameById(gameId);
    if (game == null) {
      return false;
    }
    String gameHost = game.getHostName();
    return gameHost.equals(user.getName());
  }

  public void startGame(AuthUser user, @Payload String gameId) {
    Game game = gameRepository.findGameById(gameId);
    if (game == null) {
      return;
    }

    String gameHost = game.getHostName();
    if (gameHost.equals(user.getName())) {
      log.info("{} started game {}", user.getName(), gameId);
      game.start();
    }
  }

  public ArrayList<String> listJoinableGames() {
    return gameRepository.getJoinableGames();
  }

  @Override
  public void onGameEnd(String gameId) {
    gameRepository.removeGame(gameId);
  }
}
