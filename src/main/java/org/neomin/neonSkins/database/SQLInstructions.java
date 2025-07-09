package org.neomin.neonSkins.database;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.neomin.neonSkins.NeonSkins;
import org.neomin.neonSkins.configuration.SkinPlayer;
import org.neomin.neonSkins.database.exceptions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor
public class SQLInstructions {

    private final NeonSkins plugin;
    private final Connection connection;

    public SkinPlayer createPlayerData(String name) {
        if (playerExists(name)) {
            return getPlayerData(name);
        }

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO " + plugin.getFileManager().getString("database.table_name") + " (name, skinId, texture, signature) VALUES (?, ?, ?, ?)")) {

            final SkinPlayer playerData = new SkinPlayer();

            stmt.setString(1, name);
            stmt.setString(2, "");
            stmt.setString(3, "");
            stmt.setString(4, "");

            stmt.executeUpdate();

            plugin.getCached_players().put(name, playerData);
            return playerData;
        } catch (SQLException e) {
            throw new ServerDataCreateException(Bukkit.getPlayer(name), e.getMessage());
        }
    }

    public boolean playerExists(String name) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT name FROM " + plugin.getFileManager().getString("database.table_name") + " WHERE name = ?")) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new ServerDataVerifyException(e.getMessage());
        }
    }

    public SkinPlayer getPlayerData(String name) {
        SkinPlayer playerData = plugin.getCached_players().get(name);
        if (playerData == null && playerExists(name)) {
            playerData = fetchPlayerData(name);
        }
        return playerData;
    }

    public SkinPlayer fetchPlayerData(String name) {
        if (!playerExists(name)) {
            return createPlayerData(name);
        }
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM " + plugin.getFileManager().getString("database.table_name") + " WHERE name = ?")) {

            stmt.setString(1, name);
            final ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                final SkinPlayer playerData = new SkinPlayer();
                playerData.setSkinId(rs.getString("skinId"));
                playerData.setTexture(rs.getString("texture"));
                playerData.setSignature(rs.getString("signature"));
                plugin.getCached_players().put(name, playerData);
                return playerData;
            }
            return null;
        } catch (SQLException e) {
            throw new ServerDataFetchException(Bukkit.getPlayer(name), e.getMessage());
        }
    }

    public void savePlayerData(String name) {
        if (plugin.getCached_players().containsKey(name)) {
            final SkinPlayer playerData = getPlayerData(name);

            try (PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE " + plugin.getFileManager().getString("database.table_name") +
                            " SET skinId = ?, texture = ?, signature = ? WHERE name = ?")) {

                stmt.setString(1, playerData.getSkinId());
                stmt.setString(2, playerData.getTexture());
                stmt.setString(3, playerData.getSignature());
                stmt.setString(4, name);

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new ServerDataSaveException(Bukkit.getPlayer(name), e.getMessage());
            }
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new ServerConnectionCloseException(e.getMessage());
            }
        }
    }
}