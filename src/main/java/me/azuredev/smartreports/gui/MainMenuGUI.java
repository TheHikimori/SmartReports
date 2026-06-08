package me.azuredev.smartreports.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainMenuGUI {

    private final TicketListGUI ticketListGUI;

    public MainMenuGUI(TicketListGUI ticketListGUI) {
        this.ticketListGUI = ticketListGUI;
    }

    public void open(Player player) {

        Gui gui = Gui.gui()
                .title(MiniMessage.miniMessage().deserialize("<gradient:#ffcc00:#ff6600>SmartReports</gradient>"))
                .rows(3)
                .create();

        // 1. Устанавливаем защиту на уровне GUI
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setDragAction(event -> event.setCancelled(true));
        gui.setOutsideClickAction(event -> event.setCancelled(true)); // На случай клика вне окна

        ItemStack item = new ItemStack(Material.BOOK);

        gui.setItem(13, ItemBuilder.from(item)
                .name(MiniMessage.miniMessage().deserialize("<gradient:#55ff55:#00aa00>Все тикеты</gradient>"))
                .asGuiItem(event -> {
                    // 2. Дополнительная страховка: отмена внутри клика
                    event.setCancelled(true);
                    ticketListGUI.open(player);
                })
        );

        gui.open(player);
    }
}