package me.azuredev.smartreports.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.azuredev.smartreports.ticket.HistoryManager;
import me.azuredev.smartreports.ticket.Ticket;
import me.azuredev.smartreports.ticket.TicketHistory;
import me.azuredev.smartreports.ticket.TicketManager;
import me.azuredev.smartreports.ticket.TicketStatus;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TicketViewGUI {

    private final TicketManager ticketManager;
    private final HistoryManager historyManager;
    private final HistoryGUI historyGUI;

    public TicketViewGUI(
            TicketManager ticketManager,
            HistoryManager historyManager,
            HistoryGUI historyGUI
    ) {
        this.ticketManager = ticketManager;
        this.historyManager = historyManager;
        this.historyGUI = historyGUI;
    }


    public void open(Player player, Ticket ticket) {

        Gui gui = Gui.gui()
                .title(MiniMessage.miniMessage().deserialize("<gradient:#55ffff:#5555ff>Тикет #" + ticket.getId() + "</gradient>"))
                .rows(3)
                .create();

        // Безопасная блокировка взаимодействий
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setDragAction(event -> event.setCancelled(true));

        // Взять тикет
        gui.setItem(
                11,
                ItemBuilder.from(new ItemStack(Material.LIME_DYE))
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#55ff55:#00aa00>Взять</gradient>"))
                        .asGuiItem(event -> {

                            ticket.setStatus(TicketStatus.CLAIMED);
                            ticket.setAssignedStaff(player.getUniqueId());
                            ticket.setUpdatedAt(System.currentTimeMillis());

                            ticketManager.updateTicket(ticket);

                            TicketHistory history = new TicketHistory();
                            history.setTicketId(ticket.getId());
                            history.setActor(player.getName());
                            history.setAction("CLAIM");
                            history.setDescription("Тикет взят в работу");
                            history.setTimestamp(System.currentTimeMillis());

                            historyManager.addHistory(history);

                            player.sendMessage(Component.text("Тикет взят в работу.", NamedTextColor.GREEN));
                            gui.close(player);
                        })
        );

        // Решить тикет
        gui.setItem(
                13,
                ItemBuilder.from(new ItemStack(Material.EMERALD))
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#00ff00:#00aa00>Решить</gradient>"))
                        .asGuiItem(event -> {

                            ticket.setStatus(TicketStatus.RESOLVED);
                            ticket.setUpdatedAt(System.currentTimeMillis());

                            ticketManager.updateTicket(ticket);

                            TicketHistory history = new TicketHistory();
                            history.setTicketId(ticket.getId());
                            history.setActor(player.getName());
                            history.setAction("RESOLVE");
                            history.setDescription("Тикет решён");
                            history.setTimestamp(System.currentTimeMillis());

                            historyManager.addHistory(history);

                            player.sendMessage(Component.text("Тикет решён.", NamedTextColor.GREEN));
                            gui.close(player);
                        })
        );

        // Закрыть тикет
        gui.setItem(
                15,
                ItemBuilder.from(new ItemStack(Material.BARRIER))
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#ff5555:#aa0000>Закрыть</gradient>"))
                        .asGuiItem(event -> {

                            ticket.setStatus(TicketStatus.CLOSED);
                            ticket.setUpdatedAt(System.currentTimeMillis());

                            ticketManager.updateTicket(ticket);

                            TicketHistory history = new TicketHistory();
                            history.setTicketId(ticket.getId());
                            history.setActor(player.getName());
                            history.setAction("CLOSE");
                            history.setDescription("Тикет закрыт");
                            history.setTimestamp(System.currentTimeMillis());

                            historyManager.addHistory(history);

                            player.sendMessage(Component.text("Тикет закрыт.", NamedTextColor.RED));
                            gui.close(player);
                        })
        );

        gui.setItem(
                22,
                ItemBuilder.from(Material.BOOK)
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#aaaaaa:#555555>История</gradient>"))
                        .asGuiItem(event -> historyGUI.open(player, ticket.getId()))
        );

        gui.open(player);
    }
}