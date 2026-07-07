package me.azuredev.smartreports.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MainMenuGUI {

    private final TicketListGUI ticketListGUI;

    public MainMenuGUI(TicketListGUI ticketListGUI) {
        this.ticketListGUI = ticketListGUI;
    }

    public void open(Player player) {

        // ДИНАМИЧЕСКИЕ ДАННЫЕ
        String playerName = player.getName();
        // Допустим, у тебя есть переменная с HEX-кодом цвета
        String rankColor = "#55FF55"; // Зеленый цвет для примера
        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());

        String playerRank = "Игрок"; // Дефолтное значение

        if (user != null) {
            // Получаем основную группу
            String groupName = user.getPrimaryGroup();
            // Делаем первую букву заглавной для красоты
            playerRank = groupName.substring(0, 1).toUpperCase() + groupName.substring(1);
        }
        String repStatus = "★★★★★ (Идеальная)";

        Gui gui = Gui.gui()
                .title(MiniMessage.miniMessage().deserialize("<gradient:#FF6D00:#EFB575>SmartReports</gradient>"))
                .rows(6) // 54 слота = 6 рядов
                .create();

        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setDragAction(event -> event.setCancelled(true));
        gui.setOutsideClickAction(event -> event.setCancelled(true));

        // Заполнение по слотам согласно вашей конфигурации
        addGuiItem(gui, 0, Material.LANTERN, "", null);
        addGuiItem(gui, 1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 2, Material.BLACK_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 3, Material.BLACK_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 4, Material.MOJANG_BANNER_PATTERN, "<bold>Правила сервера</bold>", null);
        addGuiItem(gui, 5, Material.BLACK_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 6, Material.BLACK_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 7, Material.LIGHT_GRAY_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 8, Material.LANTERN, "", null);

        addGuiItem(gui, 9, Material.LIGHT_GRAY_STAINED_GLASS_PANE, "", null);
        for (int i = 10; i <= 16; i++) addGuiItem(gui, i, Material.BLACK_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 17, Material.LIGHT_GRAY_STAINED_GLASS_PANE, "", null);

        addGuiItem(gui, 18, Material.BLACK_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 19, Material.BLACK_STAINED_GLASS_PANE, "", null);

        TextColor color = TextColor.fromHexString(rankColor != null ? rankColor : "#AAAAAA");

        // Слот 20 - Динамический ранг
        gui.setItem(
                20,
                ItemBuilder.from(Material.BARRIER)
                        .name(MiniMessage.miniMessage().deserialize("<color:#AA0000><bold>Репорты</bold></color>"))
                        .lore(
                                List.of(
                                        MiniMessage.miniMessage().deserialize("<color:#AAAAAA>Доступны только с ранга \"Хелпер\"</color>"),
                                        MiniMessage.miniMessage().deserialize(
                                                "<color:#AAAAAA>Ваш ранг: </color><rank>",
                                                Placeholder.component("rank",
                                                        Component.text(playerRank)
                                                                .color(color) // Применяем цвет динамически
                                                                .decoration(TextDecoration.BOLD, true)
                                                )
                                        )
                                )
                        )
                        .asGuiItem(event -> {
                            event.setCancelled(true);
                            ticketListGUI.open(player);
                        })
        );

        addGuiItem(gui, 21, Material.BLACK_STAINED_GLASS_PANE, "", null);

        // Слот 22 - Динамический ник, ранг и репутация
        addGuiItem(gui, 22, Material.PLAYER_HEAD, "Ваша статистика", List.of("", "<color:#AAAAAA>★ Ник: " + playerName + "</color>", "<color:#AAAAAA><bold>🧪 Ранг:</bold></color><color:#AAAAAA> </color><color:#AA0000><bold>" + playerRank + "</bold></color>", "<color:#AAAAAA><bold>🛡 Репутация:</bold></color><color:#AAAAAA> </color><color:#55FF55>" + repStatus + "</color>", "", ""));

        addGuiItem(gui, 23, Material.BLACK_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 24, Material.WRITABLE_BOOK, "<color:#FFAA00><bold>Кинуть на игрока репорт</bold></color>", List.of("", "<color:#AAAAAA>Репорт кидается по правилу</color>", "<color:#AAAAAA>Тупой или неуместный репорт = снятие репорта и бан на кидание репортов</color>"));
        addGuiItem(gui, 25, Material.BLACK_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 26, Material.BLACK_STAINED_GLASS_PANE, "", null);

        for (int i = 27; i <= 35; i++) addGuiItem(gui, i, Material.BLACK_STAINED_GLASS_PANE, "", null);

        addGuiItem(gui, 36, Material.LIGHT_GRAY_STAINED_GLASS_PANE, "", null);
        for (int i = 37; i <= 39; i++) addGuiItem(gui, i, Material.BLACK_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 40, Material.CONDUIT, "<bold><gradient:#FFDD00:#FFAE00>ВЕРНУТСЯ ОБРАТНО</gradient></bold>", null);
        for (int i = 41; i <= 43; i++) addGuiItem(gui, i, Material.BLACK_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 44, Material.LIGHT_GRAY_STAINED_GLASS_PANE, "", null);

        addGuiItem(gui, 45, Material.LANTERN, "", null);
        addGuiItem(gui, 46, Material.LIGHT_GRAY_STAINED_GLASS_PANE, "", null);
        for (int i = 47; i <= 51; i++) addGuiItem(gui, i, Material.BLACK_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 52, Material.LIGHT_GRAY_STAINED_GLASS_PANE, "", null);
        addGuiItem(gui, 53, Material.LANTERN, "", null);

        gui.open(player);
    }

    private void addGuiItem(Gui gui, int slot, Material material, String name, List<String> lore) {
        ItemBuilder builder = ItemBuilder.from(new ItemStack(material));
        if (name != null && !name.isEmpty()) builder.name(MiniMessage.miniMessage().deserialize(name));
        if (lore != null) builder.lore(lore.stream().map(s -> MiniMessage.miniMessage().deserialize(s)).toList());

        gui.setItem(slot, builder.asGuiItem());
    }
}