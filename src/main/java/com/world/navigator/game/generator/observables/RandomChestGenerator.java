package com.world.navigator.game.generator.observables;

import com.world.navigator.game.entities.Chest;
import com.world.navigator.game.entities.Observable;
import com.world.navigator.game.generator.GameRandomizer;
import com.world.navigator.game.generator.inventory.RandomFlashLightGenerator;
import com.world.navigator.game.generator.inventory.RandomGoldBagGenerator;
import com.world.navigator.game.generator.inventory.RandomInventoryItemGenerator;
import com.world.navigator.game.generator.inventory.RandomKeyGenerator;
import com.world.navigator.game.playeritems.InventoryItem;
import com.world.navigator.game.playeritems.Key;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RandomChestGenerator implements RandomObservableGenerator {
  private final GameRandomizer gameRandomizer;
  private final ArrayList<RandomInventoryItemGenerator> generators;

  public RandomChestGenerator(GameRandomizer gameRandomizer) {
    this.gameRandomizer = gameRandomizer;
    generators = new ArrayList<>();
    generators.add(new RandomFlashLightGenerator(gameRandomizer));
    generators.add(new RandomGoldBagGenerator(gameRandomizer));
    generators.add(new RandomKeyGenerator(gameRandomizer));
  }

  @Override
  public Observable generate() {
    Key key = gameRandomizer.nextKey();
    boolean isLocked = gameRandomizer.isNextChestLocked();

    int itemCount = gameRandomizer.nextChestItemsCount();
    int itemTypeCount = generators.size();
    int[] itemCounts = gameRandomizer.distributeNumberIntoGroups(itemCount, itemTypeCount);
    ArrayList<InventoryItem> items = new ArrayList<>();
    Collections.shuffle(generators);

    for (int i = 0; i < itemTypeCount; i++) {
      items.addAll(Arrays.asList(generators.get(i).generate(itemCounts[i])));
    }

    return new Chest(key, isLocked, items.toArray(new InventoryItem[0]));
  }
}
