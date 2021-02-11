package com.world.navigator.repository;

import com.world.navigator.game.Game;
import com.world.navigator.game.GameState;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class GameRepository {
  ConcurrentHashMap<String, Game> games = new ConcurrentHashMap<>();

  public Game findGameById(String gameId) {
    return games.get(gameId);
  }

  public void addGame(Game game) {
    games.put(game.getId(), game);
  }

  public ArrayList<String> getJoinableGames() {
    ArrayList<String> joinableGames = new ArrayList<>();
    for (Game game : games.values()) {
      if (game.getCurrentState().equals(GameState.READY)) {
        joinableGames.add(game.getId());
      }
    }
    return joinableGames;
  }
}
