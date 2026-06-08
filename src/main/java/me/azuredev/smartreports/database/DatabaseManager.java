package me.azuredev.smartreports.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.azuredev.smartreports.SmartReportsPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private final SmartReportsPlugin plugin;

    private HikariDataSource dataSource;

    public DatabaseManager(SmartReportsPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {

        String type = plugin.getConfig()
                .getString("database.type", "SQLITE");

        if (type.equalsIgnoreCase("SQLITE")) {
            setupSQLite();
        } else {
            setupMySQL();
        }

        createTables();
    }

    private void setupSQLite() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(
                "jdbc:sqlite:"
                        + plugin.getDataFolder()
                        + "/database.db"
        );

        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);
    }

    private void setupMySQL() {

        String host =
                plugin.getConfig().getString("database.mysql.host");

        int port =
                plugin.getConfig().getInt("database.mysql.port");

        String database =
                plugin.getConfig().getString("database.mysql.database");

        String username =
                plugin.getConfig().getString("database.mysql.username");

        String password =
                plugin.getConfig().getString("database.mysql.password");

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(
                "jdbc:mysql://"
                        + host + ":" + port + "/"
                        + database
        );

        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(20);

        dataSource = new HikariDataSource(config);
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public void shutdown() {

        if (dataSource != null) {
            dataSource.close();
        }
    }

    private void createTables() {

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("""
            CREATE TABLE IF NOT EXISTS tickets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                reporter TEXT,
                reported_player TEXT,
                reason TEXT,
                status TEXT,
                priority TEXT,
                category TEXT,
                assigned_staff TEXT,
                created_at INTEGER,
                updated_at INTEGER
            )
        """);

            statement.execute("""
            CREATE TABLE IF NOT EXISTS ticket_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ticket_id BIGINT,
                actor TEXT,
                action TEXT,
                description TEXT,
                timestamp BIGINT
            )
        """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}