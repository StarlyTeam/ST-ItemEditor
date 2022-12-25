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

import static net.starly.itemeditor.ItemEditorMain.config;
import static net.starly.itemeditor.ItemEditorMain.messageConfig;

public class ItemEditorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(messageConfig.getMessage("command.only_player"));
            return true;
        }
        if (!p.hasPermission("starly.itemeditor." + config.getString("permission.command.use"))) {
            p.sendMessage(messageConfig.getMessage("command.no_permission"));
            return true;
        }

        if (args.length == 0) {
            sendHelp(p);
            return true;
        }

        if (List.of("리로드", "reload").contains(args[0].toLowerCase())) {
            if (!p.hasPermission("starly.itemeditor." + config.getString("permission.command.reload"))) {
                p.sendMessage(messageConfig.getMessage("command.no_permission"));
                return true;
            }

            config.reloadConfig();
            messageConfig.reloadConfig();

            p.sendMessage(messageConfig.getMessage("command.reload"));
            return true;
        } else if (List.of("도움말", "help", "?").contains(args[0].toLowerCase())) {
            sendHelp(p);
            return true;
        } else {
            if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
                p.sendMessage(messageConfig.getMessage("command.no_item"));
                return true;
            }

            ItemStack item = p.getInventory().getItemInMainHand();

            switch (args[0].toLowerCase()) {
                case "이름", "name", "displayname" -> {
                    if (!p.hasPermission("starly.itemeditor." + config.getString("permission.command.name"))) {
                        p.sendMessage(messageConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length == 1) {
                        p.sendMessage(messageConfig.getMessage("command.wrong_command"));
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
                    p.getInventory().setItemInMainHand(item);

                    p.sendMessage(messageConfig.getMessage("command.name", Map.of("{name}", name)));
                }

                case "커스텀모델데이터", "cmd", "cid" -> {
                    if (!p.hasPermission("starly.itemeditor." + config.getString("permission.command.custom_model_data"))) {
                        p.sendMessage(messageConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length == 1) {
                        p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    int cid;

                    try {
                        cid = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setCustomModelData(cid);
                    item.setItemMeta(itemMeta);
                    p.getInventory().setItemInMainHand(item);

                    p.sendMessage(messageConfig.getMessage("command.custom_model_data", Map.of("{custom_model_data}", String.valueOf(cid))));
                }

                case "타입", "type" -> {
                    if (!p.hasPermission("starly.itemeditor." + config.getString("permission.command.type"))) {
                        p.sendMessage(messageConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length != 2) {
                        p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    try {
                        Material material = Material.valueOf(args[1].toUpperCase());

                        ItemStack newItem = new ItemStack(material);
                        newItem.setItemMeta(item.getItemMeta());
                        p.getInventory().setItemInMainHand(newItem);

                        p.sendMessage(messageConfig.getMessage("command.type.success", Map.of("{type}", material.name())));
                    } catch (Exception e) {
                        p.sendMessage(messageConfig.getMessage("command.type.fail"), args[1].toUpperCase());
                    }
                }

                case "로어", "lore" -> {
                    if (!p.hasPermission("starly.itemeditor." + config.getString("permission.command.lore"))) {
                        p.sendMessage(messageConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length == 1) {
                        p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    switch (args[1].toLowerCase()) {
                        case "보기", "view" -> {
                            if (item.getItemMeta().getLore() == null) {
                                p.sendMessage(messageConfig.getMessage("command.lore.view.no_lore"));
                                return true;
                            }

                            if (args.length == 2) {
                                messageConfig.getMessages("command.lore.view.list", Map.of("{list}", String.join(messageConfig.getConfig().getString("command.lore.view.delimiter"),
                                        item.getItemMeta().getLore()))).forEach(p::sendMessage);
                            } else if (args.length == 3) {
                                int index;

                                try {
                                    index = Integer.parseInt(args[2]) - 1;
                                } catch (NumberFormatException e) {
                                    p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                    return true;
                                }

                                if (index < 0 || item.getItemMeta().getLore() == null || index >= item.getItemMeta().getLore().size()) {
                                    p.sendMessage(messageConfig.getMessage("command.lore.no_lore"));
                                    return true;
                                }

                                messageConfig.getMessages("command.lore.view.list", Map.of("{list}", item.getItemMeta().getLore().get(index))).forEach(p::sendMessage);
                                return true;
                            } else {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }
                        }

                        case "추가", "add" -> {
                            if (args.length == 2) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            String lore = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.copyOfRange(args, 2, args.length)));

                            List<String> lores = item.getItemMeta().getLore();
                            if (lores == null) lores = new ArrayList<>();

                            lores.add(lore);
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setLore(lores);
                            item.setItemMeta(itemMeta);
                            p.getInventory().setItemInMainHand(item);

                            p.sendMessage(messageConfig.getMessage("command.lore.add",
                                    Map.of("{lore}", lore,
                                            "{line}", lores.size() + "")));
                            return true;
                        }

                        case "수정", "edit" -> {
                            if (args.length < 4) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            int index;

                            try {
                                index = Integer.parseInt(args[2]) - 1;
                            } catch (NumberFormatException e) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            if (index < 0 || item.getItemMeta().getLore() == null || index >= item.getItemMeta().getLore().size()) {
                                p.sendMessage(messageConfig.getMessage("command.lore.no_lore"));
                                return true;
                            }

                            String lore = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.copyOfRange(args, 3, args.length)));

                            List<String> lores = item.getItemMeta().getLore();
                            lores.set(index, lore);
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setLore(lores);
                            item.setItemMeta(itemMeta);
                            p.getInventory().setItemInMainHand(item);

                            p.sendMessage(messageConfig.getMessage("command.lore.edit",
                                    Map.of("{lore}", lore,
                                            "{line}", index + 1 + "")));
                            return true;
                        }

                        case "삭제", "remove" -> {
                            if (args.length != 3) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            int index;

                            try {
                                index = Integer.parseInt(args[2]) - 1;
                            } catch (NumberFormatException e) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            if (index < 0 || item.getItemMeta().getLore() == null || index >= item.getItemMeta().getLore().size()) {
                                p.sendMessage(messageConfig.getMessage("command.lore.no_lore"));
                                return true;
                            }

                            List<String> lores = item.getItemMeta().getLore();
                            lores.remove(index);
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setLore(lores);
                            item.setItemMeta(itemMeta);
                            p.getInventory().setItemInMainHand(item);

                            p.sendMessage(messageConfig.getMessage("command.lore.remove", Map.of("{line}", index + 1 + "")));
                            return true;
                        }

                        case "초기화", "clear" -> {
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.setLore(null);
                            item.setItemMeta(itemMeta);
                            p.getInventory().setItemInMainHand(item);

                            p.sendMessage(messageConfig.getMessage("command.lore.clear"));
                            return true;
                        }

                        default -> {
                            p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                            return true;
                        }
                    }

                    p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                    return false;
                }

                case "인챈트", "enchant" -> {
                    if (!p.hasPermission("starly.itemeditor." + config.getString("permission.command.enchant"))) {
                        p.sendMessage(messageConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length == 1) {
                        p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    switch (args[1].toLowerCase()) {
                        case "보기", "view" -> {
                            if (item.getItemMeta().getEnchants().isEmpty()) {
                                p.sendMessage(messageConfig.getMessage("command.enchant.no_enchant"));
                                return true;
                            }

                            if (args.length == 2) {
                                messageConfig.getMessages("command.enchant.view.list", Map.of("{list}", String.join(messageConfig.getConfig().getString("command.enchant.delimiter"),
                                        item.getItemMeta().getEnchants().entrySet().stream().map(entry -> entry.getKey().getKey().getKey() + " " + entry.getValue()).collect(Collectors.toList())))).forEach(p::sendMessage);
                            } else if (args.length == 3) {
                                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[2]));

                                if (enchantment == null) {
                                    p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                    return true;
                                }

                                if (!item.getItemMeta().getEnchants().containsKey(enchantment)) {
                                    p.sendMessage(messageConfig.getMessage("command.enchant.no_enchant"));
                                    return true;
                                }

                                messageConfig.getMessages("command.enchant.view.list", Map.of("{list}", enchantment.getKey().getKey() + " " + item.getItemMeta().getEnchants().get(enchantment))).forEach(p::sendMessage);
                                return true;
                            } else {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }
                        }

                        case "추가", "add" -> {
                            if (args.length < 3) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[2]));

                            if (enchantment == null) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            int level;

                            if (args.length == 4) {
                                try {
                                    level = Integer.parseInt(args[3]);
                                } catch (NumberFormatException e) {
                                    p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                    return true;
                                }
                            } else {
                                level = enchantment.getStartLevel();
                            }

                            if (level < enchantment.getStartLevel() || level > enchantment.getMaxLevel()) {
                                p.sendMessage(messageConfig.getMessage("command.enchant.add.wrong_level",
                                        Map.of("{min}", String.valueOf(enchantment.getStartLevel()),
                                                "{max}", String.valueOf(enchantment.getMaxLevel()))));
                                return true;
                            }

                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.addEnchant(enchantment, level, true);
                            item.setItemMeta(itemMeta);
                            p.getInventory().setItemInMainHand(item);

                            p.sendMessage(messageConfig.getMessage("command.enchant.add.success", Map.of("{enchant}", enchantment.getKey().getKey(), "{level}", String.valueOf(level))));
                            return true;
                        }

                        case "삭제", "remove" -> {
                            if (args.length != 3) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[2]));

                            if (enchantment == null) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            if (!item.getItemMeta().getEnchants().containsKey(enchantment)) {
                                p.sendMessage(messageConfig.getMessage("command.enchant.no_enchant"));
                                return true;
                            }

                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.removeEnchant(enchantment);
                            item.setItemMeta(itemMeta);
                            p.getInventory().setItemInMainHand(item);

                            p.sendMessage(messageConfig.getMessage("command.enchant.remove", Map.of("{enchant}", enchantment.getKey().getKey())));
                            return true;
                        }

                        case "초기화", "clear" -> {
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.getEnchants().keySet().forEach(itemMeta::removeEnchant);
                            item.setItemMeta(itemMeta);
                            p.getInventory().setItemInMainHand(item);

                            p.sendMessage(messageConfig.getMessage("command.enchant.clear"));
                            return true;
                        }

                        default -> {
                            p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                            return true;
                        }
                    }
                }

                case "플래그", "flag" -> {
                    if (!p.hasPermission("starly.itemeditor." + config.getString("permission.command.flag"))) {
                        p.sendMessage(messageConfig.getMessage("command.no_permission"));
                        return true;
                    }

                    if (args.length == 1) {
                        p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                        return true;
                    }

                    switch (args[1].toLowerCase()) {
                        case "보기", "view" -> {
                            if (item.getItemMeta().getItemFlags().isEmpty()) {
                                p.sendMessage(messageConfig.getMessage("command.flag.no_flag"));
                                return true;
                            }

                            if (args.length != 2) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            messageConfig.getMessages("command.flag.view.list", Map.of("{list}", String.join(messageConfig.getConfig().getString("command.flag.view.delimiter"),
                                    item.getItemMeta().getItemFlags().stream().map(ItemFlag::name).collect(Collectors.toList())))).forEach(p::sendMessage);
                        }

                        case "추가", "add" -> {
                            if (args.length != 3) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            ItemFlag itemFlag;

                            try {
                                itemFlag = ItemFlag.valueOf(args[2].toUpperCase());
                            } catch (IllegalArgumentException e) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.addItemFlags(itemFlag);
                            item.setItemMeta(itemMeta);
                            p.getInventory().setItemInMainHand(item);

                            p.sendMessage(messageConfig.getMessage("command.flag.add", Map.of("{flag}", itemFlag.name())));
                            return true;
                        }

                        case "삭제", "remove" -> {
                            if (args.length != 3) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            ItemFlag itemFlag;

                            try {
                                itemFlag = ItemFlag.valueOf(args[2].toUpperCase());
                            } catch (IllegalArgumentException e) {
                                p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                                return true;
                            }

                            if (!item.getItemMeta().getItemFlags().contains(itemFlag)) {
                                p.sendMessage(messageConfig.getMessage("command.flag.no_flag"));
                                return true;
                            }

                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.removeItemFlags(itemFlag);
                            item.setItemMeta(itemMeta);
                            p.getInventory().setItemInMainHand(item);

                            p.sendMessage(messageConfig.getMessage("command.flag.remove", Map.of("{flag}", itemFlag.name())));
                            return true;
                        }

                        case "초기화", "clear" -> {
                            ItemMeta itemMeta = item.getItemMeta();
                            itemMeta.getItemFlags().forEach(itemMeta::removeItemFlags);
                            item.setItemMeta(itemMeta);
                            p.getInventory().setItemInMainHand(item);

                            p.sendMessage(messageConfig.getMessage("command.flag.clear"));
                            return true;
                        }

                        default -> {
                            p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                            return true;
                        }
                    }
                }

                default -> {
                    p.sendMessage(messageConfig.getMessage("command.wrong_command"));
                    return true;
                }
            }

            return false;
        }
    }

    private void sendHelp(CommandSender sender) {
        messageConfig.getConfig().getStringList("command.help").forEach(line -> sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line)));
    }
}
