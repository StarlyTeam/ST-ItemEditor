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
        if (args.length == 1) return List.of("이름", "커스텀모델데이터", "타입", "로어", "인챈트", "플래그");
        else if (args.length == 2) {
            if (List.of("커스텀모델데이터", "cmd", "cid").contains(args[0].toLowerCase())) return List.of("1", "2", "3", "4", "5");
            else if (List.of("타입", "type").contains(args[0].toLowerCase())) return Arrays.asList(Arrays.stream(Material.values()).map(Enum::name).toArray(String[]::new));
            else if (List.of("로어", "lore").contains(args[0].toLowerCase())) return List.of("보기", "추가", "수정", "삭제", "초기화");
            else if (List.of("인챈트", "enchant").contains(args[0].toLowerCase())) return List.of("보기", "추가", "삭제", "초기화");
            else if (List.of("플래그", "flag").contains(args[0].toLowerCase())) return List.of("보기", "추가", "삭제", "초기화");
        } else if (args.length == 3) {
            if (List.of("로어", "lore").contains(args[0].toLowerCase())) {
                if (List.of("보기", "view", "수정", "edit", "삭제", "remove").contains(args[1].toLowerCase())) return List.of("1", "2", "3", "4", "5");
            } else if (List.of("인챈트", "enchant").contains(args[0].toLowerCase())) {
                if (List.of("보기", "view", "추가", "add", "삭제", "remove").contains(args[1].toLowerCase())) return Arrays.asList(Arrays.stream(Enchantment.values()).map(s -> s.getKey().getKey()).toArray(String[]::new));
            } else if (List.of("플래그", "flag").contains(args[0].toLowerCase())) {
                if (List.of("추가", "add", "삭제", "remove").contains(args[1].toLowerCase())) return Arrays.asList(Arrays.stream(ItemFlag.values()).map(Enum::name).toArray(String[]::new));
            }
        } else if (args.length == 4) {
            if (List.of("인챈트", "enchant").contains(args[0].toLowerCase())) {
                if (List.of("추가", "add").contains(args[1].toLowerCase())) return List.of("1", "2", "3", "4", "5");
            }
        }

        return Collections.emptyList();
    }
}