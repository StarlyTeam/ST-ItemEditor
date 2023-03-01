package net.starly.itemeditor.command.tabcomplete;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemEditorTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            if (label.equalsIgnoreCase("ie")) {
                return Arrays.asList("name", "cid", "type", "lore", "enchantment", "flag", "nbt");
            } else {
                return Arrays.asList("이름", "커스텀모델데이터", "타입", "로어", "인챈트", "플래그", "태그");
            }
        } else if (args.length == 2) {
            if (Arrays.asList("커스텀모델데이터", "cid").contains(args[0].toLowerCase())) {
                return Arrays.asList("1", "2", "3", "4", "5");
            } else if (Arrays.asList("타입", "type").contains(args[0].toLowerCase())) {
                return Arrays.asList(Arrays.stream(Material.values()).map(Enum::name).toArray(String[]::new));
            }

            if (args[0].equalsIgnoreCase("로어")) {
                return Arrays.asList("보기", "추가", "수정", "삭제", "초기화");
            } else if (args[0].equalsIgnoreCase("lore")) {
                return Arrays.asList("view", "add", "edit", "remove", "clear");
            }

            if (args[0].equalsIgnoreCase("인챈트")) {
                return Arrays.asList("보기", "추가", "삭제", "초기화");
            } else if (args[0].equalsIgnoreCase("enchantment")) {
                return Arrays.asList("view", "add", "remove", "clear");
            }

            if (args[0].equalsIgnoreCase("플래그")) {
                return Arrays.asList("보기", "추가", "삭제", "초기화");
            } else if (args[0].equalsIgnoreCase("flag")) {
                return Arrays.asList("view", "add", "remove", "clear");
            }

            if (args[0].equalsIgnoreCase("태그")) {
                return Arrays.asList("보기", "설정", "삭제", "초기화");
            } else if (args[0].equalsIgnoreCase("nbt")) {
                return Arrays.asList("view", "set", "remove", "clear");
            }
        } else if (args.length == 3) {
            if (Arrays.asList("로어", "lore").contains(args[0].toLowerCase())) {
                if (Arrays.asList("보기", "view", "수정", "edit", "삭제", "remove").contains(args[1].toLowerCase())) {
                    return Arrays.asList("1", "2", "3", "4", "5");
                }
            } else if (Arrays.asList("인챈트", "enchantment").contains(args[0].toLowerCase())) {
                if (Arrays.asList("추가", "add", "삭제", "remove").contains(args[1].toLowerCase())) {
                    return Arrays.asList(Arrays.stream(Enchantment.values()).map(s -> s.getKey().getKey()).toArray(String[]::new));
                }
            } else if (Arrays.asList("플래그", "flag").contains(args[0].toLowerCase())) {
                if (Arrays.asList("추가", "add", "삭제", "remove").contains(args[1].toLowerCase())) {
                    return Arrays.asList(Arrays.stream(ItemFlag.values()).map(Enum::name).toArray(String[]::new));
                }
            } else if (Arrays.asList("태그", "nbt").contains(args[0].toLowerCase())) {
                if (Arrays.asList("추가", "삭제").contains(args[1].toLowerCase())) {
                    return Collections.singletonList("<키>");
                } else if (Arrays.asList("add", "remove").contains(args[1].toLowerCase())) {
                    return Collections.singletonList("<Key>");
                }
            }
        } else if (args.length == 4) {
            if (Arrays.asList("인챈트", "enchant").contains(args[0].toLowerCase())) {
                if (Arrays.asList("추가", "add").contains(args[1].toLowerCase())) {
                    return Arrays.asList("1", "2", "3", "4", "5");
                }
            } else if (Arrays.asList("태그", "nbt").contains(args[0].toLowerCase())) {
                if (Arrays.asList("추가", "삭제").contains(args[1].toLowerCase())) {
                    return Collections.singletonList("<값>");
                } else if (Arrays.asList("add", "remove").contains(args[1].toLowerCase())) {
                    return Collections.singletonList("<Value>");
                }
            }
        }

        return Collections.emptyList();
    }
}