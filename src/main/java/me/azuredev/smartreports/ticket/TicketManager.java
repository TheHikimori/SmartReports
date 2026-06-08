package me.azuredev.smartreports.ticket;

import me.azuredev.smartreports.database.repository.TicketRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TicketManager {

    private final TicketRepository repository;

    public TicketManager(TicketRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Long> createTicket(
            Ticket ticket
    ) {
        return repository.create(ticket);
    }

    public CompletableFuture<Void> updateTicket(
            Ticket ticket
    ) {
        return repository.update(ticket);
    }

    public CompletableFuture<Void> deleteTicket(
            long id
    ) {
        return repository.delete(id);
    }

    public CompletableFuture<List<Ticket>> getAllTickets() {
        return repository.findAll();
    }

    public CompletableFuture<Optional<Ticket>> getTicket(
            long id
    ) {
        return repository.findById(id);
    }

    // Для совместимости с другим кодом
    public CompletableFuture<Optional<Ticket>> findById(
            long id
    ) {
        return repository.findById(id);
    }

    public CompletableFuture<List<Ticket>> getTicketsByReporter(
            UUID reporter
    ) {
        return repository.findByReporter(reporter);
    }

    public CompletableFuture<Long> getActiveTicketsCount(
            UUID reporter
    ) {
        return repository.countActiveByReporter(reporter);
    }

    public CompletableFuture<Long> countActiveTickets(
            UUID reporter
    ) {
        return repository.countActiveByReporter(reporter);
    }

    public CompletableFuture<Boolean> exists(
            long id
    ) {
        return repository.findById(id)
                .thenApply(Optional::isPresent);
    }

    public CompletableFuture<Void> claimTicket(
            Ticket ticket,
            UUID staff
    ) {

        ticket.setAssignedStaff(staff);
        ticket.setStatus(TicketStatus.CLAIMED);
        ticket.setUpdatedAt(System.currentTimeMillis());

        return updateTicket(ticket);
    }

    public CompletableFuture<Void> resolveTicket(
            Ticket ticket
    ) {

        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setUpdatedAt(System.currentTimeMillis());

        return updateTicket(ticket);
    }

    public CompletableFuture<Void> closeTicket(
            Ticket ticket
    ) {

        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setUpdatedAt(System.currentTimeMillis());

        return updateTicket(ticket);
    }
}