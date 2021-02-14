package com.world.navigator.util;

import com.world.navigator.game.playercommands.*;

import java.util.HashMap;

public class PlayerCommandSet {
  private static PlayerCommandSet commandSet;
  private final HashMap<String, PlayerCommand> commands;

  private PlayerCommandSet() {
    commands = new HashMap<>();
    initializeCommands();
  }

  public static PlayerCommandSet getInstance() {
    if (commandSet == null) {
      commandSet = new PlayerCommandSet();
    }
    return commandSet;
  }

  private void initializeCommands() {
    insertCommand(new BuyItemPlayerCommand());
    insertCommand(new CheckPlayerCommand());
    insertCommand(new ExitTradePlayerCommand());
    insertCommand(new LookPlayerCommand());
    insertCommand(new MoveBackwardPlayerCommand());
    insertCommand(new MoveForwardPlayerCommand());
    insertCommand(new SellItemPlayerCommand());
    insertCommand(new StatusPlayerCommand());
    insertCommand(new SwitchLightsPlayerCommand());
    insertCommand(new TradePlayerCommand());
    insertCommand(new TurnLeftPlayerCommand());
    insertCommand(new TurnRightPlayerCommand());
    insertCommand(new UseFlashlightPlayerCommand());
    insertCommand(new UseKeyPlayerCommand());
    insertCommand(new SubmitPlayerCommand());
  }

  private void insertCommand(PlayerCommand command) {
    commands.put(command.getName(), command);
  }

  public PlayerCommand getCommand(String commandName) {
    return commands.get(commandName);
  }
}
