package me.azuredev.smartreports.subcommands;

import me.azuredev.smartreports.SmartReportsPlugin;
import me.azuredev.smartreports.telegram.TelegramManager;
import me.azuredev.smartreports.ticket.HistoryManager;
import me.azuredev.smartreports.ticket.TicketHistory;
import org.bukkit.command.CommandSender;

public class CommentTicketCommand {

    private final HistoryManager historyManager;
    private final TelegramManager telegramManager;

    public CommentTicketCommand(
            HistoryManager historyManager,
            TelegramManager telegramManager
    ) {

        this.historyManager = historyManager;
        this.telegramManager = telegramManager;
    }

    public void execute(
            CommandSender sender,
            long ticketId,
            String comment
    ) {

        TicketHistory history =
                new TicketHistory();

        history.setTicketId(ticketId);

        history.setActor(
                sender.getName()
        );

        history.setAction(
                "COMMENT"
        );

        history.setDescription(
                comment
        );

        history.setTimestamp(
                System.currentTimeMillis()
        );

        historyManager.addHistory(
                history
        );

        SmartReportsPlugin.getInstance()
                .getLogger()
                .info(
                        "[COMMENT] Ticket #"
                                + ticketId
                                + " | "
                                + sender.getName()
                                + ": "
                                + comment
                );

        if (
                telegramManager != null
                        &&
                        telegramManager.getBot() != null
        ) {

            telegramManager.getBot()
                    .sendComment(
                            ticketId,
                            sender.getName(),
                            comment
                    );
        }

        sender.sendMessage(
                "§aКомментарий добавлен."
        );
    }
}