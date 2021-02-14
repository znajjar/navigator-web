package com.world.navigator.game.player;

import com.world.navigator.game.fighting.Fight;

public class FightingManager {
  private static final PlayerResponseFactory RESPONSE_FACTORY = PlayerResponseFactory.getInstance();
  private static final PlayerEventFactory EVENT_FACTORY = PlayerEventFactory.getInstance();
  private final ListenersManager listenersManager;
  private final StateManager stateManager;
  private final int id;
  private Fight currentFight;

  public FightingManager(ListenersManager listenersManager, StateManager stateManager, int id) {
    this.listenersManager = listenersManager;
    this.stateManager = stateManager;
    this.id = id;
  }

  public synchronized Fight setCurrentFightIfAbsent(Fight nextFight) {
    if (currentFight == null) {
      currentFight = nextFight;
      return nextFight;
    } else {
      return currentFight;
    }
  }

  public void getIntoFight() {
    stateManager.fighting();
  }

  public void sendFight(String message, String[] expectedAnswers) {
    listenersManager.notifyListenersOnEvent(
        EVENT_FACTORY.createFightEvent(message, expectedAnswers));
  }

  public PlayerResponse submitAnswer(String answer) {
    if (currentFight.isValidAnswer(answer)) {
      currentFight.submitAnswerFrom(id, answer);
      return RESPONSE_FACTORY.createSuccessfulSubmitResponse();
    } else {
      return RESPONSE_FACTORY.createFailedSubmitResponse("invalid submission");
    }
  }

  public void winFight() {
    stateManager.navigating();
    listenersManager.notifyListenersOnEvent(EVENT_FACTORY.createFightWonEvent());
    endFight();
  }

  public void loseFight() {
    stateManager.finish();
    listenersManager.notifyListenersOnEvent(EVENT_FACTORY.createLostEvent());
    endFight();
  }

  private synchronized void endFight() {
    currentFight = null;
  }
}
