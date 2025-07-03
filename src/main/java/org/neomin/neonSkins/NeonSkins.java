package org.neomin.neonSkins;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import javafx.util.Pair;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.neomin.neonSkins.commands.SkinCMD;
import org.neomin.neonSkins.configuration.FileManager;
import org.neomin.neonSkins.configuration.SkinPlayer;
import org.neomin.neonSkins.database.SkinsDatabase;

import java.util.HashMap;

@Getter
public class NeonSkins
extends JavaPlugin {

    private final HashMap<String, Pair<String, String>> cached_skin = new HashMap<>();
    private final HashMap<String, SkinPlayer> cached_players = new HashMap<>();

    private FileManager fileManager;
    private SkinsDatabase skinsDatabase;

    @Override
    public void onEnable() {

        Bukkit.getConsoleSender().sendMessage("§b[NeonSkins] §eInicializando sistema de skins...");

        fileManager = new FileManager(this);
        fileManager.init();

        skinsDatabase = new SkinsDatabase(this);
        skinsDatabase.startConnection();

        Bukkit.getPluginCommand("skin").setExecutor(new SkinCMD(this));
    }

    @Override
    public void onDisable() {

    }
}
