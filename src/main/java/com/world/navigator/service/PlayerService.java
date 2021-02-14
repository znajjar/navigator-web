package com.world.navigator.service;

import com.world.navigator.game.player.PlayerController;
import com.world.navigator.game.player.PlayerEvent;
import com.world.navigator.game.player.PlayerEventListener;
import com.world.navigator.game.player.PlayerResponse;
import com.world.navigator.model.Command;
import com.world.navigator.security.AuthUser;
import com.world.navigator.util.PlayerCommandSet;
import com.world.navigator.util.UserEventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerService implements PlayerEventListener {
  private static final int QUEUE_SIZE = 100;
  private static final PlayerCommandSet COMMAND_SET = PlayerCommandSet.getInstance();
  private final ConcurrentHashMap<AuthUser, PlayerController> players;
  private final BlockingQueue<UserEventListener> listeners;

  public PlayerService() {
    players = new ConcurrentHashMap<>();
    listeners = new ArrayBlockingQueue<>(QUEUE_SIZE);
  }

  public void addPlayer(AuthUser user, PlayerController player) {
    players.put(user, player);
    player.addListener(this);
  }

  public void removePlayer(AuthUser user) {
    players.remove(user);
  }

  public void execute(AuthUser user, Command command) {
    PlayerController player = players.get(user);
    if (player == null) {
      return;
    }

    player.queueCommand(COMMAND_SET.getCommand(command.getName()), command.getArgs());
  }

  @Override
  public void onResponse(String playerName, PlayerResponse event) {
    notifyListeners(playerName, event.toString());
  }

  @Override
  public void onEvent(String playerName, PlayerEvent event) {
    if (event.getEventType().equals("gameEnd")) {
      players.forEach(
          ((authUser, playerController) -> {
            if (authUser.getName().equals(playerName)) {
              players.remove(authUser);
              playerController.removeListener(this);
            }
          }));
    }
    notifyListeners(playerName, event.toString());
  }

  public void registerListener(UserEventListener listener) {
    listeners.add(listener);
  }

  public void notifyListeners(String userName, String event) {
    for (UserEventListener listener : listeners) {
      listener.onEvent(userName, event);
    }
  }

  public boolean isUserInGame(AuthUser user) {
    return players.containsKey(user);
  }
}
