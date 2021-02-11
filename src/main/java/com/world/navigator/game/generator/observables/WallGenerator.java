package com.world.navigator.game.generator.observables;

import com.world.navigator.game.mapitems.Observable;
import com.world.navigator.game.mapitems.Wall;

public class WallGenerator implements RandomObservableGenerator {

  @Override
  public Observable generate() {
    return new Wall();
  }
}
