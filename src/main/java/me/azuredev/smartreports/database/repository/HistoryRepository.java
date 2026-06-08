package me.azuredev.smartreports.database.repository;

import me.azuredev.smartreports.ticket.TicketHistory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface HistoryRepository {

    CompletableFuture<Void> create(
            TicketHistory history
    );

    CompletableFuture<List<TicketHistory>> findByTicket(
            long ticketId
    );
}