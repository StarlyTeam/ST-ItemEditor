package net.starly.itemeditor.command.handlers;

import net.starly.itemeditor.command.SubCommandImpl;
import net.starly.itemeditor.util.ItemEditUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static net.starly.itemeditor.ItemEditorMain.msgConfig;

public class LoreCmd implements SubCommandImpl {
    private static LoreCmd instance;

    private LoreCmd() {}

    public static LoreCmd getInstance() {
        if (instance == null) {
            synchronized (LoreCmd.class) {
                instance = new LoreCmd();
            }
        }

        return instance;
    }

    @Override
    public boolean executeCommand(Player player, Command cmd, String label, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!player.hasPermission("starly.itemeditor.command.lore")) {
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

                if (itemStack.getItemMeta().hasLore()) {
                    String list = String.join(msgConfig.getString("messages.loreList.delimiter"), itemStack.getItemMeta().getLore());
                    msgConfig.getMessages("messages.loreList.list").forEach(line -> player.sendMessage(line.replace("{list}", list)));
                } else {
                    msgConfig.getMessages("messages.loreList.list").forEach(line -> player.sendMessage(line.replace("{list}", "없음")));
                }
                return true;
            }

            case "추가":
            case "add": {
                if (args.length == 2) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.noLoreToAdd"));
                    return true;
                }

                String lore = ChatColor.translateAlternateColorCodes('&', "&r&f" + String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().addLore(itemStack, lore));

                player.sendMessage(msgConfig.getMessage("messages.loreAdded").replace("{lore}", lore).replace("{line}", (itemStack.getItemMeta().hasLore() ? itemStack.getItemMeta().getLore().size() : 1) + ""));
                return true;
            }

            case "수정":
            case "edit": {
                if (args.length == 2) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.noLoreLineToEdit"));
                    return true;
                } else if (args.length == 3) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.noLoreToEdit"));
                    return true;
                }

                int index;
                try {
                    index = Integer.parseInt(args[2]) - 1;

                    if (index < 0 || !itemStack.getItemMeta().hasLore() || index >= itemStack.getItemMeta().getLore().size()) {
                        player.sendMessage(msgConfig.getMessage("errorMessages.loreToEditNotExist"));
                        return true;
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.loreLineMustBeNumber"));
                    return true;
                }
                String lore = ChatColor.translateAlternateColorCodes('&', "&r&f" + String.join(" ", Arrays.copyOfRange(args, 3, args.length)));
                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().editLoreAt(itemStack, index, lore));

                player.sendMessage(msgConfig.getMessage("messages.loreEdited").replace("{lore}", lore).replace("{line}", args[2]));
                return true;
            }

            case "삭제":
            case "remove": {
                if (args.length != 3) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                    return true;
                }

                int index;
                try {
                    index = Integer.parseInt(args[2]) - 1;

                    if (index < 0 || !itemStack.getItemMeta().hasLore() || index >= itemStack.getItemMeta().getLore().size()) {
                        player.sendMessage(msgConfig.getMessage("errorMessages.loreToRemoveNotExist"));
                        return true;
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.loreLineMustBeNumber"));
                    return true;
                }

                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().removeLoreAt(itemStack, index));

                player.sendMessage(msgConfig.getMessage("messages.loreRemoved").replace("{line}", (index + 1) + ""));
                return true;
            }

            case "초기화":
            case "clear": {
                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().setLore(itemStack, null));

                player.sendMessage(msgConfig.getMessage("messages.loreCleared"));
                return true;
            }

            default: {
                player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                return true;
            }
        }
    }
}