package net.starly.itemeditor.command;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public interface SubCommand {
    boolean executeCommand(Player player, Command cmd, String label, String[] args);
}
