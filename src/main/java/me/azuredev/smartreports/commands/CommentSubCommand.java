package me.azuredev.smartreports.commands;

import me.azuredev.smartreports.SmartReportsPlugin;
import me.azuredev.smartreports.telegram.TelegramManager;
import me.azuredev.smartreports.ticket.*;

import org.bukkit.entity.Player;

public class CommentSubCommand {

    private final TicketManager ticketManager;
    private final HistoryManager historyManager;
    private final TelegramManager telegramManager;

    public CommentSubCommand(
            TicketManager ticketManager,
            HistoryManager historyManager,
            TelegramManager telegramManager
    ) {

        this.ticketManager = ticketManager;
        this.historyManager = historyManager;
        this.telegramManager = telegramManager;
    }

    public void execute(
            Player player,
            long ticketId,
            String comment
    ) {

        ticketManager.findById(ticketId)
                .thenAccept(optional -> {

                    if (optional.isEmpty()) {

                        player.sendMessage(
                                "§cТикет не найден."
                        );

                        return;
                    }

                    TicketHistory history =
                            new TicketHistory();

                    history.setTicketId(
                            ticketId
                    );

                    history.setActor(
                            player.getName()
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
                                            + player.getName()
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
                                        player.getName(),
                                        comment
                                );
                    }

                    player.sendMessage(
                            "§aКомментарий добавлен."
                    );
                });
    }
}