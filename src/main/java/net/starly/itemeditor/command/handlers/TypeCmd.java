package net.starly.itemeditor.command.handlers;

import net.starly.core.data.Config;
import net.starly.itemeditor.command.SubCommandImpl;
import net.starly.itemeditor.context.MessageContent;
import net.starly.itemeditor.util.ItemEditUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TypeCmd implements SubCommandImpl {
    private static TypeCmd instance;

    private TypeCmd() {}

    public static TypeCmd getInstance() {
        if (instance == null) {
            synchronized (TypeCmd.class) {
                instance = new TypeCmd();
            }
        }

        return instance;
    }

    @Override
    public boolean executeCommand(Player player, Command cmd, String label, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Config msgConfig = MessageContent.getInstance().getConfig();
        if (!player.hasPermission("starly.itemeditor.command.type")) {
            player.sendMessage(msgConfig.getMessage("errorMessages.noPermission"));
            return true;
        } else if (args.length == 1) {
            player.sendMessage(msgConfig.getMessage("errorMessages.noType"));
            return true;
        } else if (args.length != 2) {
            player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
            return true;
        }

        Material type;
        try {
            type = Material.valueOf(args[1].toUpperCase());
        } catch (Exception e) {
            player.sendMessage(msgConfig.getMessage("errorMessages.typeNotExist").replace("{type}", args[1].toUpperCase()));
            return true;
        }
        player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().setType(itemStack, type));

        player.sendMessage(msgConfig.getMessage("messages.typeChanged").replace("{type}", type.name()));
        return true;
    }
}