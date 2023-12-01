package net.tree.budgiesets;

import net.tree.budgiesets.managers.ArmorSetManager;
import net.tree.budgiesets.managers.ConfigurationManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BudgieSets extends JavaPlugin {

    public static BudgieSets plugin;

    @Override
    public void onEnable() {

        plugin = this;

        ConfigurationManager configManager = new ConfigurationManager(this);
        ArmorSetManager armorSetManager = new ArmorSetManager(this, configManager);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
