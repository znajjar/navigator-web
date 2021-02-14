package com.world.navigator.game.player;

import org.json.JSONObject;

public class JsonPlayerEvent implements PlayerEvent {
  private final JSONObject json;
  private final String eventType;

  public JsonPlayerEvent(String eventType) {
    json = new JSONObject();
    this.eventType = eventType;
  }

  @Override
  public String toString() {
    json.put("type", "event");
    json.put("eventType", eventType);
    return json.toString();
  }

  @Override
  public String getEventType() {
    return eventType;
  }

  @Override
  public void put(String key, String value) {
    json.put(key, value);
  }

  @Override
  public void put(String key, String[] value) {
    json.put(key, value);
  }
}
