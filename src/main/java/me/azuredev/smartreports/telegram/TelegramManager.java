package me.azuredev.smartreports.telegram;

import me.azuredev.smartreports.SmartReportsPlugin;
import me.azuredev.smartreports.ticket.TicketManager;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramManager {

    private final SmartReportsPlugin plugin;
    private SmartReportsBot bot;

    public TelegramManager(SmartReportsPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {

        if (!plugin.getConfig().getBoolean(
                "telegram.enabled"
        )) {
            return;
        }

        try {
            // Получаем менеджер и проверяем его на null
            TicketManager ticketManager = plugin.getManagerRegistry().getTicketManager();

            if (ticketManager == null) {
                plugin.getLogger().severe("Ошибка: TicketManager еще не инициализирован! Бот не был запущен.");
                return;
            }

            TelegramBotsApi api =
                    new TelegramBotsApi(
                            DefaultBotSession.class
                    );

            bot = new SmartReportsBot(
                    plugin,
                    ticketManager
            );

            api.registerBot(bot);

            plugin.getLogger().info(
                    "Telegram bot enabled."
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SmartReportsBot getBot() {
        return bot;
    }
}