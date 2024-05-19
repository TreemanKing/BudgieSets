package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.utilities.ProcessorKeys;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class PermPotionProcessor implements EffectProcessor, ProcessorKeys {

    @Override
    public void processEffect(List<?> potions, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object potion : potions) {
            if (potion instanceof Map<?, ?> potionMap) {
                if (validatePotionConfig(potionMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.EQUIPPED)) {
                        // Extract and apply PermPotion effects here
                        String type = (String) potionMap.get(TYPE_KEY);
                        int amplifier = (int) potionMap.get(AMPLIFIER_KEY);
                        boolean ambient = (boolean) potionMap.get(AMBIENT_KEY);
                        boolean particles = (boolean) potionMap.get(PARTICLES_KEY);
                        // Assuming you have a method to apply PermPotion effects
                        applyPotionEffect(player, type, amplifier, ambient, particles);

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

    private void applyPotionEffect(@NotNull Player player, @NotNull String effectName, int amplifier, boolean ambient, boolean particles) {
        PotionEffectType effectType = PotionEffectType.getByName(effectName.toUpperCase());

        if (effectType != null) {
            // Apply the permanent potion effect
            PotionEffect effect = new PotionEffect(effectType, PotionEffect.INFINITE_DURATION, amplifier, ambient, particles);

            // Check if the player already has effects
            if (getPotionEffects().containsKey(player.getUniqueId())) {
                getPotionEffects().get(player.getUniqueId()).add(effect);
            } else {
                // If not, create a new list with the current effect
                List<PotionEffect> effects = new ArrayList<>();
                effects.add(effect);
                getPotionEffects().put(player.getUniqueId(), effects);
            }

            player.addPotionEffect(effect);
        } else {
            BudgieSets.getBudgieSets().getLogger().warning("Invalid potion effect name: " + effectName);
        }
    }

    public void removePotionEffects(Player player) {
        UUID playerId = player.getUniqueId();
        List<PotionEffect> playerEffects = getPotionEffects().get(playerId);

        if (playerEffects != null) {

            // Remove custom potion effects for the player
            for (PotionEffect effect : playerEffects) {
                player.removePotionEffect(effect.getType());
            }

            // Remove the player entry from the activeEffects map
            getPotionEffects().remove(playerId);
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
