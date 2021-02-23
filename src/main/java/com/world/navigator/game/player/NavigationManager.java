package com.world.navigator.game.player;

import com.world.navigator.game.Direction;
import com.world.navigator.game.entities.PassThrough;
import com.world.navigator.game.entities.Room;
import com.world.navigator.game.exceptions.ItemIsLockedException;
import com.world.navigator.game.playeritems.Flashlight;

import java.util.concurrent.CopyOnWriteArrayList;

public class NavigationManager {
  private static final JsonPlayerResponseFactory RESPONSE_FACTORY = JsonPlayerResponseFactory.getInstance();
  private final CopyOnWriteArrayList<PlayerMovementListener> listeners;
  private final Inventory inventory;
  private final Location location;
  private final int playerId;

  public NavigationManager(Inventory inventory, Location location, int playerId) {
    this.inventory = inventory;
    this.location = location;
    this.playerId = playerId;
    listeners = new CopyOnWriteArrayList<>();
  }

  public void registerMovementListener(PlayerMovementListener listener) {
    listeners.add(listener);
  }

  public void notifyListenersOnMove(Room nextRoom) {
    listeners.forEach(listener -> listener.onMove(playerId, nextRoom));
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

  public PlayerResponse moveForward() {
    Direction facingDirection = location.getFacingDirection();
    Room currentRoom = location.getCurrentRoom();
    if (currentRoom.isPassThroughInDirection(facingDirection)) {
      return moveThrough(currentRoom.getPassThroughInDirection(facingDirection));
    } else {
      return RESPONSE_FACTORY.createFailedMoveResponse("You can't pass through item in front.");
    }
  }

  public PlayerResponse moveBackward() {
    Direction facingDirection = location.getFacingDirection().getOpposite();
    Room currentRoom = location.getCurrentRoom();
    if (currentRoom.isPassThroughInDirection(facingDirection)) {
      return moveThrough(currentRoom.getPassThroughInDirection(facingDirection));
    } else {
      return RESPONSE_FACTORY.createFailedMoveResponse("You can't pass through item behind.");
    }
  }

  private PlayerResponse moveThrough(PassThrough passThrough) {
    try {
      Room nextRoom = passThrough.getNextRoom();
      location.setCurrentRoom(nextRoom);
      notifyListenersOnMove(nextRoom);
      return RESPONSE_FACTORY.createSuccessfulMoveResponse(nextRoom);
    } catch (ItemIsLockedException e) {
      return RESPONSE_FACTORY.createFailedMoveResponse("Item is locked");
    }
  }

  public PlayerResponse look() {
    Direction facingDirection = location.getFacingDirection();
    Room currentRoom = location.getCurrentRoom();
    Flashlight flashlight = inventory.getFlashlight();
    if (currentRoom.canLookInDirectionWithFlashLight(facingDirection, flashlight)) {
      String itemInFront = currentRoom.lookInDirectionWithFlashlight(facingDirection, flashlight);
      return RESPONSE_FACTORY.createSuccessfulLookResponse(itemInFront);
    } else {
      return RESPONSE_FACTORY.createFailedLookResponse("Room is not lit.");
    }
  }

  public void moveTo(Room nextRoom) {
    location.setCurrentRoom(nextRoom);
  }
}
