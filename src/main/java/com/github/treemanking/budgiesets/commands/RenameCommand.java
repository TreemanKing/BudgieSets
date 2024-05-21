package com.github.treemanking.budgiesets.commands;

import com.github.treemanking.budgiesets.BudgieSets;

import java.io.File;

public interface RenameCommand {

    /**
     * Renames an armor set file in the "ArmorSets" folder.
     *
     * @param oldFileName The current name of the armor set file (without the ".yml" extension).
     * @param newFileName The new name for the armor set file (without the ".yml" extension).
     * @return {@code true} if the file was successfully renamed, {@code false} otherwise (file doesn't exist or couldn't be renamed).
     */
    default boolean renameArmorSetFile(String oldFileName, String newFileName) {
        File armorSetsFolder = new File(BudgieSets.getBudgieSets().getDataFolder(), "ArmorSets");
        File oldFile = new File(armorSetsFolder, oldFileName + ".yml");
        File newFile = new File(armorSetsFolder, newFileName + ".yml");

        if (oldFile.exists()) {
            return oldFile.renameTo(newFile);
        }
        return false; // Old file doesn't exist or couldn't be renamed
    }

}
