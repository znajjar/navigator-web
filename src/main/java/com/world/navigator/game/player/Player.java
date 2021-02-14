package com.world.navigator.game.player;

import com.world.navigator.game.Direction;
import com.world.navigator.game.exceptions.ItemIsLockedException;
import com.world.navigator.game.mapitems.PassThrough;
import com.world.navigator.game.mapitems.Room;
import com.world.navigator.game.playeritems.GoldBag;

import java.util.Objects;

public abstract class Player {
  private static final PlayerResponseFactory RESPONSE_FACTORY = PlayerResponseFactory.getInstance();
  private static final PlayerEventFactory EVENT_FACTORY = PlayerEventFactory.getInstance();
  private static final String CANNOT_MOVE_THROUGH_ITEM = "You can't move through item";
  private final ListenersManager listenersManager;
  private final InteractionManager interactionManager;
  private final NavigationManager navigationManager;
  private final CheckingManager checkingManager;
  private final FightingManager fightingManager;
  private final TradingManager tradingManager;
  private final StateManager stateManager;
  private final Inventory inventory;
  private final Location location;
  private final String name;
  private final int id;

  public Player(int id, String name) {
    this.id = id;
    this.name = name;
    location = new Location(Direction.NORTH, Room.getEmptyRoom());
    inventory = new Inventory();
    inventory.takeItem(new GoldBag());
    interactionManager = new InteractionManager(inventory, location);
    navigationManager = new NavigationManager(inventory, location);
    checkingManager = new CheckingManager(inventory, location);
    tradingManager = new TradingManager(inventory, location);
    listenersManager = new ListenersManager(name);
    stateManager = new StateManager();
    fightingManager = new FightingManager(listenersManager, stateManager, id);
  }

  public int getID() {
    return id;
  }

  public PlayerResponse getStatus() {
    return RESPONSE_FACTORY.createSuccessfulStatusResponse(inventory, location);
  }

  public PlayerResponse moveForward() {
    if (navigationManager.isPassThroughInFront()) {
      return moveThrough(navigationManager.getPassThroughInFront());
    } else {
      return RESPONSE_FACTORY.createFailedMoveResponse(CANNOT_MOVE_THROUGH_ITEM);
    }
  }

  public PlayerResponse moveBackward() {
    if (navigationManager.isPassThroughBehind()) {
      return moveThrough(navigationManager.getPassThroughBehind());
    } else {
      return RESPONSE_FACTORY.createFailedMoveResponse(CANNOT_MOVE_THROUGH_ITEM);
    }
  }

  private PlayerResponse moveThrough(PassThrough passThrough) {
    try {
      Room nextRoom = requestMove(passThrough);
      navigationManager.moveTo(nextRoom);
      checkingManager.checkFloor();
      return RESPONSE_FACTORY.createSuccessfulMoveResponse(nextRoom);
    } catch (ItemIsLockedException e) {
      return RESPONSE_FACTORY.createFailedMoveResponse("Door is locked.");
    }
  }

  public abstract Room requestMove(PassThrough passThrough) throws ItemIsLockedException;

  public NavigationManager navigate() {
    return navigationManager;
  }

  public CheckingManager check() {
    return checkingManager;
  }

  public InteractionManager interact() {
    return interactionManager;
  }

  public TradingManager trade() {
    return tradingManager;
  }

  public FightingManager fight() {
    return fightingManager;
  }

  public Inventory loot() {
    return inventory;
  }

  public ListenersManager listeners() {
    return listenersManager;
  }

  public StateManager state() {
    return stateManager;
  }

  public String getName() {
    return name;
  }

  public void startNavigating() {
    stateManager.navigating();
    listenersManager.notifyListenersOnEvent(EVENT_FACTORY.createGameStatEvent());
  }

  public void winGame() {
    stateManager.finish();
    listenersManager.notifyListenersOnEvent(EVENT_FACTORY.createWinEvent());
  }

  public void loseGame() {
    stateManager.finish();
    listenersManager.notifyListenersOnEvent(EVENT_FACTORY.createLostEvent());
  }

  public void getIntoFight() {
    stateManager.fighting();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }

    Player player = (Player) obj;
    return player.id == id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
