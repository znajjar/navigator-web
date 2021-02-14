package com.world.navigator.game.player;

import com.world.navigator.game.LootingVisitor;
import com.world.navigator.game.playeritems.Flashlight;
import com.world.navigator.game.playeritems.GoldBag;
import com.world.navigator.game.playeritems.InventoryItem;
import com.world.navigator.game.playeritems.Key;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory implements LootingVisitor {
  private final GoldBag goldBag;
  private final HashMap<String, Key> keys;
  private final ArrayList<Flashlight> flashlights;

  public Inventory() {
    this.goldBag = new GoldBag(0);
    flashlights = new ArrayList<>();
    keys = new HashMap<>();
  }

  public boolean hasFlashlight() {
    return !flashlights.isEmpty();
  }

  public Flashlight getFlashlight() {
    if (flashlights.size() == 0) {
      return Flashlight.getNullFlashlight();
    }
    return flashlights.get(0);
  }

  public boolean hasKey(String keyName) {
    return keys.containsKey(keyName);
  }

  public Key getKey(String keyName) {
    return keys.get(keyName);
  }

  public int getGoldCount() {
    return goldBag.getGoldCount();
  }

  public boolean hasItem(String itemName) {
    if (itemName.equals("flashlight")) {
      return hasFlashlight();
    } else {
      return hasKey(itemName);
    }
  }

  public InventoryItem[] getItems() {
    ArrayList<InventoryItem> items = new ArrayList<>();
    items.add(goldBag);
    items.addAll(keys.values());
    items.addAll(flashlights);
    return items.toArray(new InventoryItem[0]);
  }

  public InventoryItem takeOutItem(String itemName) {
    if (itemName.equals("flashlight")) {
      return takeOutFlashlight();
    } else {
      return takeOutKey(itemName);
    }
  }

  private Flashlight takeOutFlashlight() {
    int lastIndex = flashlights.size() - 1;
    Flashlight temp = flashlights.get(lastIndex);
    flashlights.remove(lastIndex);
    return temp;
  }

  private Key takeOutKey(String keyName) {
    Key temp = keys.get(keyName);
    keys.remove(keyName);
    return temp;
  }

  public boolean hasEnoughGoldFor(int itemPrice) {
    return getGoldCount() >= itemPrice;
  }

  public GoldBag takeOutGold(int goldToTakeOut) {
    GoldBag goldTakenOut = new GoldBag();
    goldTakenOut.takeGoldFrom(goldBag, goldToTakeOut);
    return goldTakenOut;
  }

  public int getPoints() {
    int points = 0;
    for (InventoryItem item : getItems()) {
      points += item.getPrice();
    }
    return points;
  }

  public void takeItem(InventoryItem item) {
    item.getLootedBy(this);
  }

  @Override
  public void loot(Key key) {
    keys.put(key.getName(), key);
  }

  @Override
  public void loot(Flashlight flashlight) {
    flashlights.add(flashlight);
  }

  @Override
  public void loot(GoldBag goldBag) {
    this.goldBag.takeGoldFrom(goldBag, goldBag.getGoldCount());
  }
}
