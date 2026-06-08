package me.azuredev.smartreports.commands;

import me.azuredev.smartreports.gui.MyReportsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyReportsCommand implements CommandExecutor {

    private final MyReportsGUI gui;

    public MyReportsCommand(MyReportsGUI gui) {
        this.gui = gui;
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

        gui.open(player);

        return true;
    }
}