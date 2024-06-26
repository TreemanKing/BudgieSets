package com.github.treemanking.budgiesets.utilities;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.HookManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.*;

public interface Processor extends HookManager, ProcessorKeys {

    /**
     * Checks a list of conditions for a player using the PlaceholderAPI.
     *
     * @param map the map that contains the events to effect
     * @param player the player for whom the conditions are checked
     * @param cooldownMap a map storing cooldowns for each event
     * @return true if all conditions are met or if conditions are null or PlaceholderAPI is not enabled, false otherwise
     */
    default boolean checkMap(Map<?, ?> map, Player player, Map<UUID, Long> cooldownMap) {
        List<String> conditions = castToListOfString(map.get("Conditions"));
        Double chance = (Double) map.get("Chance");
        Integer cooldown = (Integer) map.get("Cooldown");

        // Check conditions
        if (conditions != null && !conditions.isEmpty()) {
            for (String condition : conditions) {
                if (!checkCondition(condition, player)) {
                    return false; // Condition not met
                }
            }
        }

        // Check chance
        if (chance != null) {
            double randomValue = Math.random(); // Generates a value between 0.0 and 1.0
            if (randomValue >= chance) {
                return false; // Chance not met
            }
        }

        // Check cooldown
        if (cooldown != null) {
            long currentTime = System.currentTimeMillis();
            Long lastUsedTime = cooldownMap.get(player.getUniqueId());
            if (lastUsedTime != null && (currentTime - lastUsedTime)/1000 < cooldown.longValue()) {
                return false; // Cooldown not met
            }
            cooldownMap.put(player.getUniqueId(), currentTime); // Update cooldown map with current time
        }

        return true; // All checks passed
    }

    /**
     * Checks a single condition for a player using the PlaceholderAPI.
     *
     * @param condition the condition to check
     * @param player the player for whom the condition is checked
     * @return true if the condition is met, false otherwise
     */
    default boolean checkCondition(String condition, Player player) {
        if (!isPlaceholderAPIEnabled()) {
            return true;
        }

        String trimmedCondition = condition.replaceAll("\\s+", "");
        String[] parts = trimmedCondition.split("<=|>=|<|>|==");

        if (parts.length == 2) {
            String placeholder = parts[0].trim();
            String actualValue = PlaceholderAPI.setPlaceholders(player, placeholder);
            String restOfCondition = parts[1].trim();
            String operator = condition.substring(placeholder.length(), condition.length() - restOfCondition.length()).trim();

            return performComparison(actualValue, operator, restOfCondition);
        } else {
            BudgieSets.getBudgieSets().getLogger().warning("Invalid condition format: " + condition);
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

            return switch (operator) {
                case "==" -> actualNumeric == expectedNumeric;
                case "<" -> actualNumeric < expectedNumeric;
                case ">" -> actualNumeric > expectedNumeric;
                case "<=" -> actualNumeric <= expectedNumeric;
                case ">=" -> actualNumeric >= expectedNumeric;
                default -> false; // Unsupported operator
            };
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

    /**
     * Casts an object to a List of Strings if all elements in the list are Strings.
     * <p>
     * This method checks whether the provided object is an instance of {@code List<?>}.
     * It then iterates over the elements of the list to ensure that all elements are of type {@code String}.
     * If the object is not a list or if any element in the list is not a string, an {@link IllegalArgumentException} is thrown.
     *
     * @param obj the object to be cast to a list of strings
     * @return the cast list of strings
     * @throws IllegalArgumentException if the provided object is not a list or if any element in the list is not a string
     */
    default List<String> castToListOfString(Object obj) {
        if (obj == null) return new ArrayList<>();
        if (!(obj instanceof List<?> list)) {
            throw new IllegalArgumentException("The provided object is not a List");
        }

        for (Object element : list) {
            if (!(element instanceof String)) {
                throw new IllegalArgumentException("The list contains non-string elements");
            }
        }

        @SuppressWarnings("unchecked")
        List<String> stringList = (List<String>) list;
        return stringList;
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
