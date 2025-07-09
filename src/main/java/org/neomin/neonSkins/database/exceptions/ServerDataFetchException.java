package org.neomin.neonSkins.database.exceptions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerDataFetchException
extends RuntimeException {

    public ServerDataFetchException(String message) {
        Bukkit.getConsoleSender().sendMessage("§c[NeonSkins] Ocorreu um exceção na busca de dados.");
        Bukkit.getConsoleSender().sendMessage("§cDetalhes: " + message);
    }

    public ServerDataFetchException(Player player, String message) {

        if (player != null) {
            player.sendMessage("§c[NeonSkins] Ocorreu uma exceção na busca de dados.");
            player.sendMessage("§cReporte o erro para um moderador ou superior!");
        }

        Bukkit.getConsoleSender().sendMessage("§c[NeonSkins] Ocorreu uma exceção na busca de dados.");
        Bukkit.getConsoleSender().sendMessage("§cDetalhes: " + message);
    }
}
