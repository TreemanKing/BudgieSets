package com.github.treemanking.budgiesets;

import com.github.treemanking.budgiesets.managers.HookManager;
import com.github.treemanking.budgiesets.managers.configuration.ConfigurationManager;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BudgieSets extends JavaPlugin implements HookManager{

    public static BudgieSets budgieSets;

    @Override
    public void onEnable() {
        ConfigurationManager configManager = new ConfigurationManager(this);
        checkHooks(this);

        new ArmorSetManager(this, configManager);
        budgieSets = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BudgieSets getBudgieSets() {
        return budgieSets;
    }
}
