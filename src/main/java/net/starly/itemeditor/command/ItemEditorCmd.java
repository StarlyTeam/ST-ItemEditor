package net.starly.itemeditor.command;

import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import net.starly.itemeditor.command.handlers.*;
import net.starly.itemeditor.util.ItemEditUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static net.starly.itemeditor.ItemEditorMain.msgConfig;

@SuppressWarnings("all")
public class ItemEditorCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(msgConfig.getMessage("errorMessages.onlyPlayer"));
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            printHelpMessage(player);
            return true;
        }

        if (Arrays.asList("리로드", "reload").contains(args[0].toLowerCase())) {
            if (!player.hasPermission("starly.itemeditor.reload")) {
                player.sendMessage(msgConfig.getMessage("errorMessages.noPermission"));
                return true;
            }

            msgConfig.reloadConfig();

            player.sendMessage(msgConfig.getMessage("messages.reload"));
            return true;
        } else if (Arrays.asList("도움말", "help", "?").contains(args[0].toLowerCase())) {
            printHelpMessage(player);
            return true;
        } else {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                player.sendMessage(msgConfig.getMessage("errorMessages.noItemOnHand"));
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "이름":
                case "name": {
                    return NameCmd.getInstance().executeCommand(player, cmd, label, args);
                }

                case "커스텀모델데이터":
                case "cid": {
                    return CidCmd.getInstance().executeCommand(player, cmd, label, args);
                }

                case "타입":
                case "type": {
                    return TypeCmd.getInstance().executeCommand(player, cmd, label, args);
                }

                case "로어":
                case "lore": {
                    return LoreCmd.getInstance().executeCommand(player, cmd, label, args);
                }

                case "인챈트":
                case "enchantment": {
                    return EnchantmentCmd.getInstance().executeCommand(player, cmd, label, args);
                }

                case "플래그":
                case "flag": {
                    return FlagCmd.getInstance().executeCommand(player, cmd, label, args);
                }

                case "태그":
                case "nbt": {
                    return NBTCmd.getInstance().executeCommand(player, cmd, label, args);
                }

                default: {
                    player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                    return true;
                }
            }
        }
    }

    private void printHelpMessage(CommandSender sender) {
        if (!sender.hasPermission("starly.itemeditor.help")) {
            sender.sendMessage(msgConfig.getMessage("errorMessages.noPermission"));
            return;
        }

        msgConfig.getMessages("messages.help").forEach(sender::sendMessage);
    }
}
