package org.neomin.neonSkins.database;

import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.neomin.neonSkins.NeonSkins;
import org.neomin.neonSkins.configuration.SkinPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

@RequiredArgsConstructor
public class SkinsDatabase {

    private final NeonSkins plugin;
    private Connection connection;

    private final HashMap<String, Pair<String, String>> cached_skin = new HashMap<>();
    private final HashMap<String, SkinPlayer> cached_players = new HashMap<>();

    public void createTable() {
        String tableName = plugin.getFileManager().getString("database.table_name");
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                + "uuid VARCHAR(36) PRIMARY KEY,"
                + "skin_id TEXT,"
                + "is_default BOOLEAN"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
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

            createTable();
        } catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void SQLiteConnection() throws SQLException, ClassNotFoundException {
        String fileName = plugin.getFileManager().getString("database.SQLite-data.SQLite-archive");
        if (fileName.isEmpty()) fileName = "skinsDatabase.sql";

        final File sqlFile = new File(plugin.getDataFolder(), fileName);
        if (!sqlFile.exists()) {
            plugin.saveResource(fileName, false);
        }

        final String url = "jdbc:sqlite:" + sqlFile.getAbsolutePath();
        connection = DriverManager.getConnection(url);
    }

    public void MySQLConnection() throws SQLException, ClassNotFoundException {

        final String ACCESS_PATH = "database.mySQL-data.";

        final String hostname = plugin.getFileManager().getString(ACCESS_PATH + "hostname");
        final String database = plugin.getFileManager().getString(ACCESS_PATH + "database");
        final String username = plugin.getFileManager().getString(ACCESS_PATH + "username");
        final String password = plugin.getFileManager().getString(ACCESS_PATH + "password");

        final String url = "jdbc:mysql://" + hostname + "/" + database + "?useSSL=false&serverTimezone=UTC";

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(url, username, password);
    }
}
