package me.azuredev.smartreports.commands;

import me.azuredev.smartreports.SmartReportsPlugin;
import me.azuredev.smartreports.managers.AntiSpamManager;
import me.azuredev.smartreports.managers.StaffNotifier;
import me.azuredev.smartreports.report.ReportCreationContext;
import me.azuredev.smartreports.telegram.TelegramManager;
import me.azuredev.smartreports.ticket.*;

import org.bukkit.Bukkit;

public class ReportCreator {

    private final TicketManager ticketManager;
    private final HistoryManager historyManager;
    private final AntiSpamManager antiSpamManager;
    private final StaffNotifier staffNotifier;
    private final TelegramManager telegramManager;

    public ReportCreator(
            TicketManager ticketManager,
            HistoryManager historyManager,
            AntiSpamManager antiSpamManager,
            StaffNotifier staffNotifier,
            TelegramManager telegramManager
    ) {

        this.ticketManager = ticketManager;
        this.historyManager = historyManager;
        this.antiSpamManager = antiSpamManager;
        this.staffNotifier = staffNotifier;
        this.telegramManager = telegramManager;
    }

    public void create(
            ReportCreationContext context
    ) {

        Ticket ticket = new Ticket();

        ticket.setReporter(
                context.getReporter().getUniqueId()
        );

        ticket.setReportedPlayer(
                context.getTarget().getUniqueId()
        );

        ticket.setReason(
                context.getReason()
        );

        ticket.setCategory(
                context.getCategory()
        );

        ticket.setPriority(
                context.getPriority()
        );

        ticket.setStatus(
                TicketStatus.NEW
        );

        ticket.setCreatedAt(
                System.currentTimeMillis()
        );

        ticket.setUpdatedAt(
                System.currentTimeMillis()
        );

        ticketManager.createTicket(ticket)
                .thenAccept(id -> {

                    TicketHistory history =
                            new TicketHistory();

                    history.setTicketId(id);

                    history.setActor(
                            context.getReporter().getName()
                    );

                    history.setAction(
                            "CREATED"
                    );

                    history.setDescription(
                            "Репорт создан"
                    );

                    history.setTimestamp(
                            System.currentTimeMillis()
                    );

                    historyManager.addHistory(
                            history
                    );

                    antiSpamManager.update(
                            context.getReporter()
                                    .getUniqueId()
                    );

                    staffNotifier.notifyNewTicket(
                            id,
                            context.getReporter().getName(),
                            context.getTarget().getName()
                    );

                    if (
                            telegramManager != null &&
                                    telegramManager.getBot() != null
                    ) {

                        telegramManager.getBot().sendTicket(
                                id,
                                context.getReporter().getName(),
                                context.getTarget().getName(),
                                context.getReason()
                        );
                    }

                    Bukkit.getScheduler().runTask(
                            SmartReportsPlugin.getInstance(),
                            () -> context.getReporter()
                                    .sendMessage(
                                            "§aРепорт создан. ID: §e" + id
                                    )
                    );
                });
    }
}