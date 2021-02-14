package com.world.navigator.game.generator;

import com.world.navigator.game.playeritems.Key;

public class DefaultDifficultyLevel implements DifficultyLevel {
  private static final Key[] KEYS = {
    Key.getKey("one"),
    Key.getKey("two"),
    Key.getKey("three"),
    Key.getKey("four"),
    Key.getKey("five")
  };

  @Override
  public Key[] getKeys() {
    return KEYS;
  }

  @Override
  public double getWinningRoomsRatio() {
    return 1.2;
  }

  @Override
  public double getLockedDoorProbability() {
    return 0.25;
  }

  @Override
  public double getDoorProbability() {
    return 0.7;
  }

  @Override
  public double getLockedChestProbability() {
    return 0.25;
  }

  @Override
  public double getLitFlashlightProbability() {
    return 0.5;
  }

  @Override
  public double getRoomHasLightSwitchProbability() {
    return 1;
  }

  @Override
  public double getLitRoomProbability() {
    return 0.5;
  }

  @Override
  public int getAverageChestItemsCount() {
    return 3;
  }

  @Override
  public int getAverageGoldCount() {
    return 10;
  }

  @Override
  public int getTimeLimitInSeconds() {
    return 300;
  }

  @Override
  public int getStartingGoldCount() {
    return 20;
  }
}
