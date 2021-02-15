package com.world.navigator.game.player;

import com.world.navigator.game.Direction;
import com.world.navigator.game.entities.PassThrough;
import com.world.navigator.game.entities.Room;
import com.world.navigator.game.playeritems.Flashlight;

public class NavigationManager {
  private static final PlayerResponseFactory RESPONSE_FACTORY = PlayerResponseFactory.getInstance();
  private final Inventory inventory;
  private final Location location;

  public NavigationManager(Inventory inventory, Location location) {
    this.inventory = inventory;
    this.location = location;
  }

  public Room getCurrentRoom() {
    return location.getCurrentRoom();
  }

  public PlayerResponse turnRight() {
    Direction facingDirection = location.getFacingDirection().getRight();
    location.setFacingDirection(facingDirection);
    return RESPONSE_FACTORY.createSuccessfulTurnRightResponse(facingDirection);
  }

  public PlayerResponse turnLeft() {
    Direction facingDirection = location.getFacingDirection().getLeft();
    location.setFacingDirection(facingDirection);
    return RESPONSE_FACTORY.createSuccessfulTurnLeftResponse(facingDirection);
  }

  public boolean isPassThroughInFront() {
    Direction facingDirection = location.getFacingDirection();
    Room currentRoom = location.getCurrentRoom();
    return currentRoom.isPassThroughInDirection(facingDirection);
  }

  public boolean isPassThroughBehind() {
    Direction oppositeDirection = location.getFacingDirection().getOpposite();
    Room currentRoom = location.getCurrentRoom();
    return currentRoom.isPassThroughInDirection(oppositeDirection);
  }

  public PassThrough getPassThroughInFront() {
    Direction facingDirection = location.getFacingDirection();
    return getPassThroughInDirection(facingDirection);
  }

  public PassThrough getPassThroughBehind() {
    Direction facingDirection = location.getFacingDirection().getOpposite();
    return getPassThroughInDirection(facingDirection);
  }

  private PassThrough getPassThroughInDirection(Direction direction) {
    Room currentRoom = location.getCurrentRoom();
    return currentRoom.getPassThroughInDirection(direction);
  }

  public void moveTo(Room nextRoom) {
    location.setCurrentRoom(nextRoom);
  }

  public PlayerResponse look() {
    if (canLookInFront()) {
      String itemTypeInFront = lookInFront();
      return RESPONSE_FACTORY.createSuccessfulLookResponse(itemTypeInFront);
    } else {
      return RESPONSE_FACTORY.createFailedLookResponse("Room is not lit.");
    }
  }

  private boolean canLookInFront() {
    Direction facingDirection = location.getFacingDirection();
    Room currentRoom = location.getCurrentRoom();
    Flashlight flashlight = inventory.getFlashlight();
    return currentRoom.canLookInDirectionWithFlashLight(facingDirection, flashlight);
  }

  private String lookInFront() {
    Direction facingDirection = location.getFacingDirection();
    Room currentRoom = location.getCurrentRoom();
    Flashlight flashlight = inventory.getFlashlight();
    return currentRoom.lookInDirectionWithFlashlight(facingDirection, flashlight);
  }
}
