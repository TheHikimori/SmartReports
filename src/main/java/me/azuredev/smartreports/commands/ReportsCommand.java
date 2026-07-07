package me.azuredev.smartreports.commands;

import me.azuredev.smartreports.gui.MainMenuGUI;
import me.azuredev.smartreports.subcommands.CommentTicketCommand;
import me.azuredev.smartreports.subcommands.DeleteTicketCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReportsCommand implements CommandExecutor {

    private final MainMenuGUI gui;

    private final DeleteTicketCommand deleteTicketCommand;
    private final CommentTicketCommand commentTicketCommand;

    public ReportsCommand(
            MainMenuGUI gui,
            DeleteTicketCommand deleteTicketCommand,
            CommentTicketCommand commentTicketCommand
    ) {

        this.gui = gui;
        this.deleteTicketCommand = deleteTicketCommand;
        this.commentTicketCommand = commentTicketCommand;
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

        if (args.length == 0) {

            gui.open(player);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "delete" -> {

                if (args.length < 2) {

                    sender.sendMessage(
                            "§cИспользование: /reports delete <id>"
                    );

                    return true;
                }

                deleteTicketCommand.execute(
                        sender,
                        Long.parseLong(args[1])
                );
            }

            case "comment" -> {

                if (args.length < 3) {

                    sender.sendMessage(
                            "§cИспользование: /reports comment <id> <текст>"
                    );

                    return true;
                }

                String comment =
                        String.join(
                                " ",
                                Arrays.copyOfRange(
                                        args,
                                        2,
                                        args.length
                                )
                        );

                commentTicketCommand.execute(
                        sender,
                        Long.parseLong(args[1]),
                        comment
                );
            }

            default -> gui.open(player);
        }

        return true;
    }
}