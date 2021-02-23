package com.world.navigator.game.player;

public class JsonPlayerEventFactory {
    private static final JsonPlayerEventFactory INSTANCE = new JsonPlayerEventFactory();

    private JsonPlayerEventFactory() {
    }

    public static JsonPlayerEventFactory getInstance() {
        return INSTANCE;
    }

    public PlayerEvent createWinEvent() {
        PlayerEvent event = new JsonPlayerEvent("gameEnd");
        event.put("state", "win");
    return event;
  }

  public PlayerEvent createLostEvent() {
    PlayerEvent event = new JsonPlayerEvent("gameEnd");
    event.put("state", "lose");
    return event;
  }

  public PlayerEvent createGameStatEvent() {
    return new JsonPlayerEvent("gameStart");
  }

  public PlayerEvent createFightEvent(String message, String[] expectedAnswers) {
    PlayerEvent event = new JsonPlayerEvent("fightStarted");
    event.put("message", message);
    event.put("expectedAnswers", expectedAnswers);
    return event;
  }

  public PlayerEvent createFightWonEvent() {
    PlayerEvent event = new JsonPlayerEvent("fightFinished");
    event.put("state", "won");
    return event;
  }
}
