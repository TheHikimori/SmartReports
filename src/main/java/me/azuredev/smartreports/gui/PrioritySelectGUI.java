package me.azuredev.smartreports.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.azuredev.smartreports.commands.ReportCreator;
import me.azuredev.smartreports.report.ReportCreationContext;
import me.azuredev.smartreports.ticket.TicketPriority;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

public class PrioritySelectGUI {

    private final ReportCreator reportCreator;

    public PrioritySelectGUI(
            ReportCreator reportCreator
    ) {
        this.reportCreator = reportCreator;
    }

    public void open(
            ReportCreationContext context
    ) {

        Gui gui = Gui.gui()
                .title(MiniMessage.miniMessage().deserialize("<gradient:#ffcc00:#ff6600>Выберите приоритет</gradient>"))
                .rows(3)
                .create();

        // Безопасная блокировка взаимодействий
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setDragAction(event -> event.setCancelled(true));

        gui.setItem(
                11,
                ItemBuilder.from(Material.RED_DYE)
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#ff5555:#aa0000>Высокий</gradient>"))
                        .asGuiItem(event -> {

                            context.setPriority(
                                    TicketPriority.HIGH
                            );

                            gui.close(
                                    context.getReporter()
                            );

                            reportCreator.create(
                                    context
                            );
                        })
        );

        gui.setItem(
                13,
                ItemBuilder.from(Material.YELLOW_DYE)
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#ffff55:#ffaa00>Средний</gradient>"))
                        .asGuiItem(event -> {

                            context.setPriority(
                                    TicketPriority.MEDIUM
                            );

                            gui.close(
                                    context.getReporter()
                            );

                            reportCreator.create(
                                    context
                            );
                        })
        );

        gui.setItem(
                15,
                ItemBuilder.from(Material.LIME_DYE)
                        .name(MiniMessage.miniMessage().deserialize("<gradient:#55ff55:#00aa00>Низкий</gradient>"))
                        .asGuiItem(event -> {

                            context.setPriority(
                                    TicketPriority.LOW
                            );

                            gui.close(
                                    context.getReporter()
                            );

                            reportCreator.create(
                                    context
                            );
                        })
        );

        gui.open(
                context.getReporter()
        );
    }
}