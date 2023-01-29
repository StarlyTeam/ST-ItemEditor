package net.starly.itemeditor.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.starly.itemeditor.ItemEditorMain.msgConfig;

@SuppressWarnings("all")
public class ItemEditorCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(msgConfig.getMessage("command.only_player"));
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        if (List.of("리로드", "reload").contains(args[0].toLowerCase())) {
            if (!player.hasPermission("starly.itemeditor.reload")) {
                player.sendMessage(msgConfig.getMessage("command.no_permission"));
                return true;
            }

            msgConfig.reloadConfig();

            player.sendMessage(msgConfig.getMessage("command.reload"));
            return true;
        } else if (List.of("도움말", "help", "?").contains(args[0].toLowerCase())) {
            sendHelp(player);
            return true;
        } else {
            if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                player.sendMessage(msgConfig.getMessage("command.no_item"));
                return true;
            }

            ItemStack item = player.getInventory().getItemInMainHand();

            switch (args[0].toLowerCase()) {
                case "이름":
                case "name":
                case "displayname": {
                    if (!player.hasPermission("starly.itemeditor.command.name")) {
                        player.sendMessage(msgConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length == 1) {
                        player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    String name = Arrays
                            .stream(Arrays.copyOfRange(args, 1, args.length))
                            .map(s -> s.replace("&", "§"))
                            .collect(Collectors.joining(" "));

                    ItemMeta itemMeta = item.getItemMeta();
                    if (itemMeta == null) itemMeta = new ItemStack(item.getType()).getItemMeta();
                    itemMeta.setDisplayName(name);
                    item.setItemMeta(itemMeta);
                    player.getInventory().setItemInMainHand(item);

                    player.sendMessage(msgConfig.getMessage("command.name", Map.of("{name}", name)));
                    return true;
                }

                case "커스텀모델데이터":
                case "cmd":
                case "cid": {
                    if (!player.hasPermission("starly.itemeditor.command.custommodeldata")) {
                        player.sendMessage(msgConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length == 1) {
                        player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    int cid;

                    try {
                        cid = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setCustomModelData(cid);
                    item.setItemMeta(itemMeta);
                    player.getInventory().setItemInMainHand(item);

                    player.sendMessage(msgConfig.getMessage("command.custom_model_data", Map.of("{custom_model_data}", String.valueOf(cid))));
                    return true;
                }

                case "타입":
                case "type": {
                    if (!player.hasPermission("starly.itemeditor.command.type")) {
                        player.sendMessage(msgConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length != 2) {
                        player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    try {
                        Material material = Material.valueOf(args[1].toUpperCase());

                        ItemStack newItem = new ItemStack(material);
                        newItem.setItemMeta(item.getItemMeta());
                        player.getInventory().setItemInMainHand(newItem);

                        player.sendMessage(msgConfig.getMessage("command.type.success", Map.of("{type}", material.name())));
                        return true;
                    } catch (Exception e) {
                        player.sendMessage(msgConfig.getMessage("command.type.fail", Map.of("{type}", args[1].toUpperCase())));
                        return true;
                    }
                }

                case "로어":
                case "lore": {
                    if (!player.hasPermission("starly.itemeditor.command.lore")) {
                        player.sendMessage(msgConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length == 1) {
                        player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    switch (args[1].toLowerCase()) {
                        case "보기":
                        case "view": {
                            if (item.getItemMeta().getLore() == null) {
                                player.sendMessage(msgConfig.getMessage("command.lore.view.no_lore"));
                                return true;
                            }

                            if (args.length == 2) {
                                msgConfig.getMessages("command.lore.view.list", Map.of("{list}", String.join(msgConfig.getString("command.lore.view.delimiter"),
                                        item.getItemMeta().getLore()))).forEach(player::sendMessage);
                            } else if (args.length == 3) {
                                int index;

                                try {
                                    index = Integer.parseInt(args[2]) - 1;
                                } catch (NumberFormatException e) {
                                    player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                    return true;
                                }

                                if (index < 0 || item.getItemMeta().getLore() == null || index >= item.getItemMeta().getLore().size()) {
                                    player.sendMessage(msgConfig.getMessage("command.lore.no_lore"));
                                    return true;
                                }

                                msgConfig.getMessages("command.lore.view.list", Map.of("{list}", item.getItemMeta().getLore().get(index))).forEach(player::sendMessage);
                                return true;
                            } else {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }
                        }

                        case "추가":
                        case "add": {
                            if (args.length == 2) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            String lore = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.copyOfRange(args, 2, args.length)));

                            List<String> lores = item.getItemMeta().getLore();
                            if (lores == null) lores = new ArrayList<>();

                            lores.add(lore);
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setLore(lores);
                            item.setItemMeta(itemMeta);
                            player.getInventory().setItemInMainHand(item);

                            player.sendMessage(msgConfig.getMessage("command.lore.add",
                                    Map.of("{lore}", lore,
                                            "{line}", lores.size() + "")));
                            return true;
                        }

                        case "수정":
                        case "edit": {
                            if (args.length < 4) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            int index;

                            try {
                                index = Integer.parseInt(args[2]) - 1;
                            } catch (NumberFormatException e) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            if (index < 0 || item.getItemMeta().getLore() == null || index >= item.getItemMeta().getLore().size()) {
                                player.sendMessage(msgConfig.getMessage("command.lore.no_lore"));
                                return true;
                            }

                            String lore = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.copyOfRange(args, 3, args.length)));

                            List<String> lores = item.getItemMeta().getLore();
                            lores.set(index, lore);
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setLore(lores);
                            item.setItemMeta(itemMeta);
                            player.getInventory().setItemInMainHand(item);

                            player.sendMessage(msgConfig.getMessage("command.lore.edit",
                                    Map.of("{lore}", lore,
                                            "{line}", index + 1 + "")));
                            return true;
                        }

                        case "삭제":
                        case "remove": {
                            if (args.length != 3) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            int index;

                            try {
                                index = Integer.parseInt(args[2]) - 1;
                            } catch (NumberFormatException e) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            if (index < 0 || item.getItemMeta().getLore() == null || index >= item.getItemMeta().getLore().size()) {
                                player.sendMessage(msgConfig.getMessage("command.lore.no_lore"));
                                return true;
                            }

                            List<String> lores = item.getItemMeta().getLore();
                            lores.remove(index);
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setLore(lores);
                            item.setItemMeta(itemMeta);
                            player.getInventory().setItemInMainHand(item);

                            player.sendMessage(msgConfig.getMessage("command.lore.remove", Map.of("{line}", index + 1 + "")));
                            return true;
                        }

                        case "초기화":
                        case "clear": {
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setLore(null);
                            item.setItemMeta(itemMeta);
                            player.getInventory().setItemInMainHand(item);

                            player.sendMessage(msgConfig.getMessage("command.lore.clear"));
                            return true;
                        }

                        default: {
                            player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                            return true;
                        }
                    }
                }

                case "인챈트":
                case "enchant": {
                    if (!player.hasPermission("starly.itemeditor.command.enchant")) {
                        player.sendMessage(msgConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length == 1) {
                        player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    switch (args[1].toLowerCase()) {
                        case "보기":
                        case "view": {
                            if (item.getItemMeta().getEnchants().isEmpty()) {
                                player.sendMessage(msgConfig.getMessage("command.enchant.no_enchant"));
                                return true;
                            }

                            if (args.length == 2) {
                                if (item.getItemMeta().getEnchants().size() == 0) msgConfig.getMessages("command.enchant.view.list", Map.of("{list}", "없음")).forEach(player::sendMessage);
                                else msgConfig.getMessages("command.enchant.view.list",
                                        Map.of("{list}", item
                                                .getItemMeta()
                                                .getEnchants()
                                                .keySet()
                                                .stream()
                                                .map(s -> "&6" + s.getKey().getKey() + " &7: &f" + item.getItemMeta().getEnchantLevel(s))
                                                .collect(Collectors.joining("\n"))))
                                        .forEach(player::sendMessage);
                            } else if (args.length == 3) {
                                Enchantment enchantment;
                                try {
                                    enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[2]));
                                } catch (Exception e) {
                                    player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                    return true;
                                }

                                if (!item.getItemMeta().getEnchants().containsKey(enchantment)) {
                                    player.sendMessage(msgConfig.getMessage("command.enchant.no_enchant"));
                                    return true;
                                }

                                msgConfig.getMessages("command.enchant.view.list", Map.of("{list}", "&6" + enchantment.getKey().getKey() + " &7: &f" + item.getItemMeta().getEnchants().get(enchantment))).forEach(player::sendMessage);
                                return true;
                            } else {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }
                        }

                        case "추가":
                        case "add": {
                            if (args.length < 3) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[2]));

                            if (enchantment == null) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            int level;

                            if (args.length == 4) {
                                try {
                                    level = Integer.parseInt(args[3]);
                                } catch (NumberFormatException e) {
                                    player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                    return true;
                                }
                            } else {
                                level = enchantment.getStartLevel();
                            }

                            if (level < enchantment.getStartLevel() || level > enchantment.getMaxLevel()) {
                                player.sendMessage(msgConfig.getMessage("command.enchant.add.wrong_level",
                                        Map.of("{min}", String.valueOf(enchantment.getStartLevel()),
                                                "{max}", String.valueOf(enchantment.getMaxLevel()))));
                                return true;
                            }

                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.addEnchant(enchantment, level, true);
                            item.setItemMeta(itemMeta);
                            player.getInventory().setItemInMainHand(item);

                            player.sendMessage(msgConfig.getMessage("command.enchant.add.success", Map.of("{enchant}", enchantment.getKey().getKey(), "{level}", String.valueOf(level))));
                            return true;
                        }

                        case "삭제":
                        case "remove": {
                            if (args.length != 3) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[2]));

                            if (enchantment == null) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            if (!item.getItemMeta().getEnchants().containsKey(enchantment)) {
                                player.sendMessage(msgConfig.getMessage("command.enchant.no_enchant"));
                                return true;
                            }

                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.removeEnchant(enchantment);
                            item.setItemMeta(itemMeta);
                            player.getInventory().setItemInMainHand(item);

                            player.sendMessage(msgConfig.getMessage("command.enchant.remove", Map.of("{enchant}", enchantment.getKey().getKey())));
                            return true;
                        }

                        case "초기화":
                        case "clear": {
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.getEnchants().keySet().forEach(itemMeta::removeEnchant);
                            item.setItemMeta(itemMeta);
                            player.getInventory().setItemInMainHand(item);

                            player.sendMessage(msgConfig.getMessage("command.enchant.clear"));
                            return true;
                        }

                        default: {
                            player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                            return true;
                        }
                    }
                }

                case "플래그":
                case "flag": {
                    if (!player.hasPermission("starly.itemeditor.command.flag")) {
                        player.sendMessage(msgConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length == 1) {
                        player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    switch (args[1].toLowerCase()) {
                        case "보기":
                        case "view": {
                            if (item.getItemMeta().getItemFlags().isEmpty()) {
                                player.sendMessage(msgConfig.getMessage("command.flag.no_flag"));
                                return true;
                            }

                            if (args.length != 2) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            msgConfig.getMessages("command.flag.view.list", Map.of("{list}", String.join(msgConfig.getString("command.flag.view.delimiter"),
                                    item.getItemMeta().getItemFlags().stream().map(s -> "&6" + s.name()).collect(Collectors.toList())))).forEach(player::sendMessage);
                        }

                        case "추가":
                        case "add": {
                            if (args.length != 3) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            ItemFlag itemFlag;

                            try {
                                itemFlag = ItemFlag.valueOf(args[2].toUpperCase());
                            } catch (IllegalArgumentException e) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.addItemFlags(itemFlag);
                            item.setItemMeta(itemMeta);
                            player.getInventory().setItemInMainHand(item);

                            player.sendMessage(msgConfig.getMessage("command.flag.add", Map.of("{flag}", itemFlag.name())));
                            return true;
                        }

                        case "삭제":
                        case "remove": {
                            if (args.length != 3) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            ItemFlag itemFlag;

                            try {
                                itemFlag = ItemFlag.valueOf(args[2].toUpperCase());
                            } catch (IllegalArgumentException e) {
                                player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            if (!item.getItemMeta().getItemFlags().contains(itemFlag)) {
                                player.sendMessage(msgConfig.getMessage("command.flag.no_flag"));
                                return true;
                            }

                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.removeItemFlags(itemFlag);
                            item.setItemMeta(itemMeta);
                            player.getInventory().setItemInMainHand(item);

                            player.sendMessage(msgConfig.getMessage("command.flag.remove", Map.of("{flag}", itemFlag.name())));
                            return true;
                        }

                        case "초기화":
                        case "clear": {
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.getItemFlags().forEach(itemMeta::removeItemFlags);
                            item.setItemMeta(itemMeta);
                            player.getInventory().setItemInMainHand(item);

                            player.sendMessage(msgConfig.getMessage("command.flag.clear"));
                            return true;
                        }

                        default: {
                            player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                            return true;
                        }
                    }
                }

                default: {
                    player.sendMessage(msgConfig.getMessage("command.wrong_command"));
                    return true;
                }
            }
        }
    }

    private void sendHelp(CommandSender sender) {
        if (!sender.hasPermission("starly.itemeditor.help")) {
            sender.sendMessage(msgConfig.getMessage("command.no_permission"));
            return;
        }

        msgConfig.getStringList("command.help").forEach(line -> sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line)));
    }
}
