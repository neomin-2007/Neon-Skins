package org.neomin.neonSkins.database.exceptions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerDataVerifyException
extends RuntimeException {

    public ServerDataVerifyException(String message) {
        Bukkit.getConsoleSender().sendMessage("§c[NeonSkins] Ocorreu um exceção na verificação de dados.");
        Bukkit.getConsoleSender().sendMessage("§cDetalhes: " + message);
    }

    public ServerDataVerifyException(Player player, String message) {

        if (player != null) {
            player.sendMessage("§c[NeonSkins] Ocorreu uma exceção na verificação de dados.");
            player.sendMessage("§cReporte o erro para um moderador ou superior!");
        }

        Bukkit.getConsoleSender().sendMessage("§c[NeonSkins] Ocorreu uma exceção na verificação de dados.");
        Bukkit.getConsoleSender().sendMessage("§cDetalhes: " + message);
    }
}
