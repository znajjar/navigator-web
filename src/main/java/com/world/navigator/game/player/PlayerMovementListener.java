package com.world.navigator.game.player;

import com.world.navigator.game.entities.Room;

public interface PlayerMovementListener {
    void onMove(int playerId, Room nextRoom);
}
