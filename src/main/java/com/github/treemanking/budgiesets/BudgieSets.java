package com.github.treemanking.budgiesets;

import com.github.treemanking.budgiesets.commands.CommandManager;
import com.github.treemanking.budgiesets.managers.HookManager;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetManager;
import com.github.treemanking.budgiesets.managers.configuration.ConfigurationManager;
import com.github.treemanking.budgiesets.utilities.ShutdownTasks;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class BudgieSets extends JavaPlugin {

    private static BudgieSets budgieSets;
    private static ConfigurationManager configurationManager;


    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIPaperConfig(this).silentLogs(true));
    }

    @Override
    public void onEnable() {
        budgieSets = this;

        CommandAPI.onEnable();
        HookManager.checkHooks(this);

        configurationManager = new ConfigurationManager(this);
        new ArmorSetManager(this, configurationManager);
        new CommandManager(this);
    }

    @Override
    public void onDisable() {

        CommandAPI.onDisable();

        ShutdownTasks.runAll();
    }

    public static BudgieSets getBudgieSets() {
        return budgieSets;
    }

    public static ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }
}
