package com.github.treemanking.budgiesets.effects;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.utilities.Processor;
import com.github.treemanking.budgiesets.utilities.effects.PotionEffects;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Map;

/**
 * The EffectProcessor interface defines the methods required to process effects
 * for players based on their equipped armor sets, including condition checks
 * utilizing the PlaceholderAPI.
 */
public interface EffectProcessor extends Processor, PotionEffects {

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
     * Retrieves a configuration value from the map with detailed logging for errors.
     *
     * @param map  The configuration map.
     * @param key  The key to retrieve.
     * @param type The expected type of the value.
     * @param <T>  The type parameter.
     * @return The value if present and correct type, null otherwise.
     */
    default  <T> T getConfigValue(Map<?, ?> map, String key, Class<T> type) {
        return getConfigValue(map, key, type, null);
    }

    /**
     * Retrieves a configuration value from the map with detailed logging for errors, providing a default value if the key is missing or type is incorrect.
     *
     * @param map          The configuration map.
     * @param key          The key to retrieve.
     * @param type         The expected type of the value.
     * @param defaultValue The default value to use if the key is missing or type is incorrect.
     * @param <T>          The type parameter.
     * @return The value if present and correct type, otherwise the default value.
     */
    default  <T> T getConfigValue(Map<?, ?> map, String key, Class<T> type, T defaultValue) {
        if (!map.containsKey(key)) {
            BudgieSets.getBudgieSets().getLogger().warning("Missing configuration key: " + key);
            return defaultValue;
        }
        Object value = map.get(key);
        if (!type.isInstance(value)) {
            BudgieSets.getBudgieSets().getLogger().warning("Incorrect type for key: " + key + ". Expected: " + type.getSimpleName() + ", but got: " + (value == null ? "null" : value.getClass().getSimpleName()));
            return defaultValue;
        }
        return type.cast(value);
    }
}
