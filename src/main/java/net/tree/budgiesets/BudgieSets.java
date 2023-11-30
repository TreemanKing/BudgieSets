package net.tree.budgiesets;

import net.tree.budgiesets.managers.ListenerManager;
import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.managers.ConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public final class BudgieSets extends JavaPlugin {

    public static BudgieSets plugin;
    private ConfigurationManager configManager;

    @Override
    public void onEnable() {

        plugin = this;

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

    private void registerArmorSetListener(String armorSetName, FileConfiguration armorSetConfig) {
        if (armorSetName == null || armorSetConfig == null) return;

        try {

            Bukkit.getServer().getPluginManager().registerEvents(new ArmorSetListener(armorSetName, armorSetConfig), this);
            getLogger().info(armorSetName + " Registered");
        } catch (Exception exception) {
            getLogger().severe(armorSetName + " did not register and ran into an error!");
            exception.printStackTrace();
        }
    }
}
