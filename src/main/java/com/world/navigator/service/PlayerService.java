package com.world.navigator.service;

import com.world.navigator.entity.Command;
import com.world.navigator.game.player.PlayerController;
import com.world.navigator.game.player.PlayerEvent;
import com.world.navigator.game.player.PlayerEventListener;
import com.world.navigator.game.player.PlayerResponse;
import com.world.navigator.security.AuthUser;
import com.world.navigator.util.PlayerCommandSet;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerService implements PlayerEventListener {
  private static final PlayerCommandSet COMMAND_SET = PlayerCommandSet.getInstance();
  private final ConcurrentHashMap<AuthUser, PlayerController> players;
  private final SimpMessagingTemplate webSocket;

  public PlayerService(SimpMessagingTemplate webSocket) {
    this.webSocket = webSocket;
    players = new ConcurrentHashMap<>();
  }

  public void addPlayer(AuthUser user, PlayerController player) {
    players.put(user, player);
    player.addListener(this);
  }

  public void removePlayer(AuthUser user) {
    if (user != null) {
      players.remove(user);
    }
  }

  public void execute(AuthUser user, Command command) {
    PlayerController player = players.get(user);
    if (player == null) {
      return;
    }

    player.queueCommand(COMMAND_SET.getCommand(command.getName()), command.getArgs());
  }

  @Override
  public void onResponse(String playerName, PlayerResponse response) {
    notifyUser(playerName, response.toString());
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
    notifyUser(playerName, event.toString());
  }

  private void notifyUser(String username, String message) {
    webSocket.convertAndSendToUser(username, "/queue/event/player", message);
  }

  public boolean isUserInGame(AuthUser user) {
    return players.containsKey(user);
  }
}
