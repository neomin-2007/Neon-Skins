package org.neomin.neonSkins.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.neomin.neonSkins.NeonSkins;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor
public class SkinsDatabase {

    private final NeonSkins plugin;
    private SQLInstructions instructions;
    private HikariDataSource dataSource;

    public void createTable() {
        String tableName = plugin.getFileManager().getString("database.table_name");
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                + "name VARCHAR(36) PRIMARY KEY,"
                + "skinId TEXT,"
                + "texture TEXT,"
                + "signature TEXT"
                + ");";

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            plugin.getLogger().severe("Erro ao criar tabela: " + e.getMessage());
        }
    }

    public void startConnection() {
        try {
            final String dataType = plugin.getFileManager().getString("database.type");

            if (dataType.equalsIgnoreCase("SQLite")) {
                SQLiteConnection();
            } else if (dataType.equalsIgnoreCase("MySQL")) {
                MySQLConnection();
            }

            instructions = new SQLInstructions(plugin, dataSource);
            createTable();
        } catch (Exception e) {
            plugin.getLogger().severe("Erro ao iniciar conexão: " + e.getMessage());
        }
    }

    public void SQLiteConnection() throws SQLException {
        String fileName = plugin.getFileManager().getString("database.SQLite-data.SQLite-archive");
        if (fileName.isEmpty()) fileName = "skinsDatabase.sql";

        final File sqlFile = new File(plugin.getDataFolder(), fileName);
        if (!sqlFile.exists()) {
            plugin.saveResource(fileName, false);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + sqlFile.getAbsolutePath());
        config.setDriverClassName("org.sqlite.JDBC");

        config.setMaximumPoolSize(1);
        config.setConnectionTimeout(30000);

        dataSource = new HikariDataSource(config);
    }

    public void MySQLConnection() {
        final String ACCESS_PATH = "database.mySQL-data.";

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" +
                plugin.getFileManager().getString(ACCESS_PATH + "hostname") + "/" +
                plugin.getFileManager().getString(ACCESS_PATH + "database") +
                "?useSSL=false" +
                "&serverTimezone=UTC");

        config.setUsername(plugin.getFileManager().getString(ACCESS_PATH + "username"));
        config.setPassword(plugin.getFileManager().getString(ACCESS_PATH + "password"));
        config.setDriverClassName("com.mysql.jdbc.Driver");

        config.setMaximumPoolSize(plugin.getFileManager().getInt(ACCESS_PATH + "hikari-settings.setMaximumPoolSize"));
        config.setMinimumIdle(plugin.getFileManager().getInt(ACCESS_PATH + "hikari-settings.setMinimumIdle"));
        config.setConnectionTimeout(plugin.getFileManager().getInt(ACCESS_PATH + "hikari-settings.setConnectionTimeout"));
        config.setIdleTimeout(plugin.getFileManager().getInt(ACCESS_PATH + "hikari-settings.setIdleTimeout"));
        config.setMaxLifetime(plugin.getFileManager().getInt(ACCESS_PATH + "hikari-settings.setMaxLifetime"));
        config.setLeakDetectionThreshold(plugin.getFileManager().getInt(ACCESS_PATH + "hikari-settings.setLeakDetectionThreshold"));
        config.setPoolName("NeonSkinsMySQLPool");

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        dataSource = new HikariDataSource(config);
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            plugin.getLogger().info("Conexão com o banco de dados fechada com sucesso");
        }
    }
}