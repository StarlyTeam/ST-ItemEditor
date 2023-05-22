package net.starly.itemeditor.command.subcommand;

import net.starly.core.data.Config;
import net.starly.itemeditor.command.SubCommand;
import net.starly.itemeditor.context.MessageContent;
import net.starly.itemeditor.util.ItemEditUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NameCmd implements SubCommand {
    private static NameCmd instance;

    private NameCmd() {}

    public static NameCmd getInstance() {
        if (instance == null) {
            synchronized (NameCmd.class) {
                instance = new NameCmd();
            }
        }

        return instance;
    }

    @Override
    public boolean executeCommand(Player player, Command cmd, String label, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Config msgConfig = MessageContent.getInstance().getConfig();
        if (!player.hasPermission("starly.itemeditor.command.name")) {
            player.sendMessage(msgConfig.getMessage("errorMessages.noPermission"));
            return true;
        } else if (args.length == 1) {
            player.sendMessage(msgConfig.getMessage("errorMessages.noName"));
            return true;
        }

        String name = "§r" + ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace("\\b", " "));
        player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().setDisplayName(itemStack, name));

        player.sendMessage(msgConfig.getMessage("messages.nameChanged").replace("{name}", name));
        return true;
    }
}