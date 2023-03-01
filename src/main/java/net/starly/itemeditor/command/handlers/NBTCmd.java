package net.starly.itemeditor.command.handlers;

import net.starly.itemeditor.command.SubCommandImpl;
import net.starly.itemeditor.util.ClassUtil;
import net.starly.itemeditor.util.ItemEditUtil;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

import static net.starly.itemeditor.ItemEditorMain.msgConfig;

public class NBTCmd implements SubCommandImpl {
    private static NBTCmd instance;

    private NBTCmd() {
    }

    public static NBTCmd getInstance() {
        if (instance == null) {
            synchronized (NBTCmd.class) {
                instance = new NBTCmd();
            }
        }

        return instance;
    }

    @Override
    public boolean executeCommand(Player player, Command cmd, String label, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!player.hasPermission("starly.itemeditor.command.nbt")) {
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

                try {
                    Class<?> craftStackClass = ClassUtil.getInstance().getCraftStackClass();
                    Method asNMSCopy = ClassUtil.getInstance().getAsNMSCopy(), getTag = ClassUtil.getInstance().getGetTag(), getString = ClassUtil.getInstance().getGetString(), getKeys = ClassUtil.getInstance().getGetKeys();

                    Object nmsStack = asNMSCopy.invoke(craftStackClass, itemStack), nbtTagCompound = getTag.invoke(nmsStack);

                    Set<String> keys = (Set<String>) getKeys.invoke(nbtTagCompound);
                    if (keys.isEmpty()) {
                        msgConfig.getMessages("messages.nbtList.list").forEach(line -> player.sendMessage(line.replace("{list}", "없음")));
                    } else {
                        String list = keys.stream().map(key -> {
                            try {
                                return msgConfig.getString("messages.nbtList.item").replace("{key}", key).replace("{value}", (String) getString.invoke(nbtTagCompound, key)).replace("&", "§");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                return null;
                            }
                        }).collect(Collectors.joining(msgConfig.getString("messages.nbtList.delimiter")));
                        msgConfig.getMessages("messages.nbtList.list").forEach(line -> player.sendMessage(line.replace("{list}", list)));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            }

            case "설정":
            case "set": {
                if (args.length == 2) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.noKeyToSet"));
                    return true;
                } else if (args.length == 3) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.noValueToSet"));
                    return true;
                } else if (args.length != 4) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                    return true;
                }

                String key = args[2];
                String value = args[3];


                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().setNBTTag(itemStack, key, value));

                player.sendMessage(msgConfig.getMessage("messages.nbtSet").replace("{key}", key).replace("{value}", value));
                return true;
            }

            case "삭제":
            case "remove": {
                if (args.length == 2) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.noKeyToRemove"));
                    return true;
                } else if (args.length != 3) {
                    player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                    return true;
                }

                String key = args[2];


                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().removeNBTTag(itemStack, key));

                player.sendMessage(msgConfig.getMessage("messages.nbtRemoved").replace("{key}", key));
                return true;
            }

            case "초기화":
            case "clear": {
                player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().clearNBTTag(itemStack));

                player.sendMessage(msgConfig.getMessage("messages.nbtCleared"));
                return true;
            }

            default: {
                player.sendMessage(msgConfig.getMessage("errorMessages.wrongCommand"));
                return true;
            }
        }
    }
}