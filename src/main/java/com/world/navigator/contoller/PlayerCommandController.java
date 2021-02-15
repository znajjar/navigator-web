package com.world.navigator.contoller;

import com.world.navigator.entity.Command;
import com.world.navigator.security.AuthUser;
import com.world.navigator.service.PlayerService;
import com.world.navigator.util.UserEventListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.annotation.PostConstruct;

@Log4j2
@RestController
public class PlayerCommandController implements UserEventListener {

  private final SimpMessagingTemplate webSocket;
  private final PlayerService playerService;

  public PlayerCommandController(SimpMessagingTemplate webSocket, PlayerService playerService) {
    this.webSocket = webSocket;
    this.playerService = playerService;
  }

  @PostConstruct
  private void init() {
    playerService.registerListener(this);
  }

  @MessageMapping("/request/command")
  public void executeCommand(SimpMessageHeaderAccessor accessor, @Payload Command command) {
    AuthUser user = (AuthUser) accessor.getUser();
    playerService.execute(user, command);
  }

  @Override
  public void onEvent(String userName, String event) {
    webSocket.convertAndSendToUser(userName, "/queue/event/player", event);
  }

  @EventListener
  public void onDisconnectEvent(SessionDisconnectEvent event) {
    SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
    AuthUser user = (AuthUser) accessor.getUser();
    if (user != null) {
      playerService.removePlayer(user);
      log.info("{} disconnected", user);
    }
  }
}
