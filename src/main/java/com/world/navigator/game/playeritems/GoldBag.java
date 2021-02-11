package com.world.navigator.game.playeritems;

import com.world.navigator.game.LootingVisitor;
import com.world.navigator.game.exceptions.BagDoesNotHaveEnoughGoldException;

public class GoldBag implements InventoryItem {
  private int gold;

  public GoldBag() {
    this.gold = 0;
  }

  public GoldBag(int gold) {
    if (gold < 0) {
      throw new IllegalArgumentException();
    }
    this.gold = gold;
  }

  public int getGoldCount() {
    return gold;
  }

  public void takeGoldFrom(GoldBag bag, int goldToTakeOut) {
    if (bag.gold >= goldToTakeOut) {
      bag.gold -= goldToTakeOut;
      gold += goldToTakeOut;
    } else {
      throw new BagDoesNotHaveEnoughGoldException();
    }
  }

  public GoldBag moveToNewBag(int goldToMoveOut) {
    if (gold >= goldToMoveOut) {
      gold -= goldToMoveOut;
      return new GoldBag(goldToMoveOut);
    } else {
      throw new BagDoesNotHaveEnoughGoldException();
    }
  }

  @Override
  public String getItemType() {
    return "GoldBag";
  }

  @Override
  public void getLootedBy(LootingVisitor lootingVisitor) {
    lootingVisitor.loot(this);
  }

  @Override
  public int getPrice() {
    return gold;
  }
}
