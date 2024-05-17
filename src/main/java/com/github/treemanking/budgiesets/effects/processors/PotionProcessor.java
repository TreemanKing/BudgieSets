package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.effects.EffectProcessorKeys;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PotionProcessor implements EffectProcessor, EffectProcessorKeys {

    @Override
    public void processEffect(List<?> effect, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object potion : effect) {
            if (potion instanceof Map<?, ?> potionMap) {
                if (validatePotionConfig(potionMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.EQUIPPED)) {
                        // Extract and apply PermPotion effects here
                        String actionType = (String) potionMap.get(ACTION_TYPE_KEY);
                        String type = (String) potionMap.get(TYPE_KEY);
                        int duration;
                        int amplifier;
                        boolean ambient;
                        boolean particles;

                        // TODO: Fix this, less if statements!
                        if (potionMap.get(DURATION_KEY) == null) {
                            duration = 5;
                        } else {
                            duration = (int) potionMap.get(DURATION_KEY);
                        }

                        if (potionMap.get(AMPLIFIER_KEY) == null) {
                            amplifier = 0;
                        } else {
                            amplifier = (int) potionMap.get(AMPLIFIER_KEY);
                        }

                        if (potionMap.get(AMBIENT_KEY) == null) {
                            ambient = false;
                        } else {
                            ambient = (boolean) potionMap.get(AMBIENT_KEY);
                        }

                        if (potionMap.get(PARTICLES_KEY) == null) {
                            particles = true;
                        } else {
                            particles = (boolean) potionMap.get(PARTICLES_KEY);
                        }

                        List<String> conditions = (List<String>) potionMap.get(CONDITION_KEY);
                        // Assuming you have a method to apply effects
                        if (checkConditions(conditions, player)) {
                            actionPotionEffect(actionType, player, duration, type, amplifier, ambient, particles);
                        }
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid potion configuration: " + potionMap);
                }
            }
        }
    }

    private final Map<UUID, List<PotionEffect>> activeEffects = new HashMap<>();

    private void actionPotionEffect(String actionType, @NotNull Player player, int duration, @NotNull String effectName, int amplifier, boolean ambient, boolean particles) {
        if (actionType.equalsIgnoreCase("Add")) {
            applyPotionEffect(player, duration, effectName, amplifier, ambient, particles);
        } else if (actionType.equalsIgnoreCase("Remove")) {
            removePotionEffects(player, effectName);
        } else {
            BudgieSets.getBudgieSets().getLogger().warning("You must have an action type of add or remove.");
        }
    }

    private void applyPotionEffect(@NotNull Player player, int duration, @NotNull String effectName, int amplifier, boolean ambient, boolean particles) {
        PotionEffectType effectType = PotionEffectType.getByName(effectName.toUpperCase());

        if (duration == 0) duration = 5;

        if (effectType != null) {
            // Apply the permanent potion effect
            PotionEffect effect = new PotionEffect(effectType, duration * 20, amplifier, ambient, particles);

            // Check if the player already has effects
            if (activeEffects.containsKey(player.getUniqueId())) {
                activeEffects.get(player.getUniqueId()).add(effect);
            } else {
                // If not, create a new list with the current effect
                List<PotionEffect> effects = new ArrayList<>();
                effects.add(effect);
                activeEffects.put(player.getUniqueId(), effects);
            }

            player.addPotionEffect(effect);
        } else {
            BudgieSets.getBudgieSets().getLogger().warning("Invalid potion effect name: " + effectName);
        }
    }

    public void removePotionEffects(Player player, String potionEffect) {
        PotionEffectType effectType = PotionEffectType.getByName(potionEffect.toUpperCase());

        if (player.getActivePotionEffects().isEmpty()) return;
        if (effectType == null) return;

        player.removePotionEffect(effectType);
    }

    // Validation method for potion configuration
    private boolean validatePotionConfig(Map<?, ?> potionMap) {
        return potionMap.containsKey(TYPE_KEY)
                && potionMap.containsKey(ACTION_TYPE_KEY)
                && potionMap.get(TYPE_KEY) instanceof String
                && potionMap.get(ACTION_TYPE_KEY) instanceof String;
    }
}
