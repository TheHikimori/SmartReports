package me.azuredev.smartreports.commands;

import me.azuredev.smartreports.report.ReportCreationContext;
import me.azuredev.smartreports.ticket.TicketCategory;
import me.azuredev.smartreports.ticket.TicketPriority;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestReportCommand implements CommandExecutor {

    private final ReportCreator reportCreator;

    public TestReportCommand(
            ReportCreator reportCreator
    ) {
        this.reportCreator = reportCreator;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("smartreports.test")) {
            player.sendMessage("§cНет прав.");
            return true;
        }

        ReportCreationContext context =
                new ReportCreationContext(
                        player,
                        player,
                        "Тестовый репорт"
                );

        context.setCategory(
                TicketCategory.OTHER
        );

        context.setPriority(
                TicketPriority.MEDIUM
        );

        reportCreator.create(
                context
        );

        player.sendMessage(
                "§aСоздан тестовый репорт."
        );

        return true;
    }
}