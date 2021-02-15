package com.world.navigator.game.entities;

import com.world.navigator.game.Direction;
import com.world.navigator.game.exceptions.RoomNotLitException;
import com.world.navigator.game.exceptions.WrongTypeOfItemException;
import com.world.navigator.game.playeritems.Flashlight;

import java.util.EnumMap;
import java.util.Objects;

public class Room {
  private static Room EMPTY_ROOM;
  private final EnumMap<Direction, Observable> walls;
  private final RoomFloor floor;
  private final int id;

  public Room(int id) {
    this.id = id;
    walls = new EnumMap<>(Direction.class);
    for (Direction direction : Direction.values()) {
      walls.put(direction, new Wall());
    }
    floor = new RoomFloor();
  }

  public static Room getEmptyRoom() {
    if (EMPTY_ROOM == null) {
      EMPTY_ROOM = new Room(-1);
    }

    return EMPTY_ROOM;
  }

  public void setSide(Direction direction, Observable itemOnWall) {
    walls.put(direction, itemOnWall);
  }

  public EnumMap<Direction, Observable> getWalls() {
    return walls;
  }

  public int getID() {
    return id;
  }

  public boolean canLookInDirectionWithFlashLight(Direction direction, Flashlight flashlight) {
    return Flashlight.isValid(flashlight) && flashlight.isLit();
  }

  public String lookInDirectionWithFlashlight(Direction direction, Flashlight flashlight) {
    if (canLookInDirectionWithFlashLight(direction, flashlight)) {
      return walls.get(direction).look();
    } else {
      throw new RoomNotLitException();
    }
  }

  public Observable getObservableInDirection(Direction direction) {
    return walls.get(direction);
  }

  public boolean isCheckableInDirection(Direction direction) {
    return walls.get(direction) instanceof Checkable;
  }

  public Checkable checkInDirection(Direction direction) {
    Observable itemOnWall = walls.get(direction);
    if (itemOnWall instanceof Checkable) {
      return (Checkable) itemOnWall;
    } else {
      throw new WrongTypeOfItemException("Can not check item of type " + Observable.class);
    }
  }

  public boolean isTraderInDirection(Direction direction) {
    return walls.get(direction) instanceof Trader;
  }

  public Trader getTraderInDirection(Direction direction) {
    Observable itemOnWall = walls.get(direction);
    if (itemOnWall instanceof Trader) {
      return (Trader) itemOnWall;
    } else {
      throw new WrongTypeOfItemException("Item of type " + Observable.class + " is not a Trader");
    }
  }

  public boolean isPassThroughInDirection(Direction direction) {
    return walls.get(direction) instanceof PassThrough;
  }

  public PassThrough getPassThroughInDirection(Direction direction) {
    Observable itemOnWall = walls.get(direction);
    if (itemOnWall instanceof PassThrough) {
      return (PassThrough) itemOnWall;
    } else {
      throw new WrongTypeOfItemException(
          "Item of type " + Observable.class + " is not a PassThrough");
    }
  }

  public boolean isLockedWithKeyInDirection(Direction direction) {
    return walls.get(direction) instanceof LockedWithKey;
  }

  public LockedWithKey getLockedWithKeyInDirection(Direction direction) {
    Observable itemOnWall = walls.get(direction);
    if (itemOnWall instanceof LockedWithKey) {
      return (LockedWithKey) itemOnWall;
    } else {
      throw new WrongTypeOfItemException(
              "Item of type " + Observable.class + " is not a LockedWithKey");
    }
  }

  public RoomFloor getFloor() {
    return floor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Room room = (Room) o;
    return id == room.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
