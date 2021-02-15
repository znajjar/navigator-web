package com.world.navigator.game.entities;

public class Wall implements Observable {

  @Override
  public String look() {
    return "wall";
  }
}
