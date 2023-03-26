package net.starly.itemeditor.command.handlers;

import net.starly.core.data.Config;
import net.starly.itemeditor.command.SubCommandImpl;
import net.starly.itemeditor.context.MessageContent;
import net.starly.itemeditor.util.ItemEditUtil;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class EnchantmentCmd implements SubCommandImpl {
    private static EnchantmentCmd instance;

    private EnchantmentCmd() {
    }

    public static EnchantmentCmd getInstance() {
        if (instance == null) {
            synchronized (EnchantmentCmd.class) {
                instance = new EnchantmentCmd();
            }
        }

        return instance;
    }

    @Override
    public boolean executeCommand(Player player, Command cmd, String label, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Config msgConfig = MessageContent.getInstance().getConfig();
        if (!player.hasPermission("starly.itemeditor.command.enchant")) {
            player.sendMessage(msgConfig.getMessage("errorMessages.noPermission"));
            return true;
        } else if (args.length == 1) {
            player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "보기":
            case "view": {
                if (args.length != 2) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                    return true;
                }

                if (itemStack.getItemMeta().getEnchants().isEmpty()) {
                    msgConfig.getMessages("messages.enchantmentList.list").forEach(line -> player.sendMessage(line.replace("{list}", "없음")));
                } else {
                    String list = itemStack
                            .getItemMeta()
                            .getEnchants()
                            .keySet()
                            .stream()
                            .map(s -> ChatColor
                                    .translateAlternateColorCodes('&',
                                            msgConfig
                                                    .getString("messages.flagList.item")
                                                    .replace("{enchant}", s.getName())
                                                    .replace("{level}", itemStack.getEnchantmentLevel(s) + "")))
                            .collect(Collectors.joining(msgConfig.getString("messages.flagList.delimiter")));
                    msgConfig.getMessages("messages.enchantmentList.list").forEach(line -> player.sendMessage(line.replace("{list}", list)));
                }
                return true;
            }

            case "추가":
            case "add": {
                if (args.length == 2) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.noEnchantmentToAdd"));
                    return true;
                } else if (args.length == 3) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.noEnchantmentLevelToAdd"));
                    return true;
                }

                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[2]));
                if (enchantment == null) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.enchantmentNotExist"));
                    return true;
                }
                if (itemStack.getItemMeta().hasEnchant(enchantment)) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.enchantmentAlreadyExist"));
                    return true;
                }
                int level;
                try {
                    level = Integer.parseInt(args[3]);

                    if (level < 1) {
                        player.sendMessage(msgConfig.getMessage("errorMessages.enchantmentLevelMustBiggerThan1"));
                        return true;
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.enchantmentLevelMustBeNumber"));
                    return true;
                }
                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().addEnchantment(itemStack, enchantment, level));

                player.sendMessage(msgConfig.getMessage("messages.enchantmentAdded").replace("{enchant}", enchantment.getKey().getKey()).replace("{level}", level + ""));
                return true;
            }

            case "삭제":
            case "remove": {
                if (args.length == 2) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.noEnchantmentToRemove"));
                    return true;
                } else if (args.length != 3) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                    return true;
                }

                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[2]));
                if (enchantment == null) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.enchantmentNotExist"));
                    return true;
                }
                if (!itemStack.getItemMeta().getEnchants().containsKey(enchantment)) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.enchantmentAlreadyNotAdded"));
                    return true;
                }
                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().removeEnchantment(itemStack, enchantment));

                player.sendMessage(msgConfig.getMessage("messages.enchantmentRemoved").replace("{enchant}", enchantment.getKey().getKey()));
                return true;
            }

            case "초기화":
            case "clear": {
                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().clearEnchantments(itemStack));

                player.sendMessage(msgConfig.getMessage("messages.enchantmentCleared"));
                return true;
            }

            default: {
                player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                return true;
            }
        }
    }
}