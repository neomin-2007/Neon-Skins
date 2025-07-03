package org.neomin.neonSkins.configuration;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.neomin.neonSkins.NeonSkins;
import org.omg.PortableInterceptor.SUCCESSFUL;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

@RequiredArgsConstructor
public class FileManager {

    private final NeonSkins plugin;
    private final HashMap<String, String> loaded_data = new HashMap<>();

    public void init() {
        File file = new File("config.yml");
        if (!file.exists()) {
            plugin.getLogger().log(Level.WARNING, "§eNão detectamos o §farquivo §e'config.yml'.");
            plugin.getLogger().log(Level.WARNING, "§eCarregando §fconfiguração §epadrão do NeonSkins");

            plugin.saveResource("config.yml", false);
            file = new File(plugin.getDataFolder(), "config.yml");
        }

        final FileConfiguration messagesCfg = YamlConfiguration.loadConfiguration(file);
        for (String key : messagesCfg.getKeys(true)) {
            String value = messagesCfg.getString(key);

            loaded_data.put(key, value);
        }

        Bukkit.getConsoleSender().sendMessage("§b[NeonSkins] §eAs configurações foram carregadas com sucesso!");
    }

    public String getString(String key) {
        return loaded_data.get(key);
    }

    public Boolean getBoolean(String key) {
        return Boolean.parseBoolean(loaded_data.get(key));
    }

    public int getInt(String key) {
        return Integer.parseInt(loaded_data.get(key));
    }

    public double getDouble(String key) {
        return Double.parseDouble(loaded_data.get(key));
    }

    public long getLong(String key) {
        return Long.parseLong(loaded_data.get(key));
    }
}
