package com.github.treemanking.budgiesets;

import com.github.treemanking.budgiesets.managers.configuration.ConfigurationManager;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BudgieSets extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigurationManager configManager = new ConfigurationManager(this);
        new ArmorSetManager(this, configManager);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
