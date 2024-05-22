package com.github.treemanking.budgiesets.commands.subcommands;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetManager;
import com.github.treemanking.budgiesets.utilities.OnPluginDisable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public interface LoadUnloadCommand extends RenameCommand, OnPluginDisable {

    default void loadArmorSet(String armorSetName) {
        BudgieSets plugin = BudgieSets.getBudgieSets();
        List<String> enabledArmorSets = Arrays.asList(ArmorSetManager.getEnabledArmorSets());
        if (enabledArmorSets.contains(armorSetName)) {
            BudgieSets.getBudgieSets().getLogger().warning(armorSetName + " has already been loaded. Loading terminated.");
            return;
        }

        if (armorSetName.startsWith("--")) {
            renameArmorSetFile(armorSetName, armorSetName.substring(2));
            armorSetName = armorSetName.substring(2);
        }

        FileConfiguration armorSetConfig = BudgieSets.getConfigurationManager().getConfig("ArmorSets/" + armorSetName + ".yml");

        if (armorSetConfig == null) {
            return;
        }
        try {
            plugin.getServer().getPluginManager().registerEvents(
                    new ArmorSetListener(armorSetName, armorSetConfig, plugin), plugin);
            plugin.getLogger().info(armorSetName + " Registered");
            ArmorSetManager.addEnabledArmorSet(armorSetName);
        } catch (Exception exception) {
            plugin.getLogger().severe(armorSetName + " did not register and ran into an error!");
            throw exception;
        }

        BudgieSets.getBudgieSets().getLogger().info(armorSetName + " has been loaded into the server.");
    }

    default String[] getUnloadedArmorSetFileNames() {
        File armorSetsFolder = new File(BudgieSets.getBudgieSets().getDataFolder(), "ArmorSets");
        File[] files = armorSetsFolder.listFiles((dir, name) -> name.toLowerCase().startsWith("--") && name.toLowerCase().endsWith(".yml"));
        if (files != null) {
            return Arrays.stream(files)
                    .map(file -> {
                        String fileName = file.getName();
                        return fileName.substring(0, fileName.length() - 4); // Remove the ".yml" extension
                    })
                    .toArray(String[]::new);
        }
        return new String[0];
    }

    default void unloadArmorSet(String armorSetName) {
        removeAllPermPotionEffects();

        for (RegisteredListener listener : HandlerList.getRegisteredListeners(BudgieSets.getBudgieSets())) {
            if (listener.getListener().hashCode() == armorSetName.hashCode()) {
                HandlerList.unregisterAll(listener.getListener());
            }
        }

        renameArmorSetFile(armorSetName, "--" + armorSetName);
        BudgieSets.getBudgieSets().getLogger().info(armorSetName + " has been unloaded from the server.");
    }

}
