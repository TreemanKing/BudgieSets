package com.github.treemanking.budgiesets.commands;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetManager;
import com.github.treemanking.budgiesets.utilities.OnPluginDisable;
import org.bukkit.event.HandlerList;

public interface ReloadCommand extends OnPluginDisable {

    default void reloadPlugin() {
        HandlerList.unregisterAll(BudgieSets.getBudgieSets());
        new ArmorSetManager(BudgieSets.getBudgieSets(), BudgieSets.getConfigurationManager());
        removeAllPermPotionEffects();
    }

    /**
     * Registers an ArmorSetListener for a specific armor set.
     *
     * @param armorSetName the name of the armor set
     * @param plugin       the BudgieSets plugin instance
     */
    default void loadArmorSet(String armorSetName, BudgieSets plugin) {
        if (armorSetName == null || BudgieSets.getConfigurationManager().getConfig("ArmorSets/" + armorSetName) == null) {
            return;
        }
        try {
            plugin.getServer().getPluginManager().registerEvents(
                    new ArmorSetListener(armorSetName, BudgieSets.getConfigurationManager().getConfig("ArmorSets/" + armorSetName), plugin), plugin);
            plugin.getLogger().info(armorSetName + " Registered");
        } catch (Exception exception) {
            plugin.getLogger().severe(armorSetName + " did not register and ran into an error!");
        }
    }
}

