package com.world.navigator.game;

import com.world.navigator.game.mapitems.Checkable;
import com.world.navigator.game.mapitems.Room;
import com.world.navigator.game.mapitems.RoomWithLightSwitch;
import com.world.navigator.game.mapitems.Trader;
import com.world.navigator.game.player.PlayerEvent;
import com.world.navigator.game.playeritems.Flashlight;
import com.world.navigator.game.playeritems.GoldBag;
import com.world.navigator.game.playeritems.InventoryItem;
import com.world.navigator.game.playeritems.Key;
import org.json.JSONObject;

public class JsonPlayerEvent implements PlayerEvent {
  JSONObject jsonObject;
  String type;

  public JsonPlayerEvent(String type) {
    jsonObject = new JSONObject();
    this.type = type;
  }

  @Override
  public String toString() {
    jsonObject.put("type", type);
    return jsonObject.toString();
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public int getInt(String key) {
    return jsonObject.getInt(key);
  }

  @Override
  public double getDouble(String key) {
    return jsonObject.getDouble(key);
  }

  @Override
  public boolean getBoolean(String key) {
    return jsonObject.getBoolean(key);
  }

  @Override
  public String getString(String key) {
    return jsonObject.getString(key);
  }

  @Override
  public void put(String key, int value) {
    jsonObject.put(key, value);
  }

  @Override
  public void put(String key, double value) {
    jsonObject.put(key, value);
  }

  @Override
  public void put(String key, boolean value) {
    jsonObject.put(key, value);
  }

  @Override
  public void put(String key, String value) {
    jsonObject.put(key, value);
  }

  @Override
  public void put(String key, Room value) {
    jsonObject.put(key, roomToJson(value));
  }

  private JSONObject roomToJson(Room room) {
    JSONObject roomJson = new JSONObject();
    roomJson.put("roomId", room.getID());
    if (room instanceof RoomWithLightSwitch) {
      roomJson.put("lightSwitch", true);
      roomJson.put("lit", ((RoomWithLightSwitch) room).isLit());
    } else {
      roomJson.put("lightSwitch", false);
    }
    return roomJson;
  }

  @Override
  public void put(String key, Checkable value) {
    jsonObject.put(key, checkableToJson(value));
  }

  private JSONObject checkableToJson(Checkable checkable) {
    JSONObject checkableJson = new JSONObject();
    checkableJson.put("type", checkable.look());

    boolean isChecked = checkable.canBeChecked();
    checkableJson.put("isChecked", isChecked);

    if (isChecked) {
      InventoryItem[] items = checkable.getItems();
      for (InventoryItem item : items) {
        checkableJson.append("acquiredItems", inventoryItemToJson(item));
      }
    } else {
      checkableJson.append("requiredKey", checkable.getRequiredKeyName());
    }
    return checkableJson;
  }

  @Override
  public void put(String key, Trader value) {
    jsonObject.put(key, traderToJson(value));
  }

  private JSONObject traderToJson(Trader trader) {
    JSONObject traderJson = new JSONObject();
    traderJson.put("type", trader.look());

    InventoryItem[] menu = trader.getMenu();
    for (InventoryItem item : menu) {
      traderJson.append("menu", inventoryItemToJson(item));
    }

    return traderJson;
  }

  @Override
  public void put(String key, InventoryItem value) {
    jsonObject.put(key, inventoryItemToJson(value));
  }

  @Override
  public void appendItemTo(String arrayKey, InventoryItem item) {
    jsonObject.append(arrayKey, inventoryItemToJson(item));
  }

  private JSONObject inventoryItemToJson(InventoryItem item) {
    JSONObject itemJson = new JSONObject();
    itemJson.put("type", item.getItemType());
    itemJson.put("price", item.getPrice());

    if (item instanceof Key) {
      itemJson.put("name", ((Key) item).getName());
    } else if (item instanceof Flashlight) {
      itemJson.put("light", ((Flashlight) item).isLit());
    } else if (item instanceof GoldBag) {
      itemJson.put("count", ((GoldBag) item).getGoldCount());
    } else {
      throw new IllegalArgumentException("unsupported inventory item type");
    }

    return itemJson;
  }
}
