package com.world.navigator.game.generator;

import com.world.navigator.game.playeritems.Key;

import java.util.Random;

public class GameRandomizer {
  private static final int SECONDS_TO_MILLIS_FACTOR = 1000;
  private static final Random RANDOM = new Random();
  private final DifficultyLevel difficultyLevel;

  public GameRandomizer(DifficultyLevel difficultyLevel) {
    this.difficultyLevel = difficultyLevel;
  }

  public int getWinningRoomsCount(int totalRoomsCount) {
    return (int) (totalRoomsCount * difficultyLevel.getWinningRoomsRatio()) - totalRoomsCount;
  }

  public boolean isNextDoorLocked() {
    return getRandomBoolean(difficultyLevel.getLockedDoorProbability());
  }

  public Key nextKey() {
    Key[] keys = difficultyLevel.getKeys();
    int keyIndex = RANDOM.nextInt(keys.length);
    return keys[keyIndex];
  }

  public Key[] getKeySet() {
    return difficultyLevel.getKeys();
  }

  public boolean isNextDoor() {
    return getRandomBoolean(difficultyLevel.getDoorProbability());
  }

  public boolean isNextChestLocked() {
    return getRandomBoolean(difficultyLevel.getLockedChestProbability());
  }

  public boolean isNextFlashlightLit() {
    return getRandomBoolean(difficultyLevel.getLitFlashlightProbability());
  }

  public boolean isNextRoomWithLightSwitch() {
    return getRandomBoolean(difficultyLevel.getRoomHasLightSwitchProbability());
  }

  public boolean isNextRoomLit() {
    return getRandomBoolean(difficultyLevel.getLitRoomProbability());
  }

  private boolean getRandomBoolean(double probability) {
    double randomNumber = RANDOM.nextDouble();
    return probability >= randomNumber;
  }

  public int nextGoldCount() {
    return getRandomIntegerWithAverage(difficultyLevel.getAverageGoldCount());
  }

  public int nextChestItemsCount() {
    return getRandomIntegerWithAverage(difficultyLevel.getAverageChestItemsCount());
  }

  private int getRandomIntegerWithAverage(int average) {
    double rand = RANDOM.nextDouble();
    rand *= 2 * average + 1;
    return (int) rand;
  }

  public int[] distributeNumberIntoGroups(int number, int groupsCount) {
    int[] groups = new int[groupsCount];
    int remaining = number;
    for (int i = 0; i < groupsCount - 1; i++) {
      int group = RANDOM.nextInt(remaining + 1) / 2;
      remaining -= group;
      groups[i] = group;
    }
    groups[groupsCount - 1] = remaining;
    return groups;
  }

  public DoorPairDetails nextDoorPair() {
    boolean isLocked = isNextDoorLocked();
    Key key = nextKey();
    return new DoorPairDetails(key, isLocked);
  }

  public int nextInt(int bound) {
    return RANDOM.nextInt(bound);
  }

  public int getTimeLimitInMilliseconds() {
    return difficultyLevel.getTimeLimitInSeconds() * SECONDS_TO_MILLIS_FACTOR;
  }

  public int getStartingGoldCount() {
    return difficultyLevel.getStartingGoldCount();
  }
}
