package me.azuredev.smartreports.commands;

import me.azuredev.smartreports.gui.MainMenuGUI;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class ReportsCommand implements CommandExecutor {

    private final MainMenuGUI gui;

    public ReportsCommand(
            MainMenuGUI gui
    ) {
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