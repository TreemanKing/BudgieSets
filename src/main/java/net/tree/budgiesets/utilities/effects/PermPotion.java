package net.tree.budgiesets.utilities.effects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PermPotion {

    private static final Map<UUID, List<PotionEffect>> activeEffects = new HashMap<>();

    public static void applyPotionEffect(@NotNull Player player, @NotNull String effectName, int amplifier, boolean ambient, boolean particles) {
        PotionEffectType effectType = PotionEffectType.getByName(effectName.toUpperCase());

        if (effectType != null) {
            // Apply the permanent potion effect
            PotionEffect effect = new PotionEffect(effectType, PotionEffect.INFINITE_DURATION, amplifier, ambient, particles);

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

    public static void removePotionEffects(Player player) {
        UUID playerId = player.getUniqueId();
        List<PotionEffect> playerEffects = activeEffects.get(playerId);

        if (playerEffects != null) {
            // Remove all potion effects for the player
            for (PotionEffect effect : playerEffects) {
                player.removePotionEffect(effect.getType());
            }

            // Remove the player entry from the activeEffects map
            activeEffects.remove(playerId);
        }
    }

}
