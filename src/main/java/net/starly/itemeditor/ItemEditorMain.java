package net.starly.itemeditor;

import net.starly.core.bstats.Metrics;
import net.starly.itemeditor.command.ItemEditorCmd;
import net.starly.itemeditor.command.tabcomplete.ItemEditorTab;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemEditorMain extends JavaPlugin {
    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("ST-Core") == null) {
            Bukkit.getLogger().warning("[" + plugin.getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            Bukkit.getLogger().warning("[" + plugin.getName() + "] 다운로드 링크 : &fhttps://discord.gg/TF8jqSJjCG");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;
        new Metrics(this, 17173);

        // COMMAND
        Bukkit.getPluginCommand("itemeditor").setExecutor(new ItemEditorCmd());
        Bukkit.getPluginCommand("itemeditor").setTabCompleter(new ItemEditorTab());
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}