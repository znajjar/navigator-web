package com.world.navigator.game.entities;

import com.world.navigator.game.exceptions.WrongKeyUsedException;
import com.world.navigator.game.playeritems.Key;

public class Lock {
  private final Key key;
  private boolean locked;

  public Lock(Key key, boolean locked) {
    this.key = key;
    this.locked = locked;
  }

  public synchronized boolean isLocked() {
    return locked;
  }

  public boolean isUnlocked() {
    return !isLocked();
  }

  public synchronized void use(Key key) {
    if (checkKey(key)) {
      locked ^= true;
    } else {
      throw new WrongKeyUsedException();
    }
  }

  public boolean checkKey(Key key) {
    return this.key.equals(key);
  }

  public String getKeyName() {
    return key.getName();
  }
}
