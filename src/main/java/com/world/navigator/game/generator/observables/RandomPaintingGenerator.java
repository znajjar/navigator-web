package com.world.navigator.game.generator.observables;

import com.world.navigator.game.entities.Observable;
import com.world.navigator.game.entities.Painting;
import com.world.navigator.game.generator.GameRandomizer;
import com.world.navigator.game.playeritems.Key;

public class RandomPaintingGenerator implements RandomObservableGenerator {
  private final GameRandomizer gameRandomizer;

  public RandomPaintingGenerator(GameRandomizer gameRandomizer) {
    this.gameRandomizer = gameRandomizer;
  }

  @Override
  public Observable generate() {
    Key key = gameRandomizer.nextKey();
    return new Painting(key);
  }
}
