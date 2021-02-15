package com.world.navigator.game;

import com.world.navigator.game.entities.PassThrough;
import com.world.navigator.game.entities.Room;
import com.world.navigator.game.entities.WinningRoom;
import com.world.navigator.game.exceptions.ItemIsLockedException;
import com.world.navigator.game.fighting.FightsTracker;
import com.world.navigator.game.generator.DefaultDifficultyLevel;
import com.world.navigator.game.generator.DifficultyLevel;
import com.world.navigator.game.generator.WorldMapGenerator;
import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerController;
import com.world.navigator.game.playeritems.GoldBag;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

@Log4j2
public class Game {
  private static final int PLAYER_CONTROLLER_DELAY = 100;
  private final CopyOnWriteArrayList<GameEventListener> listeners;
  private final ConcurrentHashMap<Integer, Player> players;
  private final WorldMapGenerator worldMapGenerator;
  private final String hostName;
  private final TaskScheduler taskScheduler;
  private final String id;
  private final FightsTracker fightsTracker;
  private WorldMap worldMap;
  private GameState currentState;
  private int playerIdIndex;
  private ScheduledFuture<?> timerTask;

  private Game(DifficultyLevel difficultyLevel, String hostName, TaskScheduler taskScheduler) {
    this.hostName = hostName;
    this.taskScheduler = taskScheduler;
    players = new ConcurrentHashMap<>();
    worldMapGenerator = new WorldMapGenerator(difficultyLevel);
    setState(GameState.READY);
    id = UUID.randomUUID().toString();
    fightsTracker = new FightsTracker();
    listeners = new CopyOnWriteArrayList<>();
  }

  public static Game createDefaultDifficultyLevelGame(
      String hostName, TaskScheduler taskScheduler) {
    return new Game(new DefaultDifficultyLevel(), hostName, taskScheduler);
  }

  public PlayerController nextPlayer(String name) {
    int playerId = nextPlayerId();
    Player nextPlayer =
        new Player(playerId, name) {
          @Override
          public Room requestMove(PassThrough passThrough) throws ItemIsLockedException {
            return movePlayerThrough(this, passThrough);
          }
        };

    players.put(playerId, nextPlayer);
    PlayerController controller = new PlayerController(nextPlayer);
    taskScheduler.scheduleWithFixedDelay(controller, PLAYER_CONTROLLER_DELAY);
    return controller;
  }

  private synchronized int nextPlayerId() {
    return playerIdIndex++;
  }

  private Room movePlayerThrough(Player player, PassThrough passThrough) {
    Room nextRoom = worldMap.getRoomById(passThrough.getNextRoomID());
    nextRoom =
        fightsTracker.movePlayerBetween(player, player.navigate().getCurrentRoom(), nextRoom);
    if (nextRoom instanceof WinningRoom) {
      playerWon(player);
    }
    return nextRoom;
  }

  private void playerWon(Player winningPlayer) {
    if (checkState(GameState.IN_GAME)) {
      setState(GameState.ENDED);
      timerTask.cancel(false);
      winningPlayer.winGame();
      log.info("player {} won the {} game", winningPlayer.getID(), id);
      allPlayersLostExcept(winningPlayer);
      onGameEnd();
    }
  }

  public void start() {
    if (checkState(GameState.READY)) {
      setState(GameState.IN_GAME);
      worldMap = worldMapGenerator.generateMap(players.size());
      for (Player player : players.values()) {
        Room nextRoom = worldMap.nextSpawnableRoom();
        player.navigate().moveTo(nextRoom);
        player.loot().takeItem(new GoldBag(worldMap.getStartingGoldCount()));
        player.startNavigating();
        fightsTracker.movePlayerTo(player, nextRoom);
      }
      setTimer();
    }
  }

  private void setTimer() {
    Date finishDate = new Date(Calendar.getInstance().getTimeInMillis() + worldMap.getTimeLimit());
    timerTask = taskScheduler.schedule(this::timeUpCallback, finishDate);
  }

  private void timeUpCallback() {
    if (checkState(GameState.IN_GAME)) {
      setState(GameState.ENDED);
      allPlayersLostExcept(null);
      log.info("time went up on game {}", id);
      onGameEnd();
    }
  }

  private void allPlayersLostExcept(Player exceptPlayer) {
    players.values().stream()
        .filter(player -> !player.equals(exceptPlayer))
        .forEach(Player::loseGame);
  }

  private synchronized boolean checkState(GameState... states) {
    for (GameState state : states) {
      if (currentState.equals(state)) {
        return true;
      }
    }

    return false;
  }

  private synchronized void setState(GameState state) {
    currentState = state;
  }

  public String getId() {
    return id;
  }

  public String getHostName() {
    return hostName;
  }

  public boolean isReady() {
    return checkState(GameState.READY);
  }

  public void addListener(GameEventListener listener) {
    listeners.add(listener);
  }

  public void onGameEnd() {
    listeners.forEach((gameEventListener) -> gameEventListener.onGameEnd(id));
    listeners.clear();
    players.clear();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Game game = (Game) o;
    return Objects.equals(id, game.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
