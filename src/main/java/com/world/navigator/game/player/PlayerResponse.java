package com.world.navigator.game.player;

import com.world.navigator.game.mapitems.Checkable;
import com.world.navigator.game.mapitems.Room;
import com.world.navigator.game.mapitems.Trader;
import com.world.navigator.game.playeritems.InventoryItem;

public interface PlayerResponse {
  String getType();

  boolean isSuccessful();

  int getInt(String key);

  double getDouble(String key);

  boolean getBoolean(String key);

  String getString(String key);

  void put(String key, int value);

  void put(String key, double value);

  void put(String key, boolean value);

  void put(String key, String value);

  void put(String key, Room value);

  void put(String key, InventoryItem value);

  void put(String key, Checkable value);

  void put(String key, Trader value);

  void appendItemTo(String arrayKey, InventoryItem item);
}
