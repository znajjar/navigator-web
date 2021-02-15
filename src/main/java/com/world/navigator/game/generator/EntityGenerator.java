package com.world.navigator.game.generator;

import com.world.navigator.game.entities.Observable;
import com.world.navigator.game.generator.observables.*;

import java.util.ArrayList;
import java.util.Collections;

public class EntityGenerator {
  private final GameRandomizer gameRandomizer;
  private final int totalRoomCount;
  private final int winningRoomCount;
  private final int mapDimension;
  private final ArrayList<RandomObservableGenerator> observableGenerators;

  public EntityGenerator(GameRandomizer gameRandomizer, int requiredRoomCount) {
    this.gameRandomizer = gameRandomizer;
    mapDimension = (int) Math.ceil(Math.sqrt(requiredRoomCount));
    totalRoomCount = mapDimension * mapDimension;
    winningRoomCount = gameRandomizer.getWinningRoomsCount(totalRoomCount);
    observableGenerators = new ArrayList<>();
    fillObservableGenerators();
  }

  private void fillObservableGenerators() {
    observableGenerators.add(new RandomChestGenerator(gameRandomizer));
    observableGenerators.add(new RandomMirrorGenerator(gameRandomizer));
    observableGenerators.add(new RandomPaintingGenerator(gameRandomizer));
    observableGenerators.add(new RandomTraderGenerator(gameRandomizer));
    observableGenerators.add(new WallGenerator());
  }

  public int getMapDimension() {
    return mapDimension;
  }

  public ArrayList<Integer> getWinningRoomsIds() {
    ArrayList<Integer> roomsIds = getRoomsIds();
    Collections.shuffle(roomsIds);
    ArrayList<Integer> winningRoomsIds = new ArrayList<>();
    for (int i = 0; i < winningRoomCount; i++) {
      winningRoomsIds.add(roomsIds.get(i));
    }
    return winningRoomsIds;
  }

  private ArrayList<Integer> getRoomsIds() {
    ArrayList<Integer> roomsIds = new ArrayList<>();
    for (int i = 0; i < totalRoomCount; i++) {
      roomsIds.add(i);
    }
    return roomsIds;
  }

  public Observable getRandomObservable() {
    int observableType = gameRandomizer.nextInt(observableGenerators.size());
    return observableGenerators.get(observableType).generate();
  }
}
