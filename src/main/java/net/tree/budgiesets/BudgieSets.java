package net.tree.budgiesets;

import net.tree.budgiesets.managers.ListenerManager;
import net.tree.budgiesets.eventlisteners.ArmorSetEquip;
import net.tree.budgiesets.managers.ConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public final class BudgieSets extends JavaPlugin {

    private ConfigurationManager configManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigurationManager(this);
        ListenerManager.registerListeners(this);
        registerArmorSetListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // ArmorSetListener
    public void registerArmorSetListeners() {
        File[] armorSetFiles = configManager.getArmorSetFiles();

        for (File configFile : armorSetFiles) {
            FileConfiguration armorSetConfig = configManager.getConfig("ArmorSets/" + configFile.getName());
            String armorSetName = configFile.getName().replace(".yml", "");

            registerArmorSetListener(armorSetName, armorSetConfig);
        }
    }

    private void registerArmorSetListener(String armorSetName, ConfigurationSection armorSetConfig) {

        if (armorSetName != null && !armorSetName.isEmpty()) {
            // Register a listener for the specific armor set
            Bukkit.getServer().getPluginManager().registerEvents(new ArmorSetEquip(armorSetName, armorSetConfig), this);
        }
    }
}
