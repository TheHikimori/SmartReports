package me.azuredev.smartreports.managers;

import me.azuredev.smartreports.SmartReportsPlugin;
import me.azuredev.smartreports.database.AsyncExecutor;
import me.azuredev.smartreports.database.DatabaseManager;
import me.azuredev.smartreports.database.repository.SQLTicketRepository;
import me.azuredev.smartreports.database.repository.TicketRepository;
import me.azuredev.smartreports.telegram.TelegramManager;
import me.azuredev.smartreports.ticket.TicketManager;
import me.azuredev.smartreports.database.repository.HistoryRepository;
import me.azuredev.smartreports.database.repository.SQLHistoryRepository;
import me.azuredev.smartreports.ticket.HistoryManager;

public class ManagerRegistry {

    private final SmartReportsPlugin plugin;

    private DatabaseManager databaseManager;
    private TicketManager ticketManager;
    private HistoryManager historyManager;
    private AntiSpamManager antiSpamManager;
    private StaffNotifier staffNotifier;
    private TelegramManager telegramManager;

    public ManagerRegistry(SmartReportsPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {

        databaseManager = new DatabaseManager(plugin);
        databaseManager.connect();

        TicketRepository repository =
                new SQLTicketRepository(databaseManager);

        HistoryRepository historyRepository =
                new SQLHistoryRepository(
                        databaseManager
                );

        historyManager =
                new HistoryManager(
                        historyRepository
                );

        telegramManager =
                new TelegramManager(plugin);

        telegramManager.initialize();

        antiSpamManager =
                new AntiSpamManager();

        staffNotifier =
                new StaffNotifier();

        ticketManager =
                new TicketManager(repository);
    }

    public void shutdown() {

        if (databaseManager != null) {
            databaseManager.shutdown();
        }

        AsyncExecutor.shutdown();
    }

    public AntiSpamManager getAntiSpamManager() {
        return antiSpamManager;
    }

    public StaffNotifier getStaffNotifier() {
        return staffNotifier;
    }

    public TelegramManager getTelegramManager() {
        return telegramManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public TicketManager getTicketManager() {
        return ticketManager;
    }
}