package org.neomin.neonSkins.database.exceptions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerDataCreateException
extends RuntimeException {

    public ServerDataCreateException(String message) {
        Bukkit.getConsoleSender().sendMessage("§c[NeonSkins] Ocorreu um exceção na criação de dados.");
        Bukkit.getConsoleSender().sendMessage("§cDetalhes: " + message);
    }

    public ServerDataCreateException(Player player, String message) {

        if (player != null) {
            player.sendMessage("§c[NeonSkins] Ocorreu uma exceção na criação de dados.");
            player.sendMessage("§cReporte o erro para um moderador ou superior!");
        }

        Bukkit.getConsoleSender().sendMessage("§c[NeonSkins] Ocorreu uma exceção na criação de dados.");
        Bukkit.getConsoleSender().sendMessage("§cDetalhes: " + message);
    }
}
