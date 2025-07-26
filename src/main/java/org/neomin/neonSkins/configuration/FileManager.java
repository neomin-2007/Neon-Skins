package org.neomin.neonSkins.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.neomin.neonSkins.NeonSkins;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

@RequiredArgsConstructor
public class FileManager {

    private final NeonSkins plugin;
    private final HashMap<String, String> loadedData = new HashMap<>();
    private File configFile;

    @Getter private FileConfiguration config;

    public void init() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.getLogger().log(Level.WARNING, "[NeonSkins] Arquivo config.yml não encontrado, criando novo...");
            try {
                plugin.saveResource("config.yml", false);
                plugin.getLogger().log(Level.INFO, "[NeonSkins] Configuração padrão criada com sucesso!");
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "[NeonSkins] Erro ao criar config.yml: " + e.getMessage());
                return;
            }
        } else {
            plugin.getLogger().log(Level.INFO, "[NeonSkins] Carregando configuração existente...");
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        loadAllConfigValues();

        Bukkit.getConsoleSender().sendMessage("§b[NeonSkins] §aConfigurações carregadas com sucesso!");
    }

    private void loadAllConfigValues() {
        loadedData.clear();
        for (String key : config.getKeys(true)) {
            if (config.isString(key)) {
                loadedData.put(key, config.getString(key));
            } else if (config.isBoolean(key)) {
                loadedData.put(key, String.valueOf(config.getBoolean(key)));
            } else if (config.isInt(key)) {
                loadedData.put(key, String.valueOf(config.getInt(key)));
            } else if (config.isDouble(key)) {
                loadedData.put(key, String.valueOf(config.getDouble(key)));
            }
        }
    }

    public void reloadConfig() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            loadAllConfigValues();
            plugin.getLogger().log(Level.INFO, "[NeonSkins] Configuração recarregada com sucesso!");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "[NeonSkins] Erro ao recarregar configuração: " + e.getMessage());
        }
    }

    public void saveConfig() {
        try {
            config.save(configFile);
            plugin.getLogger().log(Level.INFO, "[NeonSkins] Configuração salva com sucesso!");
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "[NeonSkins] Erro ao salvar configuração: " + e.getMessage());
        }
    }

    public String getString(String key) {
        return loadedData.getOrDefault(key, "");
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(loadedData.getOrDefault(key, "false"));
    }

    public int getInt(String key) {
        try {
            return Integer.parseInt(loadedData.getOrDefault(key, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getDouble(String key) {
        try {
            return Double.parseDouble(loadedData.getOrDefault(key, "0.0"));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public long getLong(String key) {
        try {
            return Long.parseLong(loadedData.getOrDefault(key, "0"));
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}