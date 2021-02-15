package com.world.navigator.game.player;

import com.world.navigator.game.Direction;
import com.world.navigator.game.entities.Room;

class Location {
  private Direction facingDirection;
  private Room currentRoom;

  public Location(Direction facingDirection, Room currentRoom) {
    this.facingDirection = facingDirection;
    this.currentRoom = currentRoom;
  }

  public Direction getFacingDirection() {
    return facingDirection;
  }

  public void setFacingDirection(Direction facingDirection) {
    this.facingDirection = facingDirection;
  }

  public Room getCurrentRoom() {
    return currentRoom;
  }

  public void setCurrentRoom(Room currentRoom) {
    this.currentRoom = currentRoom;
  }
}
