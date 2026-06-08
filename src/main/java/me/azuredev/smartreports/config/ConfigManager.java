package me.azuredev.smartreports.config;

import me.azuredev.smartreports.SmartReportsPlugin;

public class ConfigManager {

    private final SmartReportsPlugin plugin;

    public ConfigManager(SmartReportsPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {

        plugin.saveDefaultConfig();

        save("messages.yml");
        save("gui.yml");
        save("telegram.yml");
    }

    private void save(String file) {

        if (!new java.io.File(plugin.getDataFolder(), file).exists()) {
            plugin.saveResource(file, false);
        }
    }
}