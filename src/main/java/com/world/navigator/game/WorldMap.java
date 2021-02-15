package com.world.navigator.game;

import com.world.navigator.game.entities.Room;
import com.world.navigator.game.entities.WinningRoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class WorldMap {
  private final ConcurrentHashMap<Integer, Room> rooms;
  private final long timeLimit;
  private final ArrayList<Integer> spawnableRoomsIds;
  private final int startingGoldCount;
  private int spawnableRoomIndex;

  protected WorldMap(HashMap<Integer, Room> rooms, long timeLimit, int startingGoldCount) {
    this.timeLimit = timeLimit;
    this.rooms = new ConcurrentHashMap<>(rooms);
    this.startingGoldCount = startingGoldCount;
    spawnableRoomsIds = new ArrayList<>();
    findSpawnableRoomsIds();
    Collections.shuffle(spawnableRoomsIds);
    spawnableRoomIndex = 0;
  }

  private void findSpawnableRoomsIds() {
    for (Room room : rooms.values()) {
      boolean isWinningRoom = room instanceof WinningRoom;
      if (!isWinningRoom) {
        spawnableRoomsIds.add(room.getID());
      }
    }
  }

  public long getTimeLimit() {
    return timeLimit;
  }

  public int getStartingGoldCount() {
    return startingGoldCount;
  }

  public Room getRoomById(int roomId) {
    return rooms.get(roomId);
  }

  public Room nextSpawnableRoom() {
    return getRoomById(spawnableRoomsIds.get(nextSpawnableRoomIndex()));
  }

  private synchronized int nextSpawnableRoomIndex() {
    return spawnableRoomIndex++;
  }
}
