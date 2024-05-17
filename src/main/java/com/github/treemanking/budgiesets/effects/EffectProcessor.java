package com.github.treemanking.budgiesets.effects;

import com.github.treemanking.budgiesets.managers.HookManager;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

/**
 * The EffectProcessor interface defines the methods required to process effects
 * for players based on their equipped armor sets, including condition checks
 * utilizing the PlaceholderAPI.
 */
public interface EffectProcessor {

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
     * Checks a list of conditions for a player using the PlaceholderAPI.
     *
     * @param conditions the list of conditions to check
     * @param player the player for whom the conditions are checked
     * @return true if all conditions are met or if conditions are null or PlaceholderAPI is not enabled, false otherwise
     */
    default boolean checkConditions(List<String> conditions, Player player) {
        if (HookManager.placeholderAPIEnabled) {
            Bukkit.getLogger().warning("PlaceholderAPI is not enabled on this server, conditions will not work!");
            return true;
        }

        if (conditions != null) {
            for (String condition : conditions) {
                if (!checkCondition(condition, player)) {
                    return false; // Condition not met
                }
            }
        }
        return true; // If no conditions or all conditions are met
    }

    /**
     * Checks a single condition for a player using the PlaceholderAPI.
     *
     * @param condition the condition to check
     * @param player the player for whom the condition is checked
     * @return true if the condition is met, false otherwise
     */
    default boolean checkCondition(String condition, Player player) {
        String trimmedCondition = condition.replaceAll("\\s+", "");
        String[] parts = trimmedCondition.split("<=|>=|<|>|==");

        if (parts.length == 2) {
            String placeholder = parts[0].trim();
            String actualValue = PlaceholderAPI.setPlaceholders(player, placeholder);
            String restOfCondition = parts[1].trim();
            String operator = condition.substring(placeholder.length(), condition.length() - restOfCondition.length()).trim();

            return performComparison(actualValue, operator, restOfCondition);
        } else {
            Bukkit.getLogger().warning("Invalid condition format: " + condition);
            return false;
        }
    }

    /**
     * Performs a comparison between an actual value and an expected value using the specified operator.
     *
     * @param actualValue the actual value obtained from the condition
     * @param operator the operator used for comparison (==, <, >, <=, >=)
     * @param expectedValue the expected value to compare against
     * @return true if the comparison is successful, false otherwise
     */
    default boolean performComparison(String actualValue, String operator, String expectedValue) {
        if (isNumeric(actualValue) && isNumeric(expectedValue)) {
            double actualNumeric = Double.parseDouble(actualValue);
            double expectedNumeric = Double.parseDouble(expectedValue);

            switch (operator) {
                case "==": return actualNumeric == expectedNumeric;
                case "<": return actualNumeric < expectedNumeric;
                case ">": return actualNumeric > expectedNumeric;
                case "<=": return actualNumeric <= expectedNumeric;
                case ">=": return actualNumeric >= expectedNumeric;
                default: return false; // Unsupported operator
            }
        } else if (isBoolean(actualValue) && isBoolean(expectedValue)) {
            boolean actualBoolean = Boolean.parseBoolean(actualValue);
            boolean expectedBoolean = Boolean.parseBoolean(expectedValue);

            return actualBoolean == expectedBoolean;
        } else {
            return actualValue.equals(expectedValue);
        }
    }

    /**
     * Checks if a given string is numeric.
     *
     * @param str the string to check
     * @return true if the string is numeric, false otherwise
     */
    default boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a given string represents a boolean value.
     *
     * @param str the string to check
     * @return true if the string is "true" or "false" (case-insensitive), false otherwise
     */
    default boolean isBoolean(String str) {
        return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
    }
}
