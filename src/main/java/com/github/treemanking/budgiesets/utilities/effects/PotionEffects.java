package com.github.treemanking.budgiesets.utilities.effects;

import com.github.treemanking.budgiesets.BudgieSets;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface PotionEffects {

    /**
     * A map storing PotionEffect instances for players identified by their unique UUIDs.
     *
     * <p>The key of the map is a {@link UUID} which uniquely identifies a player.
     * The value is a {@link PotionEffect} which represents the potion effect applied to the player.
     *
     * <p>This map can be used to track active potion effects for players in a game,
     * enabling the application, removal, or querying of potion effects based on player UUIDs.
     *
     * <p>Example usage:
     * <pre>{@code
     * // Adding a potion effect to a player
     * UUID playerUUID = player.getUniqueId();
     * PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, duration, amplifier);
     * potionEffects.put(playerUUID, effect);
     *
     * // Retrieving a potion effect for a player
     * PotionEffect activeEffect = potionEffects.get(playerUUID);
     * }</pre>
     */
    Map<UUID, List<PotionEffect>> potionEffects = new HashMap<>();

    /**
     * Gets the map storing potion effects for players.
     *
     * @return the map of potion effects
     */
    default Map<UUID, List<PotionEffect>> getPotionEffects() {
        return potionEffects;
    }

    /**
     * Applies a specified potion effect to a player.
     *
     * <p>This method takes in a player, the duration of the effect, the name of the effect,
     * the amplifier, and whether the effect is ambient and has particles. It applies the
     * effect to the player and adds it to the active effects map. If the effect name is
     * invalid, a warning is logged.</p>
     *
     * @param player the player to whom the potion effect will be applied
     * @param duration the duration of the potion effect in seconds (default is 5 seconds if 0 is provided)
     * @param effectName the name of the potion effect to be applied
     * @param amplifier the strength of the potion effect
     * @param ambient whether the potion effect is ambient
     * @param particles whether the potion effect has particles
     */
    default void applyPotionEffect(@NotNull Player player, int duration, @NotNull String effectName, int amplifier, boolean ambient, boolean particles) {
        PotionEffectType effectType = PotionEffectType.getByName(effectName.toUpperCase());

        if (duration == 0) duration = 5;

        if (effectType != null) {
            // Apply the permanent potion effect
            PotionEffect effect = new PotionEffect(effectType, duration * 20, amplifier, ambient, particles);
            if (duration == PotionEffect.INFINITE_DURATION) {
                // Check if the player already has effects
                if (getPotionEffects().containsKey(player.getUniqueId())) {
                    getPotionEffects().get(player.getUniqueId()).add(effect);
                } else {
                    // If not, create a new list with the current effect
                    List<PotionEffect> effects = new ArrayList<>();
                    effects.add(effect);
                    getPotionEffects().put(player.getUniqueId(), effects);
                }
            }
            player.addPotionEffect(effect);
        } else {
            BudgieSets.getBudgieSets().getLogger().warning("Invalid potion effect name: " + effectName);
        }
    }

    /**
     * Removes all custom potion effects from the specified player.
     *
     * <p>This method retrieves the unique identifier (UUID) of the player and
     * fetches the list of custom potion effects associated with that UUID. If
     * any custom potion effects are found, they are removed from the player.
     * Finally, the player entry is removed from the active effects map.</p>
     *
     * <p>Used specifically for Permanent Potion</p>
     *
     * @param player the player from whom to remove all custom potion effects
     */
    default void removePotionEffects(Player player) {
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

    /**
     * Removes a specific potion effect from the specified player.
     *
     * <p>This method takes in a player and the name of a potion effect. It retrieves the
     * potion effect type by name and removes it from the player if the player currently
     * has any active potion effects of that type.</p>
     *
     * @param player the player from whom to remove the specified potion effect
     * @param potionEffect the name of the potion effect to be removed
     */
    default void removePotionEffects(Player player, String potionEffect) {
        PotionEffectType effectType = PotionEffectType.getByName(potionEffect.toUpperCase());

        if (player.getActivePotionEffects().isEmpty()) return;
        if (effectType == null) return;

        player.removePotionEffect(effectType);
    }
}
