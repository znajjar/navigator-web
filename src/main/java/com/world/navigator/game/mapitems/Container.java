package com.world.navigator.game.mapitems;

import com.world.navigator.game.playeritems.InventoryItem;

public interface Container extends Checkable {
  void putItem(InventoryItem item);
}
