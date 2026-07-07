package me.azuredev.smartreports.subcommands;

import me.azuredev.smartreports.ticket.TicketManager;
import org.bukkit.command.CommandSender;

public class DeleteTicketCommand {

    private final TicketManager ticketManager;

    public DeleteTicketCommand(
            TicketManager ticketManager
    ) {
        this.ticketManager = ticketManager;
    }

    public void execute(
            CommandSender sender,
            long ticketId
    ) {

        ticketManager.deleteTicket(ticketId)
                .thenRun(() ->
                        sender.sendMessage(
                                "§aТикет #" + ticketId + " удалён."
                        )
                );
    }
}