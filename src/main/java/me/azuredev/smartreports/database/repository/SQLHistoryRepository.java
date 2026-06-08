package me.azuredev.smartreports.database.repository;

import me.azuredev.smartreports.database.AsyncExecutor;
import me.azuredev.smartreports.database.DatabaseManager;
import me.azuredev.smartreports.ticket.TicketHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SQLHistoryRepository implements HistoryRepository {

    private final DatabaseManager databaseManager;

    public SQLHistoryRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public CompletableFuture<Void> create(TicketHistory history) {

        return CompletableFuture.runAsync(() -> {

            try (
                    Connection connection =
                            databaseManager.getDataSource().getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(
                                    """
                                    INSERT INTO ticket_history(
                                        ticket_id,
                                        actor,
                                        action,
                                        description,
                                        timestamp
                                    )
                                    VALUES (?, ?, ?, ?, ?)
                                    """
                            )
            ) {

                statement.setLong(1, history.getTicketId());
                statement.setString(2, history.getActor());
                statement.setString(3, history.getAction());
                statement.setString(4, history.getDescription());
                statement.setLong(5, history.getTimestamp());

                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }, AsyncExecutor.getExecutor());
    }

    @Override
    public CompletableFuture<List<TicketHistory>> findByTicket(long ticketId) {

        return CompletableFuture.supplyAsync(() -> {

            List<TicketHistory> historyList =
                    new ArrayList<>();

            try (
                    Connection connection =
                            databaseManager.getDataSource().getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(
                                    """
                                    SELECT * FROM ticket_history
                                    WHERE ticket_id = ?
                                    ORDER BY timestamp DESC
                                    """
                            )
            ) {

                statement.setLong(1, ticketId);

                ResultSet rs =
                        statement.executeQuery();

                while (rs.next()) {

                    TicketHistory history =
                            new TicketHistory();

                    history.setId(
                            rs.getLong("id")
                    );

                    history.setTicketId(
                            rs.getLong("ticket_id")
                    );

                    history.setActor(
                            rs.getString("actor")
                    );

                    history.setAction(
                            rs.getString("action")
                    );

                    history.setDescription(
                            rs.getString("description")
                    );

                    history.setTimestamp(
                            rs.getLong("timestamp")
                    );

                    historyList.add(history);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return historyList;

        }, AsyncExecutor.getExecutor());
    }
}