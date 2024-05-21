/**
 * This interface provides methods for managing armor set files within the "ArmorSets" folder.
 */
package com.github.treemanking.budgiesets.commands.subcommands;

import com.github.treemanking.budgiesets.BudgieSets;

import java.io.File;
import java.util.Arrays;

public interface RemoveCommand {

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

    /**
     * Deletes an armor set file with the specified name from the "ArmorSets" folder.
     *
     * @param fileName The name of the armor set file to delete (without the ".yml" extension).
     */
    default void deleteArmorSetFile(String fileName) {
        File armorSetsFolder = new File(BudgieSets.getBudgieSets().getDataFolder(), "ArmorSets");
        File fileToDelete = new File(armorSetsFolder, fileName + ".yml");

        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                BudgieSets.getBudgieSets().getLogger().info(fileName + " has been deleted.");
            }
        }
        BudgieSets.getBudgieSets().getLogger().warning(fileName + " does not exist and therefore cannot be deleted.");
    }
}
