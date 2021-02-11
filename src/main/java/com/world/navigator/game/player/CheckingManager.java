package com.world.navigator.game.player;

import com.world.navigator.game.Direction;
import com.world.navigator.game.mapitems.Checkable;
import com.world.navigator.game.mapitems.Room;
import com.world.navigator.game.mapitems.RoomFloor;
import com.world.navigator.game.playeritems.InventoryItem;

class CheckingManager {
  private static final PlayerEventFactory RESPONSE_FACTORY = PlayerEventFactory.getInstance();
  private final Inventory inventory;
  private final Location location;

  public CheckingManager(Inventory inventory, Location location) {
    this.inventory = inventory;
    this.location = location;
  }

  public PlayerEvent checkItemInFront() {
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
      item.getLootedBy(inventory);
    }
  }

  private PlayerEvent check(Checkable checkable) {
    PlayerEvent response = RESPONSE_FACTORY.createSuccessfulCheckResponse(checkable);

    if (checkable.canBeChecked()) {
      InventoryItem[] acquiredItems = checkable.takeOutItems();
      for (InventoryItem item : acquiredItems) {
        item.getLootedBy(inventory);
      }
    }

    return response;
  }
}
