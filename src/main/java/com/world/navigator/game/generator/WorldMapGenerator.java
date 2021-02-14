package com.world.navigator.game.generator;

import com.world.navigator.game.Direction;
import com.world.navigator.game.MapBuilder;
import com.world.navigator.game.WorldMap;
import com.world.navigator.game.playeritems.Key;

import java.util.ArrayList;
import java.util.Collections;

public class WorldMapGenerator {
  private static final int MIN_ROOMS_COUNT = 5;
  private final MapBuilder builder;
  private final GameRandomizer gameRandomizer;
  private EntityGenerator entityGenerator;
  private int[][] roomsIds;
  private int mapDimension;

  public WorldMapGenerator(DifficultyLevel difficulty) {
    builder = new MapBuilder();
    gameRandomizer = new GameRandomizer(difficulty);
  }

  public WorldMap generateMap(int requiredRoomCount) {
    requiredRoomCount = Math.max(requiredRoomCount, MIN_ROOMS_COUNT);
    entityGenerator = new EntityGenerator(gameRandomizer, requiredRoomCount);
    initializeRooms();
    generateDoors();
    fillWalls();
    setLimits();

    return builder.getMap();
  }

  private void initializeRooms() {
    mapDimension = entityGenerator.getMapDimension();
    ArrayList<Integer> winningRoomsIds = entityGenerator.getWinningRoomsIds();
    roomsIds = new int[mapDimension][mapDimension];
    Collections.sort(winningRoomsIds);
    int roomIdCounter = 0;
    int winningRoomIdIndex = 0;
    for (int x = 0; x < mapDimension; x++) {
      for (int y = 0; y < mapDimension; y++) {
        int roomId = roomIdCounter++;
        int nextWinningRoomId =
            winningRoomsIds.get(Math.min(winningRoomsIds.size() - 1, winningRoomIdIndex));
        roomsIds[x][y] = roomId;
        boolean isWinningRoom = roomId == nextWinningRoomId;
        if (isWinningRoom) {
          builder.addWinningRoom(roomId);
          winningRoomIdIndex++;
        } else {
          addSpawnableRoom(roomId);
        }
      }
    }
  }

  private void addSpawnableRoom(int roomId) {
    if (gameRandomizer.isNextRoomWithLightSwitch()) {
      builder.addRoomWithLightSwitch(roomId, gameRandomizer.isNextRoomLit());
    } else {
      builder.addRoom(roomId);
    }
  }

  private void generateDoors() {
    for (int x = 0; x < mapDimension; x++) {
      for (int y = 0; y < mapDimension; y++) {
        attemptAddDoors(x, y);
      }
    }
  }

  private void attemptAddDoors(int x, int y) {
    boolean isTopRow = x == 0;
    if (!isTopRow) {
      attemptAddNorthDoor(x, y);
    }

    boolean isLeftMostColumn = y == 0;
    if (!isLeftMostColumn) {
      attemptAddWestDoor(x, y);
    }
  }

  private void attemptAddNorthDoor(int x, int y) {
    int currentRoomId = roomsIds[x][y];
    if (gameRandomizer.isNextDoor()) {
      int neighbouringRoomId = getNorthRoomId(x, y);
      DoorPairDetails doorPair = gameRandomizer.nextDoorPair();
      Key key = doorPair.getKey();
      boolean isLocked = doorPair.isLocked();
      builder.addNorthDoor(currentRoomId, neighbouringRoomId, key, isLocked);
    }
  }

  private int getNorthRoomId(int x, int y) {
    return roomsIds[x - 1][y];
  }

  private void attemptAddWestDoor(int x, int y) {
    int currentRoomId = roomsIds[x][y];
    if (gameRandomizer.isNextDoor()) {
      int neighbouringRoomId = getWestRoomId(x, y);
      DoorPairDetails doorPair = gameRandomizer.nextDoorPair();
      Key key = doorPair.getKey();
      boolean isLocked = doorPair.isLocked();
      builder.addWestDoor(currentRoomId, neighbouringRoomId, key, isLocked);
    }
  }

  private int getWestRoomId(int x, int y) {
    return roomsIds[x][y - 1];
  }

  private void fillWalls() {
    for (int x = 0; x < mapDimension; x++) {
      for (int y = 0; y < mapDimension; y++) {
        fillRoomWalls(roomsIds[x][y]);
      }
    }
  }

  private void fillRoomWalls(int roomId) {
    for (Direction direction : Direction.values()) {
      if (!builder.hasDoorInDirection(roomId, direction)) {
        builder.setRoomSide(roomId, direction, entityGenerator.getRandomObservable());
      }
    }
  }

  private void setLimits() {
    builder.setTimeLimit(gameRandomizer.getTimeLimitInMilliseconds());
    builder.setStartingGoldCount(gameRandomizer.getStartingGoldCount());
  }
}
