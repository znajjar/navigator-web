package com.world.navigator.game.entities;

import com.world.navigator.game.playeritems.InventoryItem;

public interface Checkable extends Observable {
  boolean canBeChecked();

  InventoryItem[] getItems();

  InventoryItem[] takeOutItems();

  String getRequiredKeyName();
}
