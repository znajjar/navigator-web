package com.world.navigator.game.player;

import com.world.navigator.game.Direction;
import com.world.navigator.game.mapitems.Room;
import com.world.navigator.game.mapitems.Trader;
import com.world.navigator.game.playeritems.GoldBag;
import com.world.navigator.game.playeritems.InventoryItem;

public class TradingManager {
  public static final String NOT_FACING_TRADER_MESSAGE = "You aren't facing a trader";
  private static final PlayerResponseFactory RESPONSE_FACTORY = PlayerResponseFactory.getInstance();
  private final Inventory inventory;
  private final Location location;

  TradingManager(Inventory inventory, Location location) {
    this.inventory = inventory;
    this.location = location;
  }

  public PlayerResponse trade() {
    if (isTraderInFront()) {
      Trader trader = getTraderInFront();
      return RESPONSE_FACTORY.createSuccessfulTradeResponse(trader);
    } else {
      return RESPONSE_FACTORY.createFailedTradeResponse(NOT_FACING_TRADER_MESSAGE);
    }
  }

  public PlayerResponse buy(String itemName) {
    if (isTraderInFront()) {
      Trader trader = getTraderInFront();
      return buyFromTrader(trader, itemName);
    } else {
      return RESPONSE_FACTORY.createFailedBuyResponse(NOT_FACING_TRADER_MESSAGE);
    }
  }

  private PlayerResponse buyFromTrader(Trader trader, String itemName) {
    if (trader.canSellItem(itemName)) {
      int itemPrice = trader.getItemPrice(itemName);
      if (inventory.hasEnoughGoldFor(itemPrice)) {
        GoldBag goldBagWithItemPrice = inventory.takeOutGold(itemPrice);
        InventoryItem boughtItem = trader.sellItemToPlayer(itemName, goldBagWithItemPrice);
        inventory.takeItem(boughtItem);
        return RESPONSE_FACTORY.createSuccessfulBuyResponse(boughtItem);
      } else {
        return RESPONSE_FACTORY.createFailedBuyResponse("You don't have enough gold");
      }
    } else {
      return RESPONSE_FACTORY.createFailedBuyResponse("Trader can't sell this item");
    }
  }

  public PlayerResponse sell(String itemName) {
    if (inventory.hasItem(itemName)) {
      Trader trader = getTraderInFront();
      InventoryItem itemToBeSold = inventory.takeOutItem(itemName);
      return sellToTrader(trader, itemToBeSold);
    } else {
      return RESPONSE_FACTORY.createFailedSellResponse("You don't have this item");
    }
  }

  private PlayerResponse sellToTrader(Trader trader, InventoryItem itemToBeSold) {
    GoldBag goldBag = trader.buyItemFromPlayer(itemToBeSold);
    inventory.loot(goldBag);
    return RESPONSE_FACTORY.createSuccessfulSellResponse(itemToBeSold);
  }

  private boolean isTraderInFront() {
    Room currentRoom = location.getCurrentRoom();
    Direction facingDirection = location.getFacingDirection();
    return currentRoom.isTraderInDirection(facingDirection);
  }

  private Trader getTraderInFront() {
    Room currentRoom = location.getCurrentRoom();
    Direction facingDirection = location.getFacingDirection();
    return currentRoom.getTraderInDirection(facingDirection);
  }
}
