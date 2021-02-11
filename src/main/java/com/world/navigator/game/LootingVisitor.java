package com.world.navigator.game;

import com.world.navigator.game.playeritems.Flashlight;
import com.world.navigator.game.playeritems.GoldBag;
import com.world.navigator.game.playeritems.Key;

public interface LootingVisitor {
  void loot(Key key);

  void loot(Flashlight flashlight);

  void loot(GoldBag goldBag);
}
