package net.starly.itemeditor.command;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public interface SubCommandImpl {
    boolean executeCommand(Player player, Command cmd, String label, String[] args);
}
