package com.world.navigator.game.generator.inventory;

import com.world.navigator.game.generator.GameRandomizer;
import com.world.navigator.game.playeritems.Flashlight;
import com.world.navigator.game.playeritems.InventoryItem;

public class RandomFlashLightGenerator implements RandomInventoryItemGenerator {
  private final GameRandomizer gameRandomizer;

  public RandomFlashLightGenerator(GameRandomizer gameRandomizer) {
    this.gameRandomizer = gameRandomizer;
  }

  @Override
  public InventoryItem[] generate(int count) {
    InventoryItem[] flashlights = new InventoryItem[count];
    for (int i = 0; i < count; i++) {
      flashlights[i] = new Flashlight(gameRandomizer.isNextFlashlightLit());
    }
    return flashlights;
  }
}
