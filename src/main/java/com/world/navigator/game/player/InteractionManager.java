package com.world.navigator.game.player;

import com.world.navigator.game.Direction;
import com.world.navigator.game.mapitems.LockedWithKey;
import com.world.navigator.game.mapitems.Room;
import com.world.navigator.game.mapitems.RoomWithLightSwitch;
import com.world.navigator.game.playeritems.Key;

class InteractionManager {
  private static final PlayerEventFactory RESPONSE_FACTORY = PlayerEventFactory.getInstance();
  private final Inventory inventory;
  private final Location location;

  public InteractionManager(Inventory inventory, Location location) {
    this.inventory = inventory;
    this.location = location;
  }

  public PlayerEvent useKey(String keyName) {
    if (inventory.hasKey(keyName)) {
      Key key = inventory.getKey(keyName);
      return useKeyOnItemInFront(key);
    } else {
      return RESPONSE_FACTORY.createFailedUseKeyResponse("You don't have this key.");
    }
  }

  private PlayerEvent useKeyOnItemInFront(Key key) {
    Room currentRoom = location.getCurrentRoom();
    Direction facingDirection = location.getFacingDirection();
    if (currentRoom.isLockedWithKeyInDirection(facingDirection)) {
      LockedWithKey lockedItem = currentRoom.getLockedWithKeyInDirection(facingDirection);
      lockedItem.useKey(key);
      return RESPONSE_FACTORY.createSuccessfulUseKeyResponse();
    } else {
      return RESPONSE_FACTORY.createFailedUseKeyResponse("Can't use key on item in front.");
    }
  }

  public PlayerEvent switchRoomLights() {
    Room currentRoom = location.getCurrentRoom();
    if (currentRoom instanceof RoomWithLightSwitch) {
      boolean isLit = ((RoomWithLightSwitch) currentRoom).switchLights();
      return RESPONSE_FACTORY.createSuccessfulSwitchLightsResponse(isLit);
    } else {
      return RESPONSE_FACTORY.createFailedSwitchLightsResponse("Room doesn't have a light switch");
    }
  }

  public PlayerEvent useFlashlight() {
    if (inventory.hasFlashlight()) {
      inventory.getFlashlight().switchLight();
      return RESPONSE_FACTORY.createSuccessfulUseFlashlightResponse(inventory.getFlashlight());
    } else {
      return RESPONSE_FACTORY.createFailedUseFlashlightResponse("You don't have a flashlight.");
    }
  }
}
