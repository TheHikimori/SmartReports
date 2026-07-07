package me.azuredev.smartreports;

import me.azuredev.smartreports.commands.*;
import me.azuredev.smartreports.config.ConfigManager;
import me.azuredev.smartreports.gui.*;
import me.azuredev.smartreports.managers.ManagerRegistry;
import me.azuredev.smartreports.subcommands.CommentTicketCommand;
import me.azuredev.smartreports.subcommands.DeleteTicketCommand;
import me.azuredev.smartreports.ticket.HistoryManager;
import me.azuredev.smartreports.ticket.TicketManager;
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


        TicketListGUI ticketListGUI = new TicketListGUI(
                managerRegistry.getTicketManager(),
                historyManager
        );

        MainMenuGUI mainMenuGUI = new MainMenuGUI(ticketListGUI);

        ticketListGUI.setMainMenuGUI(mainMenuGUI);

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

        DeleteTicketCommand deleteTicketCommand =
                new DeleteTicketCommand(
                        managerRegistry.getTicketManager()
                );

        CommentTicketCommand commentTicketCommand =
                new CommentTicketCommand(
                        historyManager,
                        managerRegistry.getTelegramManager()
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

        if (getCommand("testreport") != null) {
            getCommand("testreport").setExecutor(
                    new TestReportCommand(
                            reportCreator
                    )
            );
        }

        if (getCommand("ticket") != null) {
            getCommand("ticket").setExecutor(
                    new TicketCommand(
                            managerRegistry.getTicketManager(),
                            historyManager
                    )
            );
        }

        if (getCommand("tickethistory") != null) {
            getCommand("tickethistory").setExecutor(
                    new TicketHistoryCommand(
                            historyManager
                    )
            );
        }

        if (getCommand("reports") != null) {
            getCommand("reports").setExecutor(
                    new ReportsCommand(
                            mainMenuGUI,
                            deleteTicketCommand,
                            commentTicketCommand
                    )
            );
        }

        if (managerRegistry.getTelegramManager() != null) {
            managerRegistry.getTelegramManager()
                    .initialize();
        }

        getLogger().info(
                "SmartReports enabled."
        );
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