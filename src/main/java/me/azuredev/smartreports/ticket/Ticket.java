package me.azuredev.smartreports.ticket;

import java.util.UUID;

public class Ticket {

    private long id;

    private UUID reporter;
    private UUID reportedPlayer;

    private String reason;

    private TicketStatus status;
    private TicketPriority priority;
    private TicketCategory category;

    private boolean cancelled;

    private UUID assignedStaff;

    private long createdAt;
    private long updatedAt;

    public Ticket() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // ИСПРАВЛЕННЫЙ МЕТОД: возвращает reportedPlayer
    public UUID getTarget() {
        return reportedPlayer;
    }

    public UUID getReporter() {
        return reporter;
    }

    public void setReporter(UUID reporter) {
        this.reporter = reporter;
    }

    public UUID getReportedPlayer() {
        return reportedPlayer;
    }

    public void setReportedPlayer(UUID reportedPlayer) {
        this.reportedPlayer = reportedPlayer;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public void setCategory(TicketCategory category) {
        this.category = category;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public UUID getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(UUID assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}