package com.world.navigator.game.entities;

import com.world.navigator.game.playeritems.Key;

public abstract class LockedWithKey {
  private final Lock lock;

  protected LockedWithKey(Lock lock) {
    this.lock = lock;
  }

  protected LockedWithKey(Key key, boolean locked) {
    lock = new Lock(key, locked);
  }

  public void useKey(Key key) {
    lock.use(key);
  }

  public boolean checkKey(Key key) {
    return lock.checkKey(key);
  }

  public boolean isLocked() {
    return lock.isLocked();
  }

  public boolean isUnlocked() {
    return lock.isUnlocked();
  }

  public String getKeyName() {
    return lock.getKeyName();
  }
}
