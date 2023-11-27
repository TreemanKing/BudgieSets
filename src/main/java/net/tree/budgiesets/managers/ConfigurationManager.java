package net.tree.budgiesets.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;

public class ConfigurationManager {

    private final JavaPlugin plugin;

    public ConfigurationManager(JavaPlugin plugin) {
        this.plugin = plugin;
        initializeConfig();
        initializeArmorSetsFolder();
    }

    private void initializeConfig() {
        plugin.saveDefaultConfig();
    }

    private void initializeArmorSetsFolder() {
        File armorSetsFolder = new File(plugin.getDataFolder(), "ArmorSets");

        if (!armorSetsFolder.exists()) {
            armorSetsFolder.mkdirs();
        }
    }

    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);

        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }

        return YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig(FileConfiguration config, String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File[] getArmorSetFiles() {
        File armorSetsFolder = new File(plugin.getDataFolder(), "ArmorSets");
        return armorSetsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
    }
}
