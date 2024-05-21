package com.github.treemanking.budgiesets.commands;

import com.github.treemanking.budgiesets.BudgieSets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Interface for creating new armor sets and generating YAML content for the BudgieSets plugin.
 */
public interface CreateCommand {

    /**
     * Creates a new armor set configuration file with the given name and YAML content.
     *
     * @param armorSetName The name of the armor set.
     * @param yamlContent The YAML content to be written to the file.
     * @throws IOException If an I/O error occurs while writing the file.
     */
    default void createNewArmorSet(String armorSetName, String yamlContent) throws IOException {
        File armorSetsFolder = new File(BudgieSets.getBudgieSets().getDataFolder(), "ArmorSets");

        if (armorSetsFolder.mkdirs()) {
            BudgieSets.getBudgieSets().getLogger().info("ArmorSets folder created.");
        }

        File file = new File(armorSetsFolder, armorSetName + ".yml");

        if (file.exists()) {
            BudgieSets.getBudgieSets().getLogger().warning("The file " + armorSetName + ".yml already exists and will not be overwritten.");
            return; // Exit the method to prevent overwriting
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(yamlContent);
        } catch (IOException e) {
            BudgieSets.getBudgieSets().getLogger().severe("Failed to make " + armorSetName + " configuration.");
            throw e;
        }
    }

    /**
     * Generates the default YAML content for an armor set.
     *
     * @return The default YAML content as a String.
     */
    default String generateYamlContent() {
        return """
                # Please see https://github.com/TreemanKing/BudgieSets/wiki for events and effects
                Events:
                  - JUMP:
                      Chance: 0.5
                      Cooldown: 5
                      Effects:
                        - PARTICLE:
                          - Particle: REDSTONE
                            Count: 20
                            Offset: 1.0
                            Data:
                              Hex-Color: "#fcba03"
                              Size: 5.0""";
    }
}
