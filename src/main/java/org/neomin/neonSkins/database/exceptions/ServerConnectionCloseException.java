package org.neomin.neonSkins.database.exceptions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ServerConnectionCloseException
extends RuntimeException {

    public ServerConnectionCloseException(String message) {
        Bukkit.getConsoleSender().sendMessage("§c[NeonSkins] Ocorreu um exceção no fechamento da conexão.");
        Bukkit.getConsoleSender().sendMessage("§cDetalhes: " + message);
    }
}
