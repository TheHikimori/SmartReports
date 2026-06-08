package me.azuredev.smartreports;

import me.azuredev.smartreports.commands.MyReportsCommand;
import me.azuredev.smartreports.commands.ReportCommand;
import me.azuredev.smartreports.commands.ReportCreator;
import me.azuredev.smartreports.commands.ReportsCommand;
import me.azuredev.smartreports.config.ConfigManager;
import me.azuredev.smartreports.gui.*;
import me.azuredev.smartreports.managers.ManagerRegistry;
import me.azuredev.smartreports.ticket.HistoryManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SmartReportsPlugin extends JavaPlugin {

    private static SmartReportsPlugin instance;

    private ConfigManager configManager;
    private ManagerRegistry managerRegistry;
    private HistoryManager historyManager;

    @Override
    public void onEnable() {

        instance = this;

        configManager = new ConfigManager(this);
        configManager.load();

        managerRegistry = new ManagerRegistry(this);
        managerRegistry.initialize();

        historyManager =
                managerRegistry.getHistoryManager();

        HistoryGUI historyGUI =
                new HistoryGUI(
                        historyManager
                );

        TicketViewGUI ticketViewGUI =
                new TicketViewGUI(
                        managerRegistry.getTicketManager(),
                        historyManager,
                        historyGUI
                );

        MainMenuGUI mainMenuGUI =
                new MainMenuGUI(
                        new TicketListGUI(
                                managerRegistry.getTicketManager(),
                                historyManager
                        )
                );

        ReportCreator reportCreator =
                new ReportCreator(
                        managerRegistry.getTicketManager(),
                        historyManager,
                        managerRegistry.getAntiSpamManager(),
                        managerRegistry.getStaffNotifier(),
                        managerRegistry.getTelegramManager()
                );

        PrioritySelectGUI prioritySelectGUI =
                new PrioritySelectGUI(
                        reportCreator
                );

        CategorySelectGUI categorySelectGUI =
                new CategorySelectGUI(
                        prioritySelectGUI
                );

        MyReportsGUI myReportsGUI =
                new MyReportsGUI(
                        managerRegistry.getTicketManager()
                );

        if (getCommand("report") != null) {
            getCommand("report").setExecutor(
                    new ReportCommand(
                            managerRegistry.getAntiSpamManager(),
                            categorySelectGUI
                    )
            );
        }

        if (getCommand("myreports") != null) {
            getCommand("myreports").setExecutor(
                    new MyReportsCommand(
                            myReportsGUI
                    )
            );
        }

        if (getCommand("reports") != null) {
            getCommand("reports").setExecutor(
                    new ReportsCommand(
                            mainMenuGUI
                    )
            );
        }

        if (managerRegistry.getTelegramManager() != null) {
            managerRegistry.getTelegramManager().initialize();
        }

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

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}