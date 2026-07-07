package me.azuredev.smartreports.telegram;

import me.azuredev.smartreports.SmartReportsPlugin;
import me.azuredev.smartreports.ticket.Ticket;
import me.azuredev.smartreports.ticket.TicketManager;
import me.azuredev.smartreports.ticket.TicketStatus;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SmartReportsBot
        extends TelegramLongPollingBot {

    private final SmartReportsPlugin plugin;
    private final TicketManager ticketManager;

    public SmartReportsBot(
            SmartReportsPlugin plugin,
            TicketManager ticketManager
    ) {
        this.plugin = plugin;
        this.ticketManager = ticketManager;
    }

    @Override
    public String getBotUsername() {
        return "SmartReportsBot";
    }

    @Override
    public String getBotToken() {
        return plugin.getConfig()
                .getString("telegram.bot-token");
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasCallbackQuery()) {
            return;
        }

        try {
            AnswerCallbackQuery answer = new AnswerCallbackQuery();
            answer.setCallbackQueryId(update.getCallbackQuery().getId());
            execute(answer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CallbackQuery query = update.getCallbackQuery();
        String data =
                query.getData();

        String[] split =
                data.split(":");

        if (split.length != 2) {
            return;
        }

        String action =
                split[0];

        long ticketId =
                Long.parseLong(split[1]);

        final String userName = (query.getFrom().getUserName() != null)
                ? query.getFrom().getUserName()
                : query.getFrom().getFirstName();

        MaybeInaccessibleMessage maybeMessage = query.getMessage();
        final Message finalCallbackMessage = (maybeMessage instanceof Message) ? (Message) maybeMessage : null;

        ticketManager.getTicket(ticketId)
                .thenAccept(optional -> {

                    if (optional.isEmpty() || finalCallbackMessage == null) {
                        return;
                    }

                    // ЗАЩИТА: Не даем брать тикет, если он уже CLAIMED (взят)
                    if (action.equals("claim") && optional.get().getStatus() == TicketStatus.CLAIMED) {
                        return;
                    }

                    Ticket ticket =
                            optional.get();

                    String statusText = "";
                    String assignee = "";

                    switch (action) {

                        case "claim" -> {
                            if (ticket.getAssignedStaff() != null) {
                                sendAlert(query.getId(), "❌ Этот тикет уже занят другим сотрудником!");
                                return;
                            }

                            ticket.setStatus(TicketStatus.CLAIMED);
                            // Записываем ID того, кто взял
                            ticket.setAssignedStaff(UUID.nameUUIDFromBytes(userName.getBytes()));
                            ticketManager.updateTicket(ticket);

                            statusText = "\n\n🟡 Тикет #" + ticketId + " взят (@" + userName + ")";
                            assignee = "\nИсполнитель: @" + userName;
                        }

                        case "resolve", "close" -> {
                            // Создаем UUID текущего пользователя для проверки
                            UUID currentUserUuid = UUID.nameUUIDFromBytes(userName.getBytes());

                            // 1. Защита: Если тикет вообще не взят, кнопки не работают
                            if (ticket.getAssignedStaff() == null) {
                                sendAlert(query.getId(), "❌ Сначала нужно взять тикет!");
                                return;
                            }

                            // 2. Защита: Если ID того, кто нажал, НЕ совпадает с тем, кто взял - блокируем
                            if (!ticket.getAssignedStaff().equals(currentUserUuid)) {
                                sendAlert(query.getId(), "❌ Этот тикет взял другой сотрудник!");
                                return;
                            }

                            // Логика действий
                            if (action.equals("resolve")) {
                                ticket.setStatus(TicketStatus.RESOLVED);
                                statusText = "\n\n🟢 Тикет #" + ticketId + " решён (@" + userName + ")";
                            } else {
                                ticket.setStatus(TicketStatus.CLOSED);
                                statusText = "\n\n🔴 Тикет #" + ticketId + " закрыт (@" + userName + ")";
                            }
                            ticketManager.updateTicket(ticket);
                        }
                    }

                    try {
                        EditMessageText edit = new EditMessageText();
                        edit.setChatId(finalCallbackMessage.getChatId().toString());
                        edit.setMessageId(finalCallbackMessage.getMessageId());

                        // ОЧИСТКА: Удаляем старый статус
                        String originalText = finalCallbackMessage.getText() != null ? finalCallbackMessage.getText() : "";
                        String baseText = originalText.split("\n\n")[0];

                        // Если тикет взяли, добавляем строку исполнителя в верхнюю часть
                        if (action.equals("claim")) {
                            baseText = baseText.replace("Причина:", "Исполнитель: @" + userName + "\n\nПричина:");
                        }

                        edit.setText(baseText + statusText);

                        // Сохраняем кнопки, если тикет не закрыт
                        if (!action.equals("close")) {
                            if (finalCallbackMessage.getReplyMarkup() instanceof InlineKeyboardMarkup) {
                                edit.setReplyMarkup((InlineKeyboardMarkup) finalCallbackMessage.getReplyMarkup());
                            }
                        }

                        execute(edit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void sendTelegramMessage(
            String text,
            String chatId
    ) {

        try {

            SendMessage message =
                    new SendMessage();

            message.setChatId(chatId);
            message.setText(text);

            execute(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTicket(
            long id,
            String reporter,
            String target,
            String reason
    ) {

        try {

            SendMessage message =
                    new SendMessage();

            message.setChatId(
                    plugin.getConfig()
                            .getString(
                                    "telegram.chat-id"
                            )
            );

            message.setText(
                    """
                    🚨 Новый репорт
                    
                    ID: %s
                    Репортёр: %s
                    Нарушитель: %s
                    Работать будет: Драйф1
                    
                    Причина:
                    %s
                    """
                            .formatted(
                                    id,
                                    reporter,
                                    target,
                                    reason
                            )
            );

            InlineKeyboardMarkup markup =
                    new InlineKeyboardMarkup();

            List<List<InlineKeyboardButton>> rows =
                    new ArrayList<>();

            List<InlineKeyboardButton> row =
                    new ArrayList<>();

            InlineKeyboardButton claim =
                    new InlineKeyboardButton();

            claim.setText("🟡 Взять");
            claim.setCallbackData(
                    "claim:" + id
            );

            InlineKeyboardButton resolve =
                    new InlineKeyboardButton();

            resolve.setText("🟢 Решить");
            resolve.setCallbackData(
                    "resolve:" + id
            );

            InlineKeyboardButton close =
                    new InlineKeyboardButton();

            close.setText("🔴 Закрыть");
            close.setCallbackData(
                    "close:" + id
            );

            row.add(claim);
            row.add(resolve);
            row.add(close);

            rows.add(row);

            markup.setKeyboard(rows);

            message.setReplyMarkup(markup);

            execute(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendComment(
            long ticketId,
            String author,
            String comment
    ) {

        try {

            SendMessage message =
                    new SendMessage();

            message.setChatId(
                    plugin.getConfig()
                            .getString(
                                    "telegram.chat-id"
                            )
            );

            message.setText(
                    """
                    💬 Новый комментарий
                    
                    Тикет: #%s
                    Автор: %s
                    
                    Комментарий:
                    %s
                    """
                            .formatted(
                                    ticketId,
                                    author,
                                    comment
                            )
            );

            execute(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAlert(String callbackQueryId, String text) {
        try {
            AnswerCallbackQuery alert = new AnswerCallbackQuery();
            alert.setCallbackQueryId(callbackQueryId);
            alert.setText(text);
            alert.setShowAlert(true);
            execute(alert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}