package me.azuredev.smartreports.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.azuredev.smartreports.ticket.HistoryManager;
import me.azuredev.smartreports.ticket.TicketHistory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryGUI {

    private final HistoryManager historyManager;

    public HistoryGUI(
            HistoryManager historyManager
    ) {
        this.historyManager = historyManager;
    }

    public void open(
            Player player,
            long ticketId
    ) {

        historyManager.getHistory(ticketId)
                .thenAccept(historyList -> {
                    // Используем scheduler для безопасного открытия в основном потоке
                    Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(HistoryGUI.class), () -> {

                        Gui gui = Gui.gui()
                                .title(MiniMessage.miniMessage().deserialize("<gradient:#55ffff:#5555ff>История #" + ticketId + "</gradient>"))
                                .rows(6)
                                .create();

                        // Правильная защита, блокирующая клики, но не ломающая интерфейс
                        gui.setDefaultClickAction(event -> event.setCancelled(true));
                        gui.setDragAction(event -> event.setCancelled(true));

                        int slot = 0;

                        for (TicketHistory history : historyList) {

                            if (slot >= 54) {
                                break;
                            }

                            String date =
                                    new SimpleDateFormat(
                                            "dd.MM.yyyy HH:mm"
                                    ).format(
                                            new Date(
                                                    history.getTimestamp()
                                            )
                                    );

                            gui.setItem(
                                    slot++,
                                    ItemBuilder.from(
                                                    Material.PAPER
                                            )
                                            .name(
                                                    MiniMessage.miniMessage().deserialize("<gradient:#ffaa00:#ff5555>" + history.getAction() + "</gradient>")
                                            )
                                            .lore(
                                                    Component.text("Исполнитель: " + history.getActor(), net.kyori.adventure.text.format.NamedTextColor.GRAY),
                                                    Component.text(history.getDescription(), net.kyori.adventure.text.format.NamedTextColor.WHITE),
                                                    Component.text(date, net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY)
                                            )
                                            .asGuiItem()
                            );
                        }

                        gui.open(player);
                    });
                });
    }
}