package com.world.navigator.game.mapitems;

import com.world.navigator.game.exceptions.ItemIsLockedException;
import com.world.navigator.game.playeritems.InventoryItem;

public class Door extends LockedWithKey implements PassThrough {
  private final int nextRoomId;

  public Door(int nextRoomId, Lock lock) {
    super(lock);
    this.nextRoomId = nextRoomId;
  }

  @Override
  public int getNextRoomID() throws ItemIsLockedException {
    if (isUnlocked()) {
      return nextRoomId;
    } else {
      throw new ItemIsLockedException();
    }
  }

  @Override
  public boolean canPassThrough() {
    return isUnlocked();
  }

  @Override
  public String look() {
    return "door";
  }

  @Override
  public boolean canBeChecked() {
    return isUnlocked();
  }

  @Override
  public InventoryItem[] getItems() {
    return new InventoryItem[0];
  }

  @Override
  public InventoryItem[] takeOutItems() {
    return new InventoryItem[0];
  }

  @Override
  public String getRequiredKeyName() {
    return getKeyName();
  }

  @Override
  public String toString() {
    //    return "Door requires the %s key".formatted(getKeyName());
    return "door";
  }
}
