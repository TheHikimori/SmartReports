package me.azuredev.smartreports.notification;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NotificationManager {

    public void notifyStaff(String message) {

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!player.hasPermission("smartreports.staff"))
                continue;

            player.sendMessage(message);
        }
    }
}