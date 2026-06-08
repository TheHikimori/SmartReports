package me.azuredev.smartreports.commands;

import me.azuredev.smartreports.ticket.TicketManager;
import org.bukkit.command.*;

public class ReportsDeleteCommand implements CommandExecutor {

    private final TicketManager ticketManager;

    public ReportsDeleteCommand(
            TicketManager ticketManager
    ) {
        this.ticketManager = ticketManager;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (args.length < 1) {
            sender.sendMessage(
                    "/reportsdelete <id>"
            );
            return true;
        }

        long id =
                Long.parseLong(args[0]);

        ticketManager.deleteTicket(id);

        sender.sendMessage(
                "Тикет удалён."
        );

        return true;
    }
}