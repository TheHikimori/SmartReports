package me.azuredev.smartreports.commands;

import me.azuredev.smartreports.ticket.HistoryManager;
import me.azuredev.smartreports.ticket.Ticket;
import me.azuredev.smartreports.ticket.TicketHistory;
import me.azuredev.smartreports.ticket.TicketManager;
import me.azuredev.smartreports.ticket.TicketStatus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketCommand implements CommandExecutor {

    private final TicketManager ticketManager;
    private final HistoryManager historyManager;

    public TicketCommand(
            TicketManager ticketManager,
            HistoryManager historyManager
    ) {

        this.ticketManager = ticketManager;
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

        if (args.length < 2) {

            player.sendMessage(
                    "§c/ticket <claim|resolve|close> <id>"
            );

            return true;
        }

        String action =
                args[0].toLowerCase();

        long ticketId;

        try {

            ticketId =
                    Long.parseLong(args[1]);

        } catch (NumberFormatException e) {

            player.sendMessage(
                    "§cНекорректный ID."
            );

            return true;
        }

        ticketManager.getTicket(ticketId)
                .thenAccept(optionalTicket -> {

                    if (optionalTicket.isEmpty()) {

                        player.sendMessage(
                                "§cТикет не найден."
                        );

                        return;
                    }

                    Ticket ticket =
                            optionalTicket.get();

                    switch (action) {

                        case "claim" -> {

                            ticket.setStatus(
                                    TicketStatus.CLAIMED
                            );

                            ticket.setAssignedStaff(
                                    player.getUniqueId()
                            );

                            save(
                                    ticket,
                                    player,
                                    "CLAIM",
                                    "Тикет взят в работу"
                            );
                        }

                        case "resolve" -> {

                            ticket.setStatus(
                                    TicketStatus.RESOLVED
                            );

                            save(
                                    ticket,
                                    player,
                                    "RESOLVE",
                                    "Тикет решён"
                            );
                        }

                        case "close" -> {

                            ticket.setStatus(
                                    TicketStatus.CLOSED
                            );

                            save(
                                    ticket,
                                    player,
                                    "CLOSE",
                                    "Тикет закрыт"
                            );
                        }

                        default -> player.sendMessage(
                                "§cНеизвестное действие."
                        );
                    }
                });

        return true;
    }

    private void save(
            Ticket ticket,
            Player player,
            String action,
            String description
    ) {

        ticket.setUpdatedAt(
                System.currentTimeMillis()
        );

        ticketManager.updateTicket(ticket);

        TicketHistory history =
                new TicketHistory();

        history.setTicketId(
                ticket.getId()
        );

        history.setActor(
                player.getName()
        );

        history.setAction(
                action
        );

        history.setDescription(
                description
        );

        history.setTimestamp(
                System.currentTimeMillis()
        );

        historyManager.addHistory(
                history
        );

        player.sendMessage(
                "§aУспешно."
        );
    }
}