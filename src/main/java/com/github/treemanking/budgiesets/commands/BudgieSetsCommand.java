package com.github.treemanking.budgiesets.commands;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.commands.subcommands.*;

import java.io.File;
import java.util.Arrays;

public interface BudgieSetsCommand extends CreateCommand, RemoveCommand, ReloadCommand, RenameCommand, LoadUnloadCommand {

    /**
     * Retrieves the names of all armor set files in the "ArmorSets" folder.
     *
     * @return An array of strings containing the names of the armor set files without the ".yml" extension.
     */
    default String[] getArmorSetFileNames() {
        File armorSetsFolder = new File(BudgieSets.getBudgieSets().getDataFolder(), "ArmorSets");
        File[] files = armorSetsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
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
}
