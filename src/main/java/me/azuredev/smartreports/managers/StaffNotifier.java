package me.azuredev.smartreports.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffNotifier {

    public void notifyNewTicket(
            long ticketId,
            String reporter,
            String target
    ) {

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!player.hasPermission(
                    "smartreports.staff"
            )) {
                continue;
            }

            player.sendMessage(
                    "§c⚠ Новый репорт #" + ticketId
                            + " §7от §e" + reporter
                            + " §7на §c" + target
            );
        }
    }
}