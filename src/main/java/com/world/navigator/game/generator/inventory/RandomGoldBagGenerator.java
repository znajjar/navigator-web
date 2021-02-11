package com.world.navigator.game.generator.inventory;

import com.world.navigator.game.generator.GameRandomizer;
import com.world.navigator.game.playeritems.GoldBag;
import com.world.navigator.game.playeritems.InventoryItem;

public class RandomGoldBagGenerator implements RandomInventoryItemGenerator {
  private final GameRandomizer gameRandomizer;

  public RandomGoldBagGenerator(GameRandomizer gameRandomizer) {
    this.gameRandomizer = gameRandomizer;
  }

  @Override
  public InventoryItem[] generate(int count) {
    InventoryItem[] goldBags = new InventoryItem[count];
    for (int i = 0; i < count; i++) {
      goldBags[i] = new GoldBag(gameRandomizer.nextGoldCount());
    }
    return goldBags;
  }
}
