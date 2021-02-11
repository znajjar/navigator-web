package com.world.navigator.game;

import com.world.navigator.game.exceptions.RoomNotFoundException;
import com.world.navigator.game.mapitems.*;
import com.world.navigator.game.playeritems.Key;

import java.util.HashMap;

public class MapBuilder {
  private final HashMap<Integer, Room> rooms;
  private long timeLimit;
  private int playersLimit;
  private int startingGoldCount;

  public MapBuilder() {
    rooms = new HashMap<>();
  }

  public WorldMap getMap() {
    return new WorldMap(playersLimit, rooms, timeLimit, startingGoldCount);
  }

  public void setTimeLimit(long timeLimit) {
    this.timeLimit = timeLimit;
  }

  public void addRoom(int roomId) {
    Room room = new Room(roomId);
    rooms.put(roomId, room);
    fillRoomWalls(room);
  }

  public void addRoomWithLightSwitch(int roomId, boolean isLit) {
    RoomWithLightSwitch room = new RoomWithLightSwitch(roomId, isLit);
    rooms.put(roomId, room);
    fillRoomWalls(room);
  }

  public void addWinningRoom(int roomId) {
    WinningRoom winningRoom = new WinningRoom(roomId);
    rooms.put(roomId, winningRoom);
  }

  private void fillRoomWalls(Room room) {
    for (Direction direction : Direction.values()) {
      room.setSide(direction, new Wall());
    }
  }

  private Room getRoomByID(int roomId) {
    if (rooms.containsKey(roomId)) {
      return rooms.get(roomId);
    } else {
      throw new RoomNotFoundException();
    }
  }

  public void addNorthDoor(int fromRoomId, int toRoomId, Key key, boolean isLocked) {
    addDoor(fromRoomId, toRoomId, Direction.NORTH, key, isLocked);
  }

  public void addWestDoor(int fromRoomId, int toRoomId, Key key, boolean isLocked) {
    addDoor(fromRoomId, toRoomId, Direction.WEST, key, isLocked);
  }

  public void addDoor(
      int fromRoomId, int toRoomId, Direction direction, Key key, boolean isLocked) {
    Room fromRoom = getRoomByID(fromRoomId);
    Room toRoom = getRoomByID(toRoomId);

    Lock lock = new Lock(key, isLocked);
    Door fromDoor = new Door(toRoom.getID(), lock);
    Door toDoor = new Door(fromRoom.getID(), lock);

    fromRoom.setSide(direction, fromDoor);
    toRoom.setSide(direction.getOpposite(), toDoor);
  }

  private boolean roomExists(int roomId) {
    return rooms.containsKey(roomId);
  }

  public boolean hasDoorInDirection(int roomId, Direction direction) {
    Room room = getRoomByID(roomId);
    return room.getObservableInDirection(direction) instanceof Door;
  }

  public void setRoomSide(int roomId, Direction direction, Observable roomWall) {
    Room room = getRoomByID(roomId);
    room.setSide(direction, roomWall);
  }

  public void setPlayersLimit(int playersLimit) {
    this.playersLimit = playersLimit;
  }

  public void setStartingGoldCount(int startingGoldCount) {
    this.startingGoldCount = startingGoldCount;
  }
}
