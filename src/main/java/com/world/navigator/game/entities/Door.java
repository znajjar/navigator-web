package com.world.navigator.game.entities;

import com.world.navigator.game.exceptions.ItemIsLockedException;
import com.world.navigator.game.playeritems.InventoryItem;

public class Door extends LockedWithKey implements PassThrough {
  private final Room nextRoom;

    public Door(Room nextRoom, Lock lock) {
        super(lock);
        this.nextRoom = nextRoom;
    }

    @Override
    public Room getNextRoom() throws ItemIsLockedException {
        if (isUnlocked()) {
            return nextRoom;
        } else {
            throw new ItemIsLockedException();
        }
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
    return "door";
  }
}
