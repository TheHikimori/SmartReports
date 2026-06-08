package me.azuredev.smartreports.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.azuredev.smartreports.report.ReportCreationContext;
import me.azuredev.smartreports.ticket.TicketCategory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

public class CategorySelectGUI {

    private final PrioritySelectGUI priorityGUI;

    public CategorySelectGUI(
            PrioritySelectGUI priorityGUI
    ) {
        this.priorityGUI = priorityGUI;
    }

    public void open(
            ReportCreationContext context
    ) {

        // Используем MiniMessage для создания градиентного заголовка
        Gui gui = Gui.gui()
                .title(MiniMessage.miniMessage().deserialize("<gradient:#ffcc00:#ff6600>Выберите категорию</gradient>"))
                .rows(3)
                .create();

        // Устанавливаем правильную защиту, которая не ломает кнопки
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setDragAction(event -> event.setCancelled(true));

        gui.setItem(
                10,
                ItemBuilder.from(Material.DIAMOND_SWORD)
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#55ff55:#00aa00>Читы</gradient>"))
                        .asGuiItem(event -> {
                            context.setCategory(TicketCategory.CHEATS);
                            priorityGUI.open(context);
                        })
        );

        gui.setItem(
                11,
                ItemBuilder.from(Material.TNT)
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#ff5555:#aa0000>Гриферство</gradient>"))
                        .asGuiItem(event -> {
                            context.setCategory(TicketCategory.GRIEF);
                            priorityGUI.open(context);
                        })
        );

        gui.setItem(
                12,
                ItemBuilder.from(Material.PAPER)
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#ffff55:#ffaa00>Оскорбления</gradient>"))
                        .asGuiItem(event -> {
                            context.setCategory(TicketCategory.INSULTS);
                            priorityGUI.open(context);
                        })
        );

        gui.setItem(
                14,
                ItemBuilder.from(Material.OAK_SIGN)
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#55ffff:#00aaaa>Реклама</gradient>"))
                        .asGuiItem(event -> {
                            context.setCategory(TicketCategory.ADVERTISEMENT);
                            priorityGUI.open(context);
                        })
        );

        gui.setItem(
                15,
                ItemBuilder.from(Material.BEACON)
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#ff55ff:#aa00aa>Абуз бага</gradient>"))
                        .asGuiItem(event -> {
                            context.setCategory(TicketCategory.BUG_ABUSE);
                            priorityGUI.open(context);
                        })
        );

        gui.setItem(
                16,
                ItemBuilder.from(Material.BARRIER)
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#aaaaaa:#555555>Другое</gradient>"))
                        .asGuiItem(event -> {
                            context.setCategory(TicketCategory.OTHER);
                            priorityGUI.open(context);
                        })
        );

        gui.open(
                context.getReporter()
        );
    }
}