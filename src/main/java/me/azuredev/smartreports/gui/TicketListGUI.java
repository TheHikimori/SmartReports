    package me.azuredev.smartreports.gui;

    import dev.triumphteam.gui.builder.item.ItemBuilder;
    import dev.triumphteam.gui.guis.Gui;
    import me.azuredev.smartreports.ticket.HistoryManager;
    import me.azuredev.smartreports.ticket.Ticket;
    import me.azuredev.smartreports.ticket.TicketManager;
    import net.kyori.adventure.text.Component;
    import net.kyori.adventure.text.minimessage.MiniMessage;
    import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
    import org.bukkit.Bukkit;
    import org.bukkit.Material;
    import org.bukkit.entity.Player;
    import org.bukkit.plugin.java.JavaPlugin;

    import java.util.ArrayList;
    import java.util.List;

    public class TicketListGUI {

        private final TicketManager ticketManager;
        private final TicketViewGUI ticketViewGUI;
        private MainMenuGUI mainMenuGUI;


        private static final int[] TICKET_SLOTS = {
                11,12,13,14,15,
                20,21,22,23,24,
                29,30,31,32,33,
                38,39,40,41,42
        };

        public TicketListGUI(
                TicketManager ticketManager,
                HistoryManager historyManager

        ) {

            this.ticketManager = ticketManager;



            HistoryGUI historyGUI =
                    new HistoryGUI(historyManager);

            this.ticketViewGUI =
                    new TicketViewGUI(
                            ticketManager,
                            historyManager,
                            historyGUI
                    );
        }

        public void setMainMenuGUI(MainMenuGUI mainMenuGUI) {
            this.mainMenuGUI = mainMenuGUI;
        }

        public void open(Player player) {

            String playerRank = "Owner";
            String rankColor = "#FFCC00"; // Здесь может быть твоя логика выбора цвета


            ticketManager.getAllTickets().thenAccept(tickets -> {

                Bukkit.getScheduler().runTask(
                        JavaPlugin.getProvidingPlugin(
                                TicketListGUI.class
                        ),
                        () -> {

                            Gui gui = Gui.gui()
                                    .title(
                                            MiniMessage.miniMessage()
                                                    .deserialize(
                                                            "<gradient:#ffcc00:#ff6600>Список репортов</gradient>"
                                                    )
                                    )
                                    .rows(6)
                                    .create();

                            gui.setDefaultClickAction(
                                    event -> event.setCancelled(true)
                            );

                            gui.setDragAction(
                                    event -> event.setCancelled(true)
                            );

                            gui.setOutsideClickAction(
                                    event -> event.setCancelled(true)
                            );

                            fillDecoration(gui);

                            List<Component> statsLore =
                                    new ArrayList<>();

                            statsLore.add(Component.text(""));
                            statsLore.add(
                                    MiniMessage.miniMessage()
                                            .deserialize(
                                                    "<color:#AAAAAA>★ Ник: "
                                                            + player.getName()
                                                            + "</color>"
                                            )
                            );

                           // 2. Формируем компонент ранга с нужным цветом
                            Component coloredRankComponent = MiniMessage.miniMessage().deserialize(
                                    "<color:" + rankColor + "><bold>" + playerRank + "</bold></color>"
                            );

                           // 3. Вставляем этот готовый "цветной" компонент в плейсхолдер
                            statsLore.add(
                                    MiniMessage.miniMessage().deserialize(
                                            "<color:#AAAAAA><bold>🧪 Ранг:</bold></color> <rank>",
                                            Placeholder.component("rank", coloredRankComponent)
                                    )
                            );

                            statsLore.add(
                                    MiniMessage.miniMessage()
                                            .deserialize(
                                                    "<color:#AAAAAA><bold>🛡 Репутация:</bold></color> <color:#55FF55>★★★★★ (Идеальная)</color>"
                                            )
                            );

                            statsLore.add(Component.text(""));
                            statsLore.add(Component.text(""));

                            gui.setItem(
                                    4,
                                    ItemBuilder.from(
                                                    Material.PLAYER_HEAD
                                            )
                                            .name(
                                                    Component.text(
                                                            "Ваша статистика"
                                                    )
                                            )
                                            .lore(statsLore)
                                            .asGuiItem()
                            );

                            int index = 0;

                            for (Ticket ticket : tickets) {

                                if (index >= TICKET_SLOTS.length) {
                                    break;
                                }

                                List<Component> lore =
                                        new ArrayList<>();

                                lore.add(
                                        MiniMessage.miniMessage()
                                                .deserialize(
                                                        "<color:#AAAAAA>★ Репортёр: "
                                                                + ticket.getReporter()
                                                                + "</color>"
                                                )
                                );

                                lore.add(
                                        MiniMessage.miniMessage()
                                                .deserialize(
                                                        "<color:#AAAAAA>★ Причина: "
                                                                + ticket.getReason()
                                                                + "</color>"
                                                )
                                );

                                lore.add(
                                        MiniMessage.miniMessage()
                                                .deserialize(
                                                        "<color:#AAAAAA>★ Статус: "
                                                                + ticket.getStatus()
                                                                + "</color>"
                                                )
                                );

                                lore.add(
                                        MiniMessage.miniMessage()
                                                .deserialize(
                                                        "<color:#AAAAAA>🛡 ID: #"
                                                                + ticket.getId()
                                                                + "</color>"
                                                )
                                );

                                lore.add(
                                        MiniMessage.miniMessage()
                                                .deserialize(
                                                        "<gray>(Нажмите для просмотра)</gray>"
                                                )
                                );

                                gui.setItem(
                                        TICKET_SLOTS[index],
                                        ItemBuilder.from(
                                                        Material.PLAYER_HEAD
                                                )
                                                .name(
                                                        MiniMessage.miniMessage()
                                                                .deserialize(
                                                                        "<yellow>"
                                                                                + ticket.getTarget()
                                                                                + "</yellow>"
                                                                )
                                                )
                                                .lore(lore)
                                                .asGuiItem(event -> {

                                                    event.setCancelled(true);

                                                    ticketViewGUI.open(
                                                            player,
                                                            ticket
                                                    );
                                                })
                                );

                                index++;
                            }

                            gui.open(player);
                        }
                );
            });
        }

        private void fillDecoration(Gui gui) {

            int[] blackSlots = {
                    2,3,5,6,
                    10,16,
                    18,19,25,26,
                    27,28,34,35,
                    37,43,
                    48,50
            };

            int[] graySlots = {
                    1,7,
                    9,17,
                    36,44,
                    46,52
            };

            int[] lanternSlots = {
                    0,8,
                    45,53
            };

            for (int slot : blackSlots) {

                gui.setItem(
                        slot,
                        ItemBuilder.from(
                                        Material.BLACK_STAINED_GLASS_PANE
                                )
                                .name(Component.text(" "))
                                .asGuiItem()
                );
            }

            for (int slot : graySlots) {

                gui.setItem(
                        slot,
                        ItemBuilder.from(
                                        Material.LIGHT_GRAY_STAINED_GLASS_PANE
                                )
                                .name(Component.text(" "))
                                .asGuiItem()
                );
            }

            for (int slot : lanternSlots) {

                gui.setItem(
                        slot,
                        ItemBuilder.from(
                                        Material.LANTERN
                                )
                                .name(Component.text(" "))
                                .asGuiItem()
                );
            }

            gui.setItem(
                    47,
                    ItemBuilder.from(
                                    Material.ARROW
                            )
                            .name(
                                    MiniMessage.miniMessage()
                                            .deserialize(
                                                    "<yellow>Предыдущая страница</yellow>"
                                            )
                            )
                            .asGuiItem()
            );

            gui.setItem(
                    49,
                    ItemBuilder.from(
                                    Material.CONDUIT
                            )
                            .name(
                                    MiniMessage.miniMessage()
                                            .deserialize(
                                                    "<bold><gradient:#FFDD00:#FFAE00>ВЕРНУТЬСЯ ОБРАТНО</gradient></bold>"
                                            )
                            )
                            .asGuiItem(event -> {

                                event.setCancelled(true);

                                mainMenuGUI.open(
                                        (Player) event.getWhoClicked()
                                );
                            })
            );

            gui.setItem(
                    51,
                    ItemBuilder.from(
                                    Material.ARROW
                            )
                            .name(
                                    MiniMessage.miniMessage()
                                            .deserialize(
                                                    "<yellow>Следующая страница</yellow>"
                                            )
                            )
                            .asGuiItem()
            );
        }
    }