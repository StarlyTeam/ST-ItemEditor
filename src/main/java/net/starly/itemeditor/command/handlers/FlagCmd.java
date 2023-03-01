package net.starly.itemeditor.command.handlers;

import net.starly.itemeditor.command.SubCommandImpl;
import net.starly.itemeditor.util.ItemEditUtil;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

import static net.starly.itemeditor.ItemEditorMain.msgConfig;

public class FlagCmd implements SubCommandImpl {
    private static FlagCmd instance;

    private FlagCmd() {}

    public static FlagCmd getInstance() {
        if (instance == null) {
            synchronized (FlagCmd.class) {
                instance = new FlagCmd();
            }
        }

        return instance;
    }

    @Override
    public boolean executeCommand(Player player, Command cmd, String label, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!player.hasPermission("starly.itemeditor.command.flag")) {
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

                if (itemStack.getItemMeta().getItemFlags().isEmpty()) {
                    msgConfig.getMessages("messages.flagList.list").forEach(line -> player.sendMessage(line.replace("{list}", "없음")));
                } else {
                    String list = itemStack.getItemMeta().getItemFlags().stream().map(s -> "§a" + s.name()).collect(Collectors.joining(msgConfig.getString("messages.flagList.delimiter")));
                    msgConfig.getMessages("messages.flagList.list").forEach(line -> player.sendMessage(line.replace("{list}", list)));
                }
                return true;
            }

            case "추가":
            case "add": {
                if (args.length == 2) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.noFlagToAdd"));
                    return true;
                } else if (args.length != 3) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                    return true;
                }

                ItemFlag itemFlag;
                try {
                    itemFlag = ItemFlag.valueOf(args[2].toUpperCase());

                    if (itemStack.getItemMeta().hasItemFlag(itemFlag)) {
                        player.sendMessage(msgConfig.getMessage("errorMessages.flagAlreadyExist"));
                        return true;
                    }
                } catch (IllegalArgumentException e) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.flagNotExist"));
                    return true;
                }

                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().addItemFlag(itemStack, itemFlag));

                player.sendMessage(msgConfig.getMessage("messages.flagAdded").replace("{flag}", itemFlag.name()));
                return true;
            }

            case "삭제":
            case "remove": {
                if (args.length == 2) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.noFlagToRemove"));
                    return true;
                } else if (args.length != 3) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                    return true;
                }

                ItemFlag itemFlag;

                try {
                    itemFlag = ItemFlag.valueOf(args[2].toUpperCase());

                    if (!itemStack.getItemMeta().hasItemFlag(itemFlag)) {
                        player.sendMessage(msgConfig.getMessage("errorMessages.flagAlreadyNotExist"));
                        return true;
                    }
                } catch (IllegalArgumentException e) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.flagNotExist"));
                    return true;
                }

                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().removeItemFlag(itemStack, itemFlag));

                player.sendMessage(msgConfig.getMessage("messages.flagRemoved").replace("{flag}", itemFlag.name()));
                return true;
            }

            case "초기화":
            case "clear": {
                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().clearItemFlag(itemStack));

                player.sendMessage(msgConfig.getMessage("messages.flagCleared"));
                return true;
            }

            default: {
                player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                return true;
            }
        }
    }
}