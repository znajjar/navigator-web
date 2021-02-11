package com.world.navigator.game.generator.inventory;

import com.world.navigator.game.playeritems.InventoryItem;

public interface RandomInventoryItemGenerator {
  default InventoryItem generate() {
    return generate(1)[0];
  }

  InventoryItem[] generate(int count);
}
