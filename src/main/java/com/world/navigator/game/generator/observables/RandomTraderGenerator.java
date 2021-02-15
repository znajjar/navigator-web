package com.world.navigator.game.generator.observables;

import com.world.navigator.game.entities.Observable;
import com.world.navigator.game.entities.Trader;
import com.world.navigator.game.generator.GameRandomizer;
import com.world.navigator.game.playeritems.Flashlight;
import com.world.navigator.game.playeritems.InventoryItem;

import java.util.ArrayList;
import java.util.Arrays;

public class RandomTraderGenerator implements RandomObservableGenerator {
  private final GameRandomizer gameRandomizer;

  public RandomTraderGenerator(GameRandomizer gameRandomizer) {
    this.gameRandomizer = gameRandomizer;
  }

  @Override
  public Observable generate() {
    ArrayList<InventoryItem> items = new ArrayList<>(Arrays.asList(gameRandomizer.getKeySet()));
    items.add(new Flashlight(false));
    return new Trader(items.toArray(new InventoryItem[0]));
  }
}
