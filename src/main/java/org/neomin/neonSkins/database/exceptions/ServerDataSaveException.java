package org.neomin.neonSkins.database.exceptions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerDataSaveException
extends RuntimeException {

    public ServerDataSaveException(String message) {
        Bukkit.getConsoleSender().sendMessage("§c[NeonSkins] Ocorreu um exceção no salvamento de dados.");
        Bukkit.getConsoleSender().sendMessage("§cDetalhes: " + message);
    }

    public ServerDataSaveException(Player player, String message) {

        if (player != null) {
            player.sendMessage("§c[NeonSkins] Ocorreu uma exceção no salvamento de dados.");
            player.sendMessage("§cReporte o erro para um moderador ou superior!");
        }

        Bukkit.getConsoleSender().sendMessage("§c[NeonSkins] Ocorreu uma exceção no salvamento de dados.");
        Bukkit.getConsoleSender().sendMessage("§cDetalhes: " + message);
    }
}
