package net.starly.itemeditor;

import net.starly.core.bstats.Metrics;
import net.starly.core.data.Config;
import net.starly.core.data.MessageConfig;
import net.starly.itemeditor.command.ItemEditorCommand;
import net.starly.itemeditor.command.ItemEditorTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ItemEditorMain extends JavaPlugin {
    private static final Logger log = Bukkit.getLogger();
    public static Config config;
    public static MessageConfig messageConfig;

    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("ST-Core") == null) {
            log.warning("[" + plugin.getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            log.warning("[" + plugin.getName() + "] 다운로드 링크 : &fhttps://discord.gg/TF8jqSJjCG");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;

        new Metrics(this, 17173);


        // CONFIG
        config = new Config("config", plugin);
        config.loadDefaultPluginConfig();

        Config msgConfig = new Config("message", plugin);
        msgConfig.loadDefaultPluginConfig();
        messageConfig = new MessageConfig(msgConfig, "prefix");

        // COMMAND
        Bukkit.getPluginCommand("itemeditor").setExecutor(new ItemEditorCommand());
        Bukkit.getPluginCommand("itemeditor").setTabCompleter(new ItemEditorTabComplete());
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}