package com.world.navigator.service;

import com.world.navigator.model.User;
import com.world.navigator.game.Game;
import com.world.navigator.game.player.PlayerController;
import com.world.navigator.repository.GameRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Log4j2
@Service
public class GameService {

  @Autowired PlayerService playerService;

  GameRepository gameRepository;

  public GameService() {
    this.gameRepository = new GameRepository();
  }

  public boolean canCreateGame(User user) {
    return !playerService.isUserInGame(user);
  }

  public String newGame(User user) {
    Game game = Game.createDefaultDifficultyLevelGame(user.getName());
    gameRepository.addGame(game);
    PlayerController player = game.nextPlayer(user.getName());
    playerService.addPlayer(user, player);
    return game.getId();
  }

  public boolean canAddUserToGame(User user, String gameId) {
    Game game = gameRepository.findGameById(gameId);
    if (game == null) {
      return false;
    }

    return !playerService.isUserInGame(user);
  }

  public void addUserToGame(User user, String gameId) {
    Game game = gameRepository.findGameById(gameId);
    if (game == null) {
      return;
    }

    if (playerService.isUserInGame(user)) {
      return;
    }

    PlayerController player = game.nextPlayer(user.getName());
    playerService.addPlayer(user, player);
  }

  public boolean canStartGame(User user, String gameId) {
    Game game = gameRepository.findGameById(gameId);
    if (game == null) {
      return false;
    }
    String gameHost = game.getHostName();
    if (gameHost.equals(user.getName())) {
      return true;
    } else {
      return false;
    }
  }

  public void startGame(User user, @Payload String gameId) {
    Game game = gameRepository.findGameById(gameId);
    if (game == null) {
      return;
    }

    String gameHost = game.getHostName();
    log.info("{} is trying to start game {} with hostname {}", user.getName(), gameId, gameHost);
    if (gameHost.equals(user.getName())) {
      game.start();
    }
  }

  public ArrayList<String> listJoinableGames() {
    var joinableGames = gameRepository.getJoinableGames();
    log.info("joinable games" + joinableGames.toString());
    return joinableGames;
  }
}
