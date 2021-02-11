package com.world.navigator.game.playeritems;

import com.world.navigator.game.LootingVisitor;
import com.world.navigator.game.exceptions.ItemIsNullException;

public class Flashlight implements InventoryItem {
  private static final int FLASHLIGHT_PRICE = 2;
  private static int serialNumberCounter = 1;
  private static Flashlight nullFlashlight;
  private final int serialNumber;
  private boolean light;

  public Flashlight(boolean light) {
    this.light = light;
    serialNumber = serialNumberCounter;
    serialNumberCounter += 1;
  }

  public static Flashlight getNullFlashlight() {
    if (nullFlashlight == null) {
      nullFlashlight = new Flashlight(false);
    }
    return nullFlashlight;
  }

  public static boolean checkIfNull(Flashlight flashlight) {
    if (nullFlashlight == null) {
      return false;
    } else {
      return flashlight == nullFlashlight;
    }
  }

  public static boolean isValid(Flashlight flashlight) {
    if (nullFlashlight == null) {
      return true;
    } else {
      return flashlight != nullFlashlight;
    }
  }

  public void switchLight() {
    if (checkIfNull(this)) {
      throw new ItemIsNullException();
    } else {
      light ^= true;
    }
  }

  public boolean isLit() {
    return light;
  }

  @Override
  public String getItemType() {
    return "Flashlight";
  }

  @Override
  public void getLootedBy(LootingVisitor lootingVisitor) {
    lootingVisitor.loot(this);
  }

  @Override
  public int getPrice() {
    return FLASHLIGHT_PRICE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Flashlight that = (Flashlight) o;
    return serialNumber == that.serialNumber;
  }
}
