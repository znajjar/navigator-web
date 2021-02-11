package com.world.navigator.game.player;

import com.world.navigator.game.Direction;
import com.world.navigator.game.exceptions.ItemIsLockedException;
import com.world.navigator.game.mapitems.PassThrough;
import com.world.navigator.game.mapitems.Room;
import com.world.navigator.game.playeritems.GoldBag;
import com.world.navigator.game.playeritems.InventoryItem;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Player {
  private static final PlayerEventFactory EVENT_FACTORY = PlayerEventFactory.getInstance();
  private final CopyOnWriteArrayList<PlayerEvenListener> listeners;
  private final NavigationManager navigationManager;
  private final CheckingManager checkingManager;
  private final InteractionManager interactionManager;
  private final TradingManager tradingManager;
  private final Inventory inventory;
  private final int id;
  private final Location location;
  private final String name;
  private PlayerState state;

  public Player(int id, String name) {
    this.id = id;
    this.name = name;
    location = new Location(Direction.NORTH, Room.getEmptyRoom());
    inventory = new Inventory();
    inventory.loot(new GoldBag());
    navigationManager = new NavigationManager(inventory, location);
    checkingManager = new CheckingManager(inventory, location);
    interactionManager = new InteractionManager(inventory, location);
    tradingManager = new TradingManager(inventory, location);
    listeners = new CopyOnWriteArrayList<>();
    state = PlayerState.WAITING;
  }

  public synchronized PlayerState getState() {
    return state;
  }

  public synchronized void setState(PlayerState state) {
    this.state = state;
  }

  public void addListener(PlayerEvenListener listener) {
    listeners.add(listener);
  }

  public void notifyListeners(PlayerEvent event) {
    for (PlayerEvenListener listener : listeners) {
      listener.onEvent(this, event);
    }
  }

  public int getID() {
    return id;
  }

  public PlayerEvent getStatus() {
    PlayerEvent status = EVENT_FACTORY.createSuccessfulResponse("status");

    status.put("facingDirection", location.getFacingDirection().name());
    status.put("currentRoom", location.getCurrentRoom());

    for (InventoryItem item : inventory.getItems()) {
      status.appendItemTo("items", item);
    }

    return status;
  }

  public PlayerEvent turnRight() {
    return navigationManager.turnRight();
  }

  public PlayerEvent turnLeft() {
    return navigationManager.turnLeft();
  }

  public PlayerEvent moveForward() {
    if (navigationManager.isPassThroughInFront()) {
      return moveThrough(navigationManager.getPassThroughInFront());
    } else {
      return EVENT_FACTORY.createFailedMoveResponse("You can't move through item in front.");
    }
  }

  public PlayerEvent moveBackward() {
    if (navigationManager.isPassThroughBehind()) {
      return moveThrough(navigationManager.getPassThroughBehind());
    } else {
      return EVENT_FACTORY.createFailedMoveResponse("You can't move through item behind.");
    }
  }

  private PlayerEvent moveThrough(PassThrough passThrough) {
    try {
      Room nextRoom = requestMove(passThrough);
      navigationManager.moveTo(nextRoom);
      checkingManager.checkFloor();
      return EVENT_FACTORY.createSuccessfulMoveResponse(nextRoom);
    } catch (ItemIsLockedException e) {
      return EVENT_FACTORY.createFailedMoveResponse("Door is locked.");
    }
  }

  public void moveTo(Room room) {
    navigationManager.moveTo(room);
  }

  public abstract Room requestMove(PassThrough passThrough) throws ItemIsLockedException;

  public PlayerEvent look() {
    return navigationManager.look();
  }

  public PlayerEvent check() {
    return checkingManager.checkItemInFront();
  }

  public PlayerEvent useKey(String keyName) {
    return interactionManager.useKey(keyName);
  }

  public PlayerEvent useFlashlight() {
    return interactionManager.useFlashlight();
  }

  public PlayerEvent switchRoomLights() {
    return interactionManager.switchRoomLights();
  }

  public PlayerEvent trade() {
    return tradingManager.trade();
  }

  public PlayerEvent buy(String itemName) {
    return tradingManager.buy(itemName);
  }

  public PlayerEvent sell(String itemName) {
    return tradingManager.sell(itemName);
  }

  public PlayerEvent exitTrade() {
    return EVENT_FACTORY.createSuccessfulExitTradeResponse();
  }

  public void takeGold(GoldBag goldBag) {
    goldBag.getLootedBy(inventory);
  }

  public Room getCurrentRoom() {
    return location.getCurrentRoom();
  }

  public String getName() {
    return name;
  }

  public void startNavigating() {
    setState(PlayerState.NAVIGATING);
    notifyListeners(EVENT_FACTORY.createGameStartedEvent());
  }

  public void winGame() {
    setState(PlayerState.FINISHED);
    notifyListeners(EVENT_FACTORY.createGameWonEvent());
  }

  public void loseGame() {
    setState(PlayerState.FINISHED);
    notifyListeners(EVENT_FACTORY.createGameLostEvent());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Player) {
      return id == ((Player) obj).getID();
    } else {
      return false;
    }
  }
}
