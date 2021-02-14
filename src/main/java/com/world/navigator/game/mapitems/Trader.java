package com.world.navigator.game.mapitems;

import com.world.navigator.game.exceptions.TraderDoesNotHaveItemException;
import com.world.navigator.game.playeritems.GoldBag;
import com.world.navigator.game.playeritems.InventoryItem;

import java.util.HashMap;

public class Trader implements Observable {
  private static final int GOLD = 100;
  private final HashMap<String, InventoryItem> menu;
  private final GoldBag goldBag;

  public Trader(InventoryItem[] menu) {
    goldBag = new GoldBag(GOLD);
    this.menu = new HashMap<>();
    for (InventoryItem item : menu) {
      this.menu.put(item.getItemType(), item);
    }
  }

  @Override
  public String look() {
    return "trader";
  }

  public InventoryItem[] getMenu() {
    return menu.values().toArray(new InventoryItem[0]);
  }

  public boolean canSellItem(String itemName) {
    return menu.containsKey(itemName);
  }

  public int getItemPrice(String itemName) {
    if (canSellItem(itemName)) {
      return menu.get(itemName).getPrice();
    } else {
      throw new TraderDoesNotHaveItemException();
    }
  }

  public InventoryItem sellItemToPlayer(String itemName, GoldBag playerGoldBag) {
    if (menu.containsKey(itemName)) {
      InventoryItem itemToBeSold = menu.get(itemName);
      goldBag.takeGoldFrom(playerGoldBag, itemToBeSold.getPrice());
      return itemToBeSold;
    } else {
      throw new TraderDoesNotHaveItemException();
    }
  }

  public GoldBag buyItemFromPlayer(InventoryItem itemToBeBought) {
    menu.put(itemToBeBought.getItemType(), itemToBeBought);
    return goldBag.moveToNewBag(itemToBeBought.getPrice());
  }
}
