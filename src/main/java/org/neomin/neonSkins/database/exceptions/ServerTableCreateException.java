package org.neomin.neonSkins.database.exceptions;

import org.bukkit.Bukkit;

public class ServerTableCreateException
extends RuntimeException {

    public ServerTableCreateException(String message) {
        Bukkit.getConsoleSender().sendMessage("§c[NeonSkins] Ocorreu um exceção na criação de tabelas.");
        Bukkit.getConsoleSender().sendMessage("§cDetalhes: " + message);
    }
}
