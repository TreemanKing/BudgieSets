package com.github.treemanking.budgiesets.managers.configuration;

import com.github.treemanking.budgiesets.BudgieSets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public interface TemplateArmorSetConfig {

    default void createNewArmorSet(String armorSetName, String yamlContent) throws IOException {
        File armorSetsFolder = new File(BudgieSets.getBudgieSets().getDataFolder(), "ArmorSets");

        if (armorSetsFolder.mkdirs()) {
            BudgieSets.getBudgieSets().getLogger().info("ArmorSets folder created.");
        }

        File file = new File(armorSetsFolder, armorSetName + ".yml");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(yamlContent);
        } catch (IOException e) {
            BudgieSets.getBudgieSets().getLogger().severe("Failed to make " + armorSetName + " configuration.");
        }
    }

    default String generateYamlContent() {
        return """
                # Please see https://github.com/TreemanKing/BudgieSets/wiki for events and effects
                Events:
                  - DAMAGED:
                      Chance: 0.1
                      Effects:
                        - CANCEL_EVENT:
                          - Boolean: true
                        - PLAY_SOUND:
                          - Sound: ENTITY_ENDERMAN_TELEPORT\s
                            Pitch: 1.0
                            Volume: 1.5
                        - PARTICLE:
                          - Particle: DRAGON_BREATH
                            Count: 40
                            Offset: 1.0
                        - ACTION_BAR:
                          - Text: "Attack Evaded!\"""";
    }

}
