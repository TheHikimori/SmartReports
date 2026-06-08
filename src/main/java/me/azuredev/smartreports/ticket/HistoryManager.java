package me.azuredev.smartreports.ticket;

import me.azuredev.smartreports.database.repository.HistoryRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HistoryManager {

    private final HistoryRepository repository;

    public HistoryManager(HistoryRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Void> addHistory(
            TicketHistory history
    ) {
        return repository.create(history);
    }

    public CompletableFuture<List<TicketHistory>> getHistory(
            long ticketId
    ) {
        return repository.findByTicket(ticketId);
    }
}