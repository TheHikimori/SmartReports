package me.azuredev.smartreports.commands;

import me.azuredev.smartreports.ticket.*;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommentSubCommand {

    private final TicketManager ticketManager;
    private final HistoryManager historyManager;

    public CommentSubCommand(
            TicketManager ticketManager,
            HistoryManager historyManager
    ) {
        this.ticketManager = ticketManager;
        this.historyManager = historyManager;
    }

    public void execute(
            Player player,
            long ticketId,
            String comment
    ) {

        ticketManager.findById(ticketId)
                .thenAccept(optional -> {

                    if (optional.isEmpty())
                        return;

                    TicketHistory history =
                            new TicketHistory();

                    history.setTicketId(ticketId);

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
                });
    }
}