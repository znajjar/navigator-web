package com.world.navigator.game.generator;

import com.world.navigator.game.playeritems.Key;

public interface DifficultyLevel {
  Key[] getKeys();

  double getWinningRoomsRatio();

  double getLockedDoorProbability();

  double getDoorProbability();

  double getLockedChestProbability();

  double getLitFlashlightProbability();

  double getRoomHasLightSwitchProbability();

  double getLitRoomProbability();

  int getAverageChestItemsCount();

  int getAverageGoldCount();

  int getTimeLimitInSeconds();

  int getStartingGoldCount();
}
