package com.world.navigator.game.generator.observables;

import com.world.navigator.game.entities.Observable;
import com.world.navigator.game.entities.Wall;

public class WallGenerator implements RandomObservableGenerator {

  @Override
  public Observable generate() {
    return new Wall();
  }
}
