package com.world.navigator.game;

import com.world.navigator.game.exceptions.ItemIsLockedException;
import com.world.navigator.game.generator.DefaultDifficultyLevel;
import com.world.navigator.game.generator.DifficultyLevel;
import com.world.navigator.game.generator.WorldMapGenerator;
import com.world.navigator.game.mapitems.PassThrough;
import com.world.navigator.game.mapitems.Room;
import com.world.navigator.game.mapitems.WinningRoom;
import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerController;
import com.world.navigator.game.playeritems.GoldBag;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Log4j2
public class Game {
  private final ConcurrentHashMap<Integer, Player> players;
  //  private final NotificationsManager notificationsManager;
  private final ScheduledExecutorService executorService;
  private final WorldMapGenerator worldMapGenerator;
  private final String hostName;
  private WorldMap worldMap;
  private GameState currentState;
  private String id;
  private int playerIdIndex;

  private Game(DifficultyLevel difficultyLevel, String hostName) {
    this.hostName = hostName;
    currentState = GameState.LOADING;
    players = new ConcurrentHashMap<>();
    //    notificationsManager = new NotificationsManager();
    worldMapGenerator = new WorldMapGenerator(difficultyLevel);
    setState(GameState.READY);
    executorService = new ScheduledThreadPoolExecutor(10);
    id = UUID.randomUUID().toString();
  }

  public static Game createDefaultDifficultyLevelGame(String hostName) {
    return new Game(new DefaultDifficultyLevel(), hostName);
  }

  public PlayerController nextPlayer(String name) {
//    int startingGoldCount = worldMap.getStartingGoldCount();
    int playerId = playerIdIndex++;
    Player nextPlayer =
        new Player(playerId, name) {
          @Override
          public Room requestMove(PassThrough passThrough) throws ItemIsLockedException {
            return movePlayerThrough(this, passThrough);
          }
        };

    PlayerController controller = new PlayerController(nextPlayer);
    executorService.scheduleWithFixedDelay(controller, 0, 100, TimeUnit.MILLISECONDS);
    players.put(nextPlayer.getID(), nextPlayer);
    //    new Thread(controller).start();
    return controller;
  }

  private Room movePlayerThrough(Player player, PassThrough passThrough) {
    Room nextRoom = worldMap.getRoomById(passThrough.getNextRoomID());
    if (nextRoom instanceof WinningRoom) {
      player.winGame();
      log.info("player {} won the game", player.getID());
      for (Player player1 : players.values()) {
        if (!player.equals(player1)) {
          player1.loseGame();
          log.info("player {} lost the game", player1.getID());
        }
      }
    }
    return nextRoom;
  }

  public void start() {
    checkState(GameState.READY);
    worldMap = worldMapGenerator.generateMap(players.size());
    for (Player player : players.values()) {
      player.moveTo(worldMap.nextSpawnableRoom());
      player.takeGold(new GoldBag(worldMap.getStartingGoldCount()));
      player.startNavigating();
    }
    setState(GameState.IN_GAME);
    //    notificationsManager.startTimer();
  }

  private void checkState(GameState... states) {
    boolean found = false;
    for (GameState state : states) {
      if (currentState.equals(state)) {
        found = true;
        break;
      }
    }

    if (!found) {
      throw new IllegalStateException();
    }
  }

  private void setState(GameState state) {
    currentState = state;
  }

  public String  getId() {
    return id;
  }

  public String getHostName() {
    return hostName;
  }

  public GameState getCurrentState() {
    return currentState;
  }
}
