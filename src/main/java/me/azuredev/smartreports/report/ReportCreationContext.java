package me.azuredev.smartreports.report;

import me.azuredev.smartreports.ticket.TicketCategory;
import me.azuredev.smartreports.ticket.TicketPriority;
import org.bukkit.entity.Player;

public class ReportCreationContext {

    private final Player reporter;
    private final Player target;
    private final String reason;

    private TicketCategory category;
    private TicketPriority priority;

    public ReportCreationContext(
            Player reporter,
            Player target,
            String reason
    ) {
        this.reporter = reporter;
        this.target = target;
        this.reason = reason;
    }

    public Player getReporter() {
        return reporter;
    }

    public Player getTarget() {
        return target;
    }

    public String getReason() {
        return reason;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public void setCategory(
            TicketCategory category
    ) {
        this.category = category;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(
            TicketPriority priority
    ) {
        this.priority = priority;
    }
}