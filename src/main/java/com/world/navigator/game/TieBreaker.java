package com.world.navigator.game;

import com.world.navigator.game.player.Player;

public interface TieBreaker {
  Player breakTie(Player player1, Player player2);
}
