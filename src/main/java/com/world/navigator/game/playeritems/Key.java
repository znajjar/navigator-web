package com.world.navigator.game.playeritems;

import com.world.navigator.game.LootingVisitor;
import com.world.navigator.game.exceptions.IllegalKeyNameException;

import java.util.*;

public final class Key implements InventoryItem {
  private static final HashSet<String> RESERVED_NAMES =
      new HashSet<>(Collections.singletonList("empty, flashlight, goldBag, gold"));
  private static final int KEY_PRICE = 10;
  private static final HashMap<String, Key> keys = new HashMap<>();
  private static final Random random = new Random();
  private static Key emptyKey;
  private final String name;
  private final int id;

  private Key(String name, int id) {
    this.name = name;
    this.id = id;
  }

  protected static Key createKey(String name) {
    String lowercaseName = name.toLowerCase();
    if (keys.containsKey(lowercaseName) || RESERVED_NAMES.contains(name)) {
      throw new IllegalKeyNameException();
    }
    int id = random.nextInt();
    Key newKey = new Key(name, id);
    keys.put(name, newKey);
    return newKey;
  }

  public static Key getKey(String keyName) {
    if (keys.containsKey(keyName)) {
      return keys.get(keyName);
    } else {
      return createKey(keyName);
    }
  }

  public static Key getEmptyKey() {
    if (emptyKey == null) emptyKey = new Key("empty", 0);
    return emptyKey;
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Key key = (Key) o;
    return id == key.id && Objects.equals(name, key.name);
  }

  public String getItemType() {
    return "key";
  }

  @Override
  public void getLootedBy(LootingVisitor lootingVisitor) {
    lootingVisitor.loot(this);
  }

  @Override
  public int getPrice() {
    return KEY_PRICE;
  }

  public String getName() {
    return name;
  }
}
