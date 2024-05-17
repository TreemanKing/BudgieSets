package com.github.treemanking.budgiesets.managers.armorsets;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.configuration.ConfigurationManager;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;

/**
 * The ArmorSetManager class manages the registration of ArmorSetListeners
 * for each armor set configuration file found in the ArmorSets folder.
 */
public class ArmorSetManager {

    private final BudgieSets plugin;
    private final ConfigurationManager configurationManager;

    /**
     * Constructs an ArmorSetManager for the given plugin and configuration manager.
     * It registers listeners for all armor sets defined in the configuration files.
     *
     * @param plugin the BudgieSets plugin instance
     * @param configurationManager the ConfigurationManager instance
     */
    public ArmorSetManager(BudgieSets plugin, ConfigurationManager configurationManager) {
        this.plugin = plugin;
        this.configurationManager = configurationManager;
        registerArmorSetListeners();
    }

    /**
     * Registers ArmorSetListeners for each armor set configuration file found in the ArmorSets folder.
     */
    private void registerArmorSetListeners() {
        File[] armorSetFiles = configurationManager.getArmorSetFiles();

        for (File configFile : armorSetFiles) {
            FileConfiguration armorSetConfig = configurationManager.getConfig("ArmorSets/" + configFile.getName());
            String armorSetName = configFile.getName().replace(".yml", "");

            registerArmorSetListener(armorSetName, armorSetConfig, plugin);
        }
    }

    /**
     * Registers an ArmorSetListener for a specific armor set.
     *
     * @param armorSetName the name of the armor set
     * @param armorSetConfig the configuration file for the armor set
     * @param plugin the BudgieSets plugin instance
     */
    private void registerArmorSetListener(String armorSetName, FileConfiguration armorSetConfig, BudgieSets plugin) {
        if (armorSetName == null || armorSetConfig == null) {
            return;
        } try {
            plugin.getServer().getPluginManager().registerEvents(
                    new ArmorSetListener(armorSetName, armorSetConfig, plugin), plugin);
            plugin.getLogger().info(armorSetName + " Registered");
        } catch (Exception exception) {
            plugin.getLogger().severe(armorSetName + " did not register and ran into an error!");
        }
    }
}