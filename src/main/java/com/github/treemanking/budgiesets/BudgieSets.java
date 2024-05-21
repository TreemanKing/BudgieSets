package com.github.treemanking.budgiesets;

import com.github.treemanking.budgiesets.managers.HookManager;
import com.github.treemanking.budgiesets.managers.configuration.ConfigurationManager;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetManager;
import com.github.treemanking.budgiesets.utilities.OnPluginDisable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class BudgieSets extends JavaPlugin implements HookManager, OnPluginDisable {

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
        for (Player player : getServer().getOnlinePlayers()) {
            if (!getPotionEffects().containsKey(player.getUniqueId())) continue;
            removePotionEffects(player);
        }
    }

    public static BudgieSets getBudgieSets() {
        return budgieSets;
    }
}
