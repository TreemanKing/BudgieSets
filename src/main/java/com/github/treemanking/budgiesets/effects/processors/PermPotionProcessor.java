package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.utilities.OnPluginDisable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermPotionProcessor implements EffectProcessor, OnPluginDisable {

    @Override
    public void processEffect(List<?> potions, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object potion : potions) {
            if (potion instanceof Map<?, ?> potionMap) {
                if (validatePotionConfig(potionMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.EQUIPPED) || equipStatus.equals(ArmorSetListener.EquipStatus.NULL)) {
                        // Extract and apply PermPotion effects here
                        String type = (String) potionMap.get(TYPE_KEY);
                        int amplifier = (int) potionMap.get(AMPLIFIER_KEY);
                        boolean ambient = (boolean) potionMap.get(AMBIENT_KEY);
                        boolean particles = (boolean) potionMap.get(PARTICLES_KEY);
                        // Assuming you have a method to apply PermPotion effects
                        applyPotionEffect(player, PotionEffect.INFINITE_DURATION, type, amplifier, ambient, particles);
                    } else {
                        removePotionEffects(player);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid potion configuration: " + potionMap);
                }
            }
        }
    }

    // Validation method for potion configuration
    private boolean validatePotionConfig(Map<?, ?> potionMap) {
        return potionMap.containsKey(TYPE_KEY)
                && potionMap.containsKey(AMPLIFIER_KEY)
                && potionMap.containsKey(AMBIENT_KEY)
                && potionMap.containsKey(PARTICLES_KEY)
                && potionMap.get(TYPE_KEY) instanceof String
                && potionMap.get(AMPLIFIER_KEY) instanceof Integer
                && potionMap.get(AMBIENT_KEY) instanceof Boolean
                && potionMap.get(PARTICLES_KEY) instanceof Boolean;

    }
}
