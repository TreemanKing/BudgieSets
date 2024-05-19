package com.github.treemanking.budgiesets.effects;

import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.utilities.Processor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The EffectProcessor interface defines the methods required to process effects
 * for players based on their equipped armor sets, including condition checks
 * utilizing the PlaceholderAPI.
 */
public interface EffectProcessor extends Processor {

    /**
     * Processes a given effect on a player based on their equip status and the associated event.
     *
     * @param effect the list of effects to process
     * @param player the player on whom the effect is to be processed
     * @param equipStatus the equip status of the player's armor set
     * @param event the event triggering the effect
     */
    void processEffect(List<?> effect, Player player, ArmorSetListener.EquipStatus equipStatus, Event event);

    /**
     * Converts a hexadecimal color string to its red, green, and blue components.
     *
     * @param hex the hexadecimal color string (e.g., "#FF5733" or "FF5733")
     * @return an array of three integers representing the red, green, and blue components
     * @throws IllegalArgumentException if the hex string is invalid
     */
    default int[] convertHexToRGB(String hex) throws IllegalArgumentException {
        if (hex == null) throw new IllegalArgumentException("Hex cannot be null");
        // Remove the leading '#' if it's present
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        // Check if the hex string is valid
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Invalid hex color string. Must be 6 characters long.");
        }

        try {
            // Parse the hex string to an integer
            int rgb = Integer.parseInt(hex, 16);

            // Extract the red, green, and blue components
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;

            return new int[]{red, green, blue};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex color string. Contains non-hex characters.", e);
        }
    }

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
}
