package net.starly.itemeditor.context;

import net.starly.core.data.Config;
import net.starly.itemeditor.ItemEditorMain;

public class MessageContent {
    private static MessageContent instance;
    private final Config config;

    private MessageContent() {
        config = new Config("message", ItemEditorMain.getPlugin());
        config.loadDefaultConfig();
        config.setPrefix("prefix");
    }

    public static MessageContent getInstance() {
        if (instance == null) {
            synchronized (MessageContent.class) {
                instance = new MessageContent();
            }
        }

        return instance;
    }

    public Config getConfig() {
        return config;
    }
}
