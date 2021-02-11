package com.world.navigator.game.mapitems;

import com.world.navigator.game.exceptions.ItemIsLockedException;
import com.world.navigator.game.playeritems.InventoryItem;
import com.world.navigator.game.playeritems.Key;

import java.util.ArrayList;
import java.util.Arrays;

public class Chest extends LockedWithKey implements Container {
  private final ArrayList<InventoryItem> items;

  public Chest(Key key, boolean locked, InventoryItem[] items) {
    super(key, locked);
    this.items = new ArrayList<>(Arrays.asList(items));
  }

  @Override
  public String toString() {
//    return "Chest requires the %s key".formatted(getKeyName());
    return "chest";
  }

  @Override
  public String look() {
    return "chest";
  }

  @Override
  public InventoryItem[] getItems() {
    if (isUnlocked()) {
      return items.toArray(new InventoryItem[0]);
    } else {
      throw new ItemIsLockedException();
    }
  }

  @Override
  public InventoryItem[] takeOutItems() {
    InventoryItem[] items = getItems();
    this.items.clear();
    return items;
  }

  @Override
  public void putItem(InventoryItem item) {
    items.add(item);
  }

  @Override
  public boolean canBeChecked() {
    return isUnlocked();
  }

  @Override
  public String getRequiredKeyName() {
    return getKeyName();
  }
}
