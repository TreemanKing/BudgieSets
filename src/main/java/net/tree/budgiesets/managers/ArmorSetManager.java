package net.tree.budgiesets.managers;

import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class ArmorSetManager {

    private final BudgieSets plugin;
    private final ConfigurationManager configurationManager;

    public ArmorSetManager(BudgieSets plugin, ConfigurationManager configurationManager) {
        this.plugin = plugin;
        this.configurationManager = configurationManager;
        registerArmorSetListeners();
    }

    // ArmorSetListener
    private void registerArmorSetListeners() {
        File[] armorSetFiles = configurationManager.getArmorSetFiles();

        for (File configFile : armorSetFiles) {
            FileConfiguration armorSetConfig = configurationManager.getConfig("ArmorSets/" + configFile.getName());
            String armorSetName = configFile.getName().replace(".yml", "");

            registerArmorSetListener(armorSetName, armorSetConfig, plugin);
        }
    }

    private void registerArmorSetListener(String armorSetName, FileConfiguration armorSetConfig, BudgieSets plugin) {
        if (armorSetName == null || armorSetConfig == null) return;

        try {

            plugin.getServer().getPluginManager().registerEvents(new ArmorSetListener(armorSetName, armorSetConfig, plugin), plugin);
            plugin.getLogger().info(armorSetName + " Registered");
        } catch (Exception exception) {
            plugin.getLogger().severe(armorSetName + " did not register and ran into an error!");
        }
    }
}
