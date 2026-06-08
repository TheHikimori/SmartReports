package me.azuredev.smartreports.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.azuredev.smartreports.ticket.HistoryManager;
import me.azuredev.smartreports.ticket.Ticket;
import me.azuredev.smartreports.ticket.TicketManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class TicketListGUI {

    private final TicketManager ticketManager;
    private final TicketViewGUI ticketViewGUI;

    public TicketListGUI(
            TicketManager ticketManager,
            HistoryManager historyManager
    ) {

        this.ticketManager = ticketManager;

        HistoryGUI historyGUI =
                new HistoryGUI(
                        historyManager
                );

        this.ticketViewGUI =
                new TicketViewGUI(
                        ticketManager,
                        historyManager,
                        historyGUI
                );
    }

    public void open(Player player) {

        ticketManager.getAllTickets().thenAccept(tickets -> {
            // Переносим открытие GUI в основной поток сервера
            Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(TicketListGUI.class), () -> {

                Gui gui = Gui.gui()
                        .title(MiniMessage.miniMessage().deserialize("<gradient:#55ffff:#5555ff>Все тикеты</gradient>"))
                        .rows(6)
                        .create();

                // "Мертвая" защита: блокируем любые клики и перетаскивания
                gui.setDefaultClickAction(event -> event.setCancelled(true));
                gui.setDragAction(event -> event.setCancelled(true));

                int slot = 0;

                for (Ticket ticket : tickets) {

                    if (slot >= 54) {
                        break;
                    }

                    ItemStack item =
                            new ItemStack(Material.BOOK);

                    ItemMeta meta =
                            item.getItemMeta();

                    meta.displayName(
                            MiniMessage.miniMessage().deserialize("<gradient:#ffaa00:#ff5555>Тикет #" + ticket.getId() + "</gradient>")
                    );

                    meta.lore(List.of(
                            Component.text("Причина: ", NamedTextColor.GRAY)
                                    .append(Component.text(ticket.getReason(), NamedTextColor.WHITE)),
                            Component.text("Статус: ", NamedTextColor.GRAY)
                                    .append(Component.text(ticket.getStatus().toString(), NamedTextColor.YELLOW))
                    ));

                    item.setItemMeta(meta);

                    gui.setItem(
                            slot++,
                            ItemBuilder.from(item)
                                    .asGuiItem(event -> {
                                        event.setCancelled(true); // Дополнительная защита
                                        ticketViewGUI.open(
                                                player,
                                                ticket
                                        );
                                    })
                    );
                }

                gui.open(player);
            });
        });
    }
}