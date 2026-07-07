package me.azuredev.smartreports.commands;

import me.azuredev.smartreports.ticket.HistoryManager;
import me.azuredev.smartreports.ticket.TicketHistory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TicketHistoryCommand implements CommandExecutor {

    private final HistoryManager historyManager;

    public TicketHistoryCommand(
            HistoryManager historyManager
    ) {

        this.historyManager = historyManager;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player))
            return true;

        if (args.length < 1) {

            player.sendMessage(
                    "§c/tickethistory <id>"
            );

            return true;
        }

        long ticketId;

        try {

            ticketId =
                    Long.parseLong(args[0]);

        } catch (NumberFormatException e) {

            player.sendMessage(
                    "§cНекорректный ID."
            );

            return true;
        }

        historyManager.getHistory(ticketId)
                .thenAccept(historyList -> {

                    player.sendMessage(
                            "§6История тикета #" + ticketId
                    );

                    for (TicketHistory history : historyList) {

                        String date =
                                new SimpleDateFormat(
                                        "dd.MM.yyyy HH:mm"
                                ).format(
                                        new Date(
                                                history.getTimestamp()
                                        )
                                );

                        player.sendMessage(
                                "§e[" + history.getAction() + "] §f"
                                        + history.getDescription()
                                        + " §7(" + history.getActor()
                                        + ", " + date + ")"
                        );
                    }
                });

        return true;
    }
}