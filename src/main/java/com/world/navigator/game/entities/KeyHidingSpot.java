package com.world.navigator.game.entities;

import com.world.navigator.game.playeritems.InventoryItem;
import com.world.navigator.game.playeritems.Key;

public abstract class KeyHidingSpot implements Checkable {
  private Key key;
  private boolean hiddenKey;

  public KeyHidingSpot() {
    key = Key.getEmptyKey();
    hiddenKey = false;
  }

  public KeyHidingSpot(Key key) {
    this.key = key;
    hiddenKey = true;
  }

  @Override
  public boolean canBeChecked() {
    return true;
  }

  @Override
  public InventoryItem[] takeOutItems() {
    InventoryItem[] items = new InventoryItem[1];
    items[0] = getKey();
    return items;
  }

  @Override
  public String getRequiredKeyName() {
    return null;
  }

  @Override
  public InventoryItem[] getItems() {
    if (hasKey()) {
      return new InventoryItem[] {getKey()};
    } else {
      return new InventoryItem[0];
    }
  }

  @Override
  public String toString() {
    String keyString;
    if (hasKey()) {
      //      keyString = "that has the %s key".formatted(key.getItemType());
      keyString = String.format("that has the %s key", key.getItemType());
    } else {
      keyString = "that doesn't have a key";
    }
    return look() + keyString;
  }

  public boolean hasKey() {
    return hiddenKey;
  }

  public Key getKey() {
    hiddenKey = false;
    Key returnKey = key;
    key = Key.getEmptyKey();
    return returnKey;
  }
}
