package com.world.navigator.game.player;

import com.world.navigator.game.Direction;
import com.world.navigator.game.entities.Checkable;
import com.world.navigator.game.entities.Room;
import com.world.navigator.game.entities.Trader;
import com.world.navigator.game.playeritems.Flashlight;
import com.world.navigator.game.playeritems.InventoryItem;

public class PlayerResponseFactory {
  private static final PlayerResponseFactory INSTANCE = new PlayerResponseFactory();

  private PlayerResponseFactory() {}

  public static PlayerResponseFactory getInstance() {
    return INSTANCE;
  }

  public PlayerResponse createSuccessfulResponse(String requestType) {
    JsonPlayerResponse response = new JsonPlayerResponse("response", true);
    response.put("requestType", requestType);
    return response;
  }

  public PlayerResponse createFailedResponse(String requestType, String cause) {
    JsonPlayerResponse response = new JsonPlayerResponse("response", false);
    response.put("requestType", requestType);
    response.put("cause", cause);
    return response;
  }

  public PlayerResponse createSuccessfulUseKeyResponse() {
    return createSuccessfulResponse("useKey");
  }

  public PlayerResponse createFailedUseKeyResponse(String cause) {
    return createFailedResponse("useKey", cause);
  }

  public PlayerResponse createSuccessfulMoveResponse(Room nextRoom) {
    PlayerResponse response = createSuccessfulResponse("move");
    response.put("nextRoom", nextRoom);
    return response;
  }

  public PlayerResponse createFailedMoveResponse(String cause) {
    return createFailedResponse("move", cause);
  }

  public PlayerResponse createSuccessfulUseFlashlightResponse(Flashlight flashlight) {
    PlayerResponse response = createSuccessfulResponse("useFlashlight");
    response.put("flashlight", flashlight);
    return response;
  }

  public PlayerResponse createFailedUseFlashlightResponse(String cause) {
    return createFailedResponse("useFlashlight", cause);
  }

  public PlayerResponse createSuccessfulSwitchLightsResponse(boolean isLit) {
    PlayerResponse response = createSuccessfulResponse("switchLights");
    response.put("isLit", isLit);
    return response;
  }

  public PlayerResponse createFailedSwitchLightsResponse(String cause) {
    return createFailedResponse("switchLights", cause);
  }

  public PlayerResponse createSuccessfulCheckResponse(Checkable checkable) {
    PlayerResponse response = createSuccessfulResponse("check");
    response.put("checkedItem", checkable);
    return response;
  }

  public PlayerResponse createFailedCheckResponse(String cause) {
    return createFailedResponse("check", cause);
  }

  public PlayerResponse createSuccessfulLookResponse(String itemType) {
    PlayerResponse response = createSuccessfulResponse("look");
    response.put("itemType", itemType);
    return response;
  }

  public PlayerResponse createFailedLookResponse(String cause) {
    return createFailedResponse("look", cause);
  }

  public PlayerResponse createSuccessfulTurnRightResponse(Direction facingDirection) {
    PlayerResponse response = createSuccessfulResponse("turn");
    response.put("turnDirection", "right");
    response.put("facingDirection", facingDirection.name());
    return response;
  }

  public PlayerResponse createSuccessfulTurnLeftResponse(Direction facingDirection) {
    PlayerResponse response = createSuccessfulResponse("turn");
    response.put("turnDirection", "left");
    response.put("facingDirection", facingDirection.name());
    return response;
  }

  public PlayerResponse createFailedTurnResponse(String cause) {
    return createFailedResponse("turn", cause);
  }

  public PlayerResponse createSuccessfulTradeResponse(Trader trader) {
    PlayerResponse response = createSuccessfulResponse("trade");
    response.put("trader", trader);
    return response;
  }

  public PlayerResponse createFailedTradeResponse(String cause) {
    return createFailedResponse("trade", cause);
  }

  public PlayerResponse createSuccessfulBuyResponse(InventoryItem boughtItem) {
    PlayerResponse response = createSuccessfulResponse("buy");
    response.put("item", boughtItem);
    return response;
  }

  public PlayerResponse createFailedBuyResponse(String cause) {
    return createFailedResponse("buy", cause);
  }

  public PlayerResponse createSuccessfulSellResponse(InventoryItem soldItem) {
    PlayerResponse response = createSuccessfulResponse("sell");
    response.put("item", soldItem);
    return response;
  }

  public PlayerResponse createFailedSellResponse(String cause) {
    return createFailedResponse("sell", cause);
  }

  public PlayerResponse createSuccessfulExitTradeResponse() {
    return createSuccessfulResponse("exitTrade");
  }

  public PlayerResponse createFailedExitTradeResponse(String cause) {
    return createFailedResponse("exitTrade", cause);
  }

  public PlayerResponse createSuccessfulStatusResponse(Inventory inventory, Location location) {
    PlayerResponse status = createSuccessfulResponse("status");
    status.put("facingDirection", location.getFacingDirection().name());
    status.put("currentRoom", location.getCurrentRoom());

    for (InventoryItem item : inventory.getItems()) {
      status.appendItemTo("items", item);
    }
    return status;
  }

  public PlayerResponse createFailedStatusResponse(String cause) {
    return createFailedResponse("status", cause);
  }

  public PlayerResponse createSuccessfulSubmitResponse() {
    return createSuccessfulResponse("submit");
  }

  public PlayerResponse createFailedSubmitResponse(String cause) {
    return createFailedResponse("submit", cause);
  }
}
