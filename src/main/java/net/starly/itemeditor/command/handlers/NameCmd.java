package net.starly.itemeditor.command.handlers;

import net.starly.itemeditor.command.SubCommandImpl;
import net.starly.itemeditor.util.ItemEditUtil;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

import static net.starly.itemeditor.ItemEditorMain.msgConfig;

public class NameCmd implements SubCommandImpl {
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
        if (!player.hasPermission("starly.itemeditor.command.name")) {
            player.sendMessage(msgConfig.getMessage("errorMessages.noPermission"));
            return true;
        } else if (args.length == 1) {
            player.sendMessage(msgConfig.getMessage("errorMessages.noName"));
            return true;
        }

        String name = "§r" + Arrays.stream(Arrays.copyOfRange(args, 1, args.length)).map(s -> s.replace("&", "§")).collect(Collectors.joining(" "));
        player.getInventory().setItemInMainHand(ItemEditUtil.getInstance().setDisplayName(itemStack, name));

        player.sendMessage(msgConfig.getMessage("messages.nameChanged").replace("{name}", name));
        return true;
    }
}