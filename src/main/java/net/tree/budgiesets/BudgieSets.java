package net.tree.budgiesets;

import net.tree.budgiesets.managers.ArmorSetManager;
import net.tree.budgiesets.managers.ConfigurationManager;
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
