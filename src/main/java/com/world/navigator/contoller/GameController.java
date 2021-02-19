package com.world.navigator.contoller;

import com.world.navigator.entity.GameJoinEvent;
import com.world.navigator.security.AuthUser;
import com.world.navigator.service.GameService;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
public class GameController {

  private final GameService gameService;
  private final SimpMessagingTemplate webSocket;

  public GameController(GameService gameService, SimpMessagingTemplate webSocket) {
    this.gameService = gameService;
    this.webSocket = webSocket;
  }

  @MessageMapping("/request/game/join")
  public void joinGame(SimpMessageHeaderAccessor accessor, @Payload String gameId) {
    AuthUser user = (AuthUser) accessor.getUser();
    if (gameService.canAddUserToGame(user, gameId)) {
      gameService.addUserToGame(user, gameId);
      notifyUserAboutJoinEventAsPlayer(user, gameId);
    }
  }

  @MessageMapping("/request/game/start")
  public void startGame(SimpMessageHeaderAccessor accessor, @Payload String gameId) {
    log.info("starting game {}", gameId);
    AuthUser user = (AuthUser) accessor.getUser();
    if (gameService.canStartGame(user, gameId)) {
      gameService.startGame(user, gameId);
      notifyGameListUpdate();
    }
  }

  @MessageMapping("/request/game/list")
  public void listGames(SimpMessageHeaderAccessor accessor) {
    AuthUser user = (AuthUser) accessor.getUser();
    assert user != null;
    webSocket.convertAndSendToUser(
        user.getName(), "/queue/event/game/list", gameService.listJoinableGames());
  }

  @MessageMapping("/request/game/create")
  public void createGame(SimpMessageHeaderAccessor accessor) {
    AuthUser user = (AuthUser) accessor.getUser();
    if (gameService.canCreateGame(user)) {
      assert user != null;
      String newGameId = gameService.newGame(user);
      log.info("created new game: {}", newGameId);
      notifyUserAboutJoinEventAsHost(user, newGameId);
      notifyGameListUpdate();
    }
  }

  private void notifyUserAboutJoinEventAsHost(AuthUser user, String gameId) {
    GameJoinEvent event = new GameJoinEvent(gameId, true);
    notifyUserAboutJoinEvent(user, event);
  }

  private void notifyUserAboutJoinEventAsPlayer(AuthUser user, String gameId) {
    GameJoinEvent event = new GameJoinEvent(gameId, false);
    notifyUserAboutJoinEvent(user, event);
  }

  private void notifyUserAboutJoinEvent(AuthUser user, GameJoinEvent event) {
    webSocket.convertAndSendToUser(user.getName(), "/queue/event/game/joined", event);
  }

  private void notifyGameListUpdate() {
    List<String> joinableGames = gameService.listJoinableGames();
    webSocket.convertAndSend("/topic/games/update", joinableGames);
  }
}
