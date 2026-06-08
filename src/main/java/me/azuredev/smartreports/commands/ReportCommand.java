package me.azuredev.smartreports.commands;

import me.azuredev.smartreports.gui.CategorySelectGUI;
import me.azuredev.smartreports.managers.AntiSpamManager;
import me.azuredev.smartreports.report.ReportCreationContext;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

    private final AntiSpamManager antiSpamManager;
    private final CategorySelectGUI categorySelectGUI;

    public ReportCommand(
            AntiSpamManager antiSpamManager,
            CategorySelectGUI categorySelectGUI
    ) {

        this.antiSpamManager = antiSpamManager;
        this.categorySelectGUI = categorySelectGUI;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player))
            return true;

        long cooldown = 30_000;

        if (antiSpamManager.isOnCooldown(
                player.getUniqueId(),
                cooldown
        )) {

            long seconds =
                    antiSpamManager.getRemaining(
                            player.getUniqueId(),
                            cooldown
                    ) / 1000;

            player.sendMessage(
                    "§cПодождите " + seconds + " сек."
            );

            return true;
        }

        if (args.length < 2) {

            player.sendMessage(
                    "§cИспользование: /report <игрок> <причина>"
            );

            return true;
        }

        Player target =
                Bukkit.getPlayer(args[0]);

        if (target == null) {

            player.sendMessage(
                    "§cИгрок не найден."
            );

            return true;
        }

        if (target.equals(player)) {

            player.sendMessage(
                    "§cНельзя репортить самого себя."
            );

            return true;
        }

        String reason =
                String.join(
                        " ",
                        java.util.Arrays.copyOfRange(
                                args,
                                1,
                                args.length
                        )
                );

        ReportCreationContext context =
                new ReportCreationContext(
                        player,
                        target,
                        reason
                );

        categorySelectGUI.open(
                context
        );

        return true;
    }
}