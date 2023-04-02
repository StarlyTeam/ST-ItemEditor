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
        if (!isPluginEnabled("net.starly.core.StarlyCore")) {
            Bukkit.getLogger().warning("[" + plugin.getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            Bukkit.getLogger().warning("[" + plugin.getName() + "] 다운로드 링크 : &fhttp://starly.kr/");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;
        new Metrics(this, 17173);

        // COMMAND
        Bukkit.getPluginCommand("item-editor").setExecutor(new ItemEditorCmd());
        Bukkit.getPluginCommand("item-editor").setTabCompleter(new ItemEditorTab());
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    private boolean isPluginEnabled(String path) {
        try {
            Class.forName(path);
            return true;
        } catch (NoClassDefFoundError ignored) {
        } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    }
}