package com.world.navigator.game.entities;

import com.world.navigator.game.playeritems.InventoryItem;

import java.util.ArrayList;

public class RoomFloor implements Container {
  private final ArrayList<InventoryItem> items;

  public RoomFloor() {
    items = new ArrayList<>();
  }

  @Override
  public void putItem(InventoryItem item) {
    items.add(item);
  }

  @Override
  public boolean canBeChecked() {
    return true;
  }

  @Override
  public InventoryItem[] getItems() {
    return items.toArray(new InventoryItem[0]);
  }

  @Override
  public InventoryItem[] takeOutItems() {
    InventoryItem[] items = getItems();
    this.items.clear();
    return items;
  }

  @Override
  public String getRequiredKeyName() {
    return null;
  }

  @Override
  public String look() {
    return "floor";
  }
}
