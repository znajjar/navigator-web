package com.world.navigator.game.playeritems;

import com.world.navigator.game.LootingVisitor;

public interface InventoryItem {
  String getItemType();

  void getLootedBy(LootingVisitor lootingVisitor);

  int getPrice();
}
