package com.world.navigator.game.player;

public class StateManager {
  private PlayerState playerState;

  public StateManager() {
    playerState = PlayerState.WAITING;
  }

  public void navigating() {
    setState(PlayerState.NAVIGATING);
  }

  public void fighting() {
    setState(PlayerState.FIGHTING);
  }

  public void trading() {
    setState(PlayerState.TRADING);
  }

  public void finish() {
    setState(PlayerState.FINISHED);
  }

  public boolean isNavigating() {
    return checkState(PlayerState.NAVIGATING);
  }

  public boolean isFighting() {
    return checkState(PlayerState.FIGHTING);
  }

  public boolean isTrading() {
    return checkState(PlayerState.TRADING);
  }

  public boolean isFinished() {
    return checkState(PlayerState.FINISHED);
  }

  private synchronized boolean checkState(PlayerState playerState) {
    return this.playerState.equals(playerState);
  }

  private synchronized void setState(PlayerState playerState) {
    this.playerState = playerState;
  }
}
