package com.world.navigator.game.generator.inventory;

import com.world.navigator.game.generator.GameRandomizer;
import com.world.navigator.game.playeritems.InventoryItem;
import com.world.navigator.game.playeritems.Key;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RandomKeyGenerator implements RandomInventoryItemGenerator {
  private final GameRandomizer gameRandomizer;

  public RandomKeyGenerator(GameRandomizer gameRandomizer) {
    this.gameRandomizer = gameRandomizer;
  }

  @Override
  public InventoryItem generate() {
    return gameRandomizer.nextKey();
  }

  @Override
  public InventoryItem[] generate(int count) {
    InventoryItem[] keys = new InventoryItem[count];
    ArrayList<Key> keySet = new ArrayList<>(Arrays.asList(gameRandomizer.getKeySet()));
    Collections.shuffle(keySet);
    for (int i = 0; i < count && i < keySet.size(); i++) {
      keys[i] = keySet.get(i);
    }

    return keys;
  }
}
