package com.world.navigator.game.fighting;

import com.world.navigator.game.exceptions.PlayerIsInAFightException;
import com.world.navigator.game.mapitems.Room;
import com.world.navigator.game.player.Player;

import java.util.concurrent.ConcurrentHashMap;

public class FightsTracker {
  private final ConcurrentHashMap<Room, Player> latestPlayerToEnterRoom;

  public FightsTracker() {
    latestPlayerToEnterRoom = new ConcurrentHashMap<>();
  }

  public void movePlayerTo(Player player, Room nextRoom) {
    checkForFightInRoom(player, nextRoom);
  }

  public Room movePlayerBetween(Player player, Room currentRoom, Room nextRoom) {
    // in case player gets into a fight while trying to move out of room
    // if false it means the player is in a fight and shouldn't move out of room
    boolean isSuccessful = latestPlayerToEnterRoom.remove(currentRoom, player);
    if (isSuccessful) {
      checkForFightInRoom(player, nextRoom);
      return nextRoom;
    } else {
      return currentRoom;
    }
  }

  private void checkForFightInRoom(Player movingPlayer, Room nextRoom) {
    Player playerInRoom = latestPlayerToEnterRoom.put(nextRoom, movingPlayer);

    if (playerInRoom == null) {
      return;
    }

    createFightBetween(movingPlayer, playerInRoom);
  }

  private void createFightBetween(Player movingPlayer, Player playerInRoom) {
    Fight newFight = new Fight();

    newFight.setTieBreaker(new RockPaperScissorsTieBreaker());

    Fight checkFight = movingPlayer.fight().setCurrentFightIfAbsent(newFight);
    if (checkFight != newFight) {
      throw new PlayerIsInAFightException();
    }
    newFight.setFirstPlayer(movingPlayer);

    Fight secondPlayerCurrentFight = playerInRoom.fight().setCurrentFightIfAbsent(newFight);
    if (secondPlayerCurrentFight == newFight) {
      newFight.setSecondPlayer(playerInRoom);
    } else {
      newFight.secondPlayerIsWinnerOf(secondPlayerCurrentFight);
    }
  }
}
