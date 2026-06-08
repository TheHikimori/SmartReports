package me.azuredev.smartreports.database.repository;

import me.azuredev.smartreports.ticket.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface TicketRepository {

    CompletableFuture<Long> create(Ticket ticket);

    CompletableFuture<Void> update(Ticket ticket);

    CompletableFuture<Void> delete(long id);

    CompletableFuture<Optional<Ticket>> findById(long id);

    CompletableFuture<List<Ticket>> findAll();

    CompletableFuture<List<Ticket>> findByReporter(
            UUID reporter
    );

    CompletableFuture<Long> countActiveByReporter(
            UUID reporter
    );
}