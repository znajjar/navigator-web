package com.world.navigator.game.player;

import com.world.navigator.game.Direction;
import com.world.navigator.game.entities.Checkable;
import com.world.navigator.game.entities.Room;
import com.world.navigator.game.entities.RoomFloor;
import com.world.navigator.game.playeritems.InventoryItem;

public class CheckingManager {
  private static final JsonPlayerResponseFactory RESPONSE_FACTORY = JsonPlayerResponseFactory.getInstance();
    private final Inventory inventory;
  private final Location location;

  public CheckingManager(Inventory inventory, Location location) {
    this.inventory = inventory;
    this.location = location;
  }

  public PlayerResponse checkItemInFront() {
    Room currentRoom = location.getCurrentRoom();
    Direction facingDirection = location.getFacingDirection();

    if (currentRoom.isCheckableInDirection(facingDirection)) {
      Checkable checkableInFront = currentRoom.checkInDirection(facingDirection);
      return check(checkableInFront);
    } else {
      return RESPONSE_FACTORY.createFailedCheckResponse("Item is not checkable");
    }
  }

  public void checkFloor() {
    Room currentRoom = location.getCurrentRoom();
    RoomFloor roomFloor = currentRoom.getFloor();
    for (InventoryItem item : roomFloor.takeOutItems()) {
      inventory.takeItem(item);
    }
  }

  private PlayerResponse check(Checkable checkable) {
    PlayerResponse response = RESPONSE_FACTORY.createSuccessfulCheckResponse(checkable);

    if (checkable.canBeChecked()) {
      InventoryItem[] acquiredItems = checkable.takeOutItems();
      for (InventoryItem item : acquiredItems) {
        inventory.takeItem(item);
      }
    }

    return response;
  }
}
