package com.world.navigator.util;

import com.world.navigator.model.User;
import com.world.navigator.service.UserService;
import lombok.SneakyThrows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import javax.naming.NameAlreadyBoundException;
import java.util.List;
import java.util.Map;

public class UserInterceptor implements ChannelInterceptor {

  private final UserService userService;

  public UserInterceptor(UserService userService) {
    this.userService = userService;
  }

  @SneakyThrows
  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor =
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

      if (raw instanceof Map) {
        Object username = ((Map) raw).get("username");

        if (username instanceof List) {
          String requestedName = ((List) username).get(0).toString();

          if (userService.isNameTaken(requestedName)) {
            throw new NameAlreadyBoundException();
          }

          User user = new User(requestedName);
          userService.addUser(user);
          accessor.setUser(user);
        }
      }
    }
    return message;
  }
}
