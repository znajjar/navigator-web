package com.world.navigator.service;

import com.world.navigator.model.Command;
import com.world.navigator.model.User;
import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerController;
import com.world.navigator.game.player.PlayerEvenListener;
import com.world.navigator.game.player.PlayerEvent;
import com.world.navigator.util.PlayerCommandSet;
import com.world.navigator.util.UserEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerService implements PlayerEvenListener {
  private static final int queueSize = 100;
  private static final PlayerCommandSet COMMAND_SET = PlayerCommandSet.getInstance();
  @Autowired UserService userService;
  ConcurrentHashMap<User, PlayerController> players;
  BlockingQueue<UserEventListener> listeners = new ArrayBlockingQueue<>(queueSize);

  public PlayerService() {
    players = new ConcurrentHashMap<>();
  }

  public void addPlayer(User user, PlayerController player) {
    players.put(user, player);
    player.addListener(this);
  }

  public void execute(User user, Command command) {
    PlayerController player = players.get(user);
    if (player == null) {
      return;
    }

    player.queueCommand(COMMAND_SET.getCommand(command.getName()), command.getArgs());
  }

  @Override
  public void onEvent(Player player, PlayerEvent event) {
    notifyListeners(player.getName(), event.toString());
  }

  public void registerListener(UserEventListener listener) {
    listeners.add(listener);
  }

  public void notifyListeners(String userName, String event) {
    for (UserEventListener listener : listeners) {
      listener.onEvent(userName, event);
    }
  }

  public boolean isUserInGame(User user) {
    return players.containsKey(user);
  }
}
