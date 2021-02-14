package com.world.navigator.game.player;

import com.world.navigator.game.playercommands.PlayerCommand;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Log4j2
public class PlayerController implements Runnable {
  public static final int QUEUE_CAPACITY = 100;
  private final BlockingQueue<QueuedCommand> commandsQueue;
  private final Player player;

  public PlayerController(Player player) {
    this.player = player;
    commandsQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
  }

  public void addListener(PlayerEventListener listener) {
    player.listeners().addListener(listener);
  }

  public void removeListener(PlayerEventListener listener) {
    player.listeners().removeListener(listener);
  }

  public void queueCommand(PlayerCommand command, String[] args) {
    try {
      commandsQueue.put(new QueuedCommand(command, args));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    while (!commandsQueue.isEmpty()) {
      try {
        QueuedCommand queuedCommand = commandsQueue.take();
        PlayerCommand command = queuedCommand.command;
        String[] args = queuedCommand.args;
        command.execute(player, args);
      } catch (InterruptedException e) {
        log.warn("player command execution was interrupted");
      } catch (Exception e) {
        log.warn("exception while executing player command:\n" + e.toString());
      }
    }
  }

  private static class QueuedCommand {
    final PlayerCommand command;
    final String[] args;

    private QueuedCommand(PlayerCommand command, String[] args) {
      this.command = command;
      this.args = args;
    }
  }
}
