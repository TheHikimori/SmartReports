package me.azuredev.smartreports;

import me.azuredev.smartreports.config.ConfigManager;
import me.azuredev.smartreports.managers.ManagerRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmartReportsPlugin extends JavaPlugin {

    private static SmartReportsPlugin instance;

    private ConfigManager configManager;
    private ManagerRegistry managerRegistry;

    @Override
    public void onEnable() {

        instance = this;

        this.configManager = new ConfigManager(this);
        configManager.load();

        this.managerRegistry = new ManagerRegistry(this);
        managerRegistry.initialize();

        getLogger().info("SmartReports enabled.");

    }

    @Override
    public void onDisable() {

        if (managerRegistry != null) {
            managerRegistry.shutdown();
        }

        getLogger().info("SmartReports disabled.");
    }

    public static SmartReportsPlugin getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ManagerRegistry getManagerRegistry() {
        return managerRegistry;
    }
}