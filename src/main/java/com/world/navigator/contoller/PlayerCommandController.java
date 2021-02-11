package com.world.navigator.contoller;

import com.world.navigator.model.Command;
import com.world.navigator.model.User;
import com.world.navigator.service.PlayerService;
import com.world.navigator.util.UserEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class PlayerCommandController implements UserEventListener {

  @Autowired SimpMessagingTemplate webSocket;
  @Autowired PlayerService playerService;

  @PostConstruct
  private void init() {
    playerService.registerListener(this);
  }

  @MessageMapping("/request/command")
  public void executeCommand(SimpMessageHeaderAccessor accessor, @Payload Command command) {
    User user = (User) accessor.getUser();
    playerService.execute(user, command);
  }

  @Override
  public void onEvent(String userName, String event) {
    webSocket.convertAndSendToUser(userName, "/queue/event/player", event);
  }
}
