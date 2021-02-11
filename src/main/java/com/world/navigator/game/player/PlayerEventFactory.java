package com.world.navigator.game.player;

import com.world.navigator.game.Direction;
import com.world.navigator.game.JsonPlayerEvent;
import com.world.navigator.game.mapitems.Checkable;
import com.world.navigator.game.mapitems.Room;
import com.world.navigator.game.mapitems.Trader;
import com.world.navigator.game.playeritems.Flashlight;
import com.world.navigator.game.playeritems.InventoryItem;

public class PlayerEventFactory {
  public static final String USE_KEY_TYPE = "useKey";
  public static final String USE_FLASHLIGHT_TYPE = "useFlashlight";
  private static final PlayerEventFactory INSTANCE = new PlayerEventFactory();

  private PlayerEventFactory() {}

  public static PlayerEventFactory getInstance() {
    return INSTANCE;
  }

  public PlayerEvent createSuccessfulResponse(String requestType) {
    JsonPlayerEvent response = new JsonPlayerEvent("response");
    response.put("success", true);
    response.put("requestType", requestType);
    return response;
  }

  public PlayerEvent createFailedResponse(String requestType, String cause) {
    JsonPlayerEvent response = new JsonPlayerEvent("response");
    response.put("success", false);
    response.put("requestType", requestType);
    response.put("cause", cause);
    return response;
  }

  public PlayerEvent createEvent(String type) {
    JsonPlayerEvent event = new JsonPlayerEvent("event");
    event.put("eventType", type);
    return event;
  }

  public PlayerEvent createSuccessfulUseKeyResponse() {
    return createSuccessfulResponse(USE_KEY_TYPE);
  }

  public PlayerEvent createFailedUseKeyResponse(String cause) {
    return createFailedResponse(USE_KEY_TYPE, cause);
  }

  public PlayerEvent createSuccessfulMoveResponse(Room nextRoom) {
    PlayerEvent response = createSuccessfulResponse("move");
    response.put("nextRoom", nextRoom);
    return response;
  }

  public PlayerEvent createFailedMoveResponse(String cause) {
    return createFailedResponse("move", cause);
  }

  public PlayerEvent createSuccessfulUseFlashlightResponse(Flashlight flashlight) {
    PlayerEvent response = createSuccessfulResponse(USE_FLASHLIGHT_TYPE);
    response.put("flashlight", flashlight);
    return response;
  }

  public PlayerEvent createFailedUseFlashlightResponse(String cause) {
    return createFailedResponse(USE_FLASHLIGHT_TYPE, cause);
  }

  public PlayerEvent createSuccessfulSwitchLightsResponse(boolean isLit) {
    PlayerEvent response = createSuccessfulResponse("switchLights");
    response.put("isLit", isLit);
    return response;
  }

  public PlayerEvent createFailedSwitchLightsResponse(String cause) {
    return createFailedResponse("switchLights", cause);
  }

  public PlayerEvent createSuccessfulCheckResponse(Checkable checkable) {
    PlayerEvent response = createSuccessfulResponse("check");
    response.put("checkedItem", checkable);
    return response;
  }

  public PlayerEvent createFailedCheckResponse(String cause) {
    return createFailedResponse("check", cause);
  }

  public PlayerEvent createSuccessfulLookResponse(String itemType) {
    PlayerEvent response = createSuccessfulResponse("look");
    response.put("itemType", itemType);
    return response;
  }

  public PlayerEvent createFailedLookResponse(String cause) {
    return createFailedResponse("look", cause);
  }

  public PlayerEvent createSuccessfulTurnRightResponse(Direction facingDirection) {
    PlayerEvent response = createSuccessfulResponse("turn");
    response.put("turnDirection", "right");
    response.put("facingDirection", facingDirection.name());
    return response;
  }

  public PlayerEvent createSuccessfulTurnLeftResponse(Direction facingDirection) {
    PlayerEvent response = createSuccessfulResponse("turn");
    response.put("turnDirection", "left");
    response.put("facingDirection", facingDirection.name());
    return response;
  }

  public PlayerEvent createFailedTurnResponse(String cause) {
    return createFailedResponse("turn", cause);
  }

  public PlayerEvent createSuccessfulTradeResponse(Trader trader) {
    PlayerEvent response = createSuccessfulResponse("trade");
    response.put("trader", trader);
    return response;
  }

  public PlayerEvent createFailedTradeResponse(String cause) {
    return createFailedResponse("trade", cause);
  }

  public PlayerEvent createSuccessfulBuyResponse(InventoryItem boughtItem) {
    PlayerEvent response = createSuccessfulResponse("buy");
    response.put("item", boughtItem);
    return response;
  }

  public PlayerEvent createFailedBuyResponse(String cause) {
    return createFailedResponse("buy", cause);
  }

  public PlayerEvent createSuccessfulSellResponse(InventoryItem soldItem) {
    PlayerEvent response = createSuccessfulResponse("sell");
    response.put("item", soldItem);
    return response;
  }

  public PlayerEvent createFailedSellResponse(String cause) {
    return createFailedResponse("sell", cause);
  }

  public PlayerEvent createSuccessfulExitTradeResponse() {
    return createSuccessfulResponse("exitTrade");
  }

  public PlayerEvent createFailedExitTradeResponse(String cause) {
    return createFailedResponse("exitTrade", cause);
  }

  public PlayerEvent createFailedStatusResponse(String cause) {
    return createFailedResponse("status", cause);
  }

  public PlayerEvent createSuccessfulStatusResponse() {
    PlayerEvent event = createSuccessfulResponse("status");
    // TODO implement status command
    return event;
  }

  public PlayerEvent createGameStartedEvent() {
    return createEvent("gameStarted");
  }

  public PlayerEvent createGameWonEvent() {
    return createEvent("gameWon");
  }

  public PlayerEvent createGameLostEvent() {
    return createEvent("gameLost");
  }
}
