package com.world.navigator.game.entities;

import com.world.navigator.game.playeritems.InventoryItem;

public interface Container extends Checkable {
  void putItem(InventoryItem item);
}
