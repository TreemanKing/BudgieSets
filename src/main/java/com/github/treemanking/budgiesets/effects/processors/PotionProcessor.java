package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PotionProcessor implements EffectProcessor {

    private final String TYPE_KEY = "Type";
    private final String DURATION_KEY = "Duration";
    private final String AMPLIFIER_KEY = "Amplifier";
    private final String AMBIENT_KEY = "Ambient";
    private final String PARTICLES_KEY = "Particles";
    @Override
    public void processEffect(List<?> effect, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object potion : effect) {
            if (potion instanceof Map<?, ?>) {
                Map<?, ?> potionMap = (Map<?, ?>) potion;

                if (validatePotionConfig(potionMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.EQUIPPED)) {
                        // Extract and apply PermPotion effects here
                        String type = (String) potionMap.get(TYPE_KEY);
                        int duration = (int) potionMap.get(DURATION_KEY);
                        int amplifier = (int) potionMap.get(AMPLIFIER_KEY);
                        boolean ambient = (boolean) potionMap.get(AMBIENT_KEY);
                        boolean particles = (boolean) potionMap.get(PARTICLES_KEY);
                        List<String> conditions = (List<String>) potionMap.get("Conditions");
                        // Assuming you have a method to apply PermPotion effects
                        if (checkConditions(conditions, player)) {
                            applyPotionEffect(player, duration, type, amplifier, ambient, particles);
                        }
                    } else {
                        if ((int) potionMap.get(DURATION_KEY) == -1) removePotionEffects(player);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    Bukkit.getLogger().warning("Invalid potion configuration: " + potionMap);
                }
            }
        }
    }

    private static final Map<UUID, List<PotionEffect>> activeEffects = new HashMap<>();

    private void applyPotionEffect(@NotNull Player player, int duration, @NotNull String effectName, int amplifier, boolean ambient, boolean particles) {
        PotionEffectType effectType = PotionEffectType.getByName(effectName.toUpperCase());

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
            Bukkit.getLogger().warning("Invalid potion effect name: " + effectName);
        }
    }

    public void removePotionEffects(Player player) {
        UUID playerId = player.getUniqueId();
        List<PotionEffect> playerEffects = activeEffects.get(playerId);

        if (playerEffects != null) {
            // Create a copy of the effects list to avoid ConcurrentModificationException
            List<PotionEffect> effectsCopy = new ArrayList<>(playerEffects);

            player.clearActivePotionEffects();
            // Remove custom potion effects for the player
            for (PotionEffect effect : effectsCopy) {
                player.removePotionEffect(effect.getType());
            }

            // Remove the player entry from the activeEffects map
            activeEffects.remove(playerId);
        }
    }

    // Validation method for potion configuration
    private boolean validatePotionConfig(Map<?, ?> potionMap) {
        return potionMap.containsKey(TYPE_KEY)
                && potionMap.containsKey(DURATION_KEY)
                && potionMap.containsKey(AMPLIFIER_KEY)
                && potionMap.containsKey(AMBIENT_KEY)
                && potionMap.containsKey(PARTICLES_KEY)
                && potionMap.get(TYPE_KEY) instanceof String
                && potionMap.get(DURATION_KEY) instanceof Integer
                && potionMap.get(AMPLIFIER_KEY) instanceof Integer
                && potionMap.get(AMBIENT_KEY) instanceof Boolean
                && potionMap.get(PARTICLES_KEY) instanceof Boolean;
    }
}
