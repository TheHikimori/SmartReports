package me.azuredev.smartreports.database.repository;

import me.azuredev.smartreports.database.AsyncExecutor;
import me.azuredev.smartreports.database.DatabaseManager;
import me.azuredev.smartreports.ticket.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SQLTicketRepository implements TicketRepository {

    private final DatabaseManager databaseManager;

    public SQLTicketRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public CompletableFuture<Long> create(Ticket ticket) {

        return CompletableFuture.supplyAsync(() -> {

            String sql = """
                    INSERT INTO tickets(
                        reporter,
                        reported_player,
                        reason,
                        status,
                        priority,
                        category,
                        assigned_staff,
                        created_at,
                        updated_at
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            try (
                    Connection connection = databaseManager.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS
                    )
            ) {

                statement.setString(1, ticket.getReporter().toString());
                statement.setString(2, ticket.getReportedPlayer().toString());
                statement.setString(3, ticket.getReason());
                statement.setString(4, ticket.getStatus().name());
                statement.setString(5, ticket.getPriority().name());
                statement.setString(6, ticket.getCategory().name());

                if (ticket.getAssignedStaff() != null) {
                    statement.setString(7, ticket.getAssignedStaff().toString());
                } else {
                    statement.setNull(7, Types.VARCHAR);
                }

                statement.setLong(8, ticket.getCreatedAt());
                statement.setLong(9, ticket.getUpdatedAt());

                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (keys.next()) {
                        return keys.getLong(1);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1L;

        }, AsyncExecutor.getExecutor());
    }

    @Override
    public CompletableFuture<Optional<Ticket>> findById(long id) {

        return CompletableFuture.supplyAsync(() -> {

            try (
                    Connection connection = databaseManager.getDataSource().getConnection();
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    "SELECT * FROM tickets WHERE id = ?"
                            )
            ) {

                statement.setLong(1, id);

                try (ResultSet rs = statement.executeQuery()) {

                    if (rs.next()) {
                        return Optional.of(map(rs));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return Optional.empty();

        }, AsyncExecutor.getExecutor());
    }

    @Override
    public CompletableFuture<List<Ticket>> findAll() {

        return CompletableFuture.supplyAsync(() -> {

            List<Ticket> tickets = new ArrayList<>();

            try (
                    Connection connection = databaseManager.getDataSource().getConnection();
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    "SELECT * FROM tickets ORDER BY id DESC"
                            );
                    ResultSet rs = statement.executeQuery()
            ) {

                while (rs.next()) {
                    tickets.add(map(rs));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return tickets;

        }, AsyncExecutor.getExecutor());
    }

    @Override
    public CompletableFuture<List<Ticket>> findByReporter(UUID reporter) {

        return CompletableFuture.supplyAsync(() -> {

            List<Ticket> tickets = new ArrayList<>();

            try (
                    Connection connection = databaseManager.getDataSource().getConnection();
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    "SELECT * FROM tickets WHERE reporter = ? ORDER BY id DESC"
                            )
            ) {

                statement.setString(
                        1,
                        reporter.toString()
                );

                try (ResultSet rs = statement.executeQuery()) {

                    while (rs.next()) {
                        tickets.add(map(rs));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return tickets;

        }, AsyncExecutor.getExecutor());
    }

    @Override
    public CompletableFuture<Long> countActiveByReporter(
            UUID reporter
    ) {

        return CompletableFuture.supplyAsync(() -> {

            String sql = """
                    SELECT COUNT(*)
                    FROM tickets
                    WHERE reporter = ?
                    AND status IN ('NEW', 'CLAIMED')
                    """;

            try (
                    Connection connection =
                            databaseManager.getDataSource().getConnection();

                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(
                        1,
                        reporter.toString()
                );

                try (ResultSet rs = statement.executeQuery()) {

                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0L;

        }, AsyncExecutor.getExecutor());
    }

    @Override
    public CompletableFuture<Void> update(Ticket ticket) {

        return CompletableFuture.runAsync(() -> {

            String sql = """
                    UPDATE tickets
                    SET status = ?,
                        assigned_staff = ?,
                        updated_at = ?
                    WHERE id = ?
                    """;

            try (
                    Connection connection = databaseManager.getDataSource().getConnection();
                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(
                        1,
                        ticket.getStatus().name()
                );

                if (ticket.getAssignedStaff() != null) {
                    statement.setString(
                            2,
                            ticket.getAssignedStaff().toString()
                    );
                } else {
                    statement.setNull(
                            2,
                            Types.VARCHAR
                    );
                }

                statement.setLong(
                        3,
                        System.currentTimeMillis()
                );

                statement.setLong(
                        4,
                        ticket.getId()
                );

                statement.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, AsyncExecutor.getExecutor());
    }

    @Override
    public CompletableFuture<Void> delete(long id) {

        return CompletableFuture.runAsync(() -> {

            try (
                    Connection connection = databaseManager.getDataSource().getConnection();
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    "DELETE FROM tickets WHERE id = ?"
                            )
            ) {

                statement.setLong(1, id);
                statement.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, AsyncExecutor.getExecutor());
    }

    private Ticket map(ResultSet rs) throws SQLException {

        Ticket ticket = new Ticket();

        ticket.setId(rs.getLong("id"));

        ticket.setReporter(
                UUID.fromString(
                        rs.getString("reporter")
                )
        );

        ticket.setReportedPlayer(
                UUID.fromString(
                        rs.getString("reported_player")
                )
        );

        ticket.setReason(
                rs.getString("reason")
        );

        ticket.setStatus(
                TicketStatus.valueOf(
                        rs.getString("status")
                )
        );

        ticket.setPriority(
                TicketPriority.valueOf(
                        rs.getString("priority")
                )
        );

        ticket.setCategory(
                TicketCategory.valueOf(
                        rs.getString("category")
                )
        );

        String staff =
                rs.getString("assigned_staff");

        if (staff != null) {
            ticket.setAssignedStaff(
                    UUID.fromString(staff)
            );
        }

        ticket.setCreatedAt(
                rs.getLong("created_at")
        );

        ticket.setUpdatedAt(
                rs.getLong("updated_at")
        );

        return ticket;
    }
}