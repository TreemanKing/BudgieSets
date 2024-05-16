package net.tree.budgiesets.processor.interfaces;

import me.clip.placeholderapi.PlaceholderAPI;
import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import java.util.List;
import java.util.Objects;

public interface EffectProcessor {
    void processEffect(List<?> effect, Player player, ArmorSetListener.EquipStatus equipStatus, Event event);

    // Check Cooldown


    // Condition Checker using Placeholder API
    default boolean checkConditions(List<String> conditions, Player player) {
        // Don't check conditions if PlaceholderAPI is not enabled
        if (!Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI")).isEnabled()) {
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
        return true; // If no conditions or conditions are met
    }

    default boolean checkCondition(String condition, Player player) {
        // Remove whitespaces before splitting the condition
        String trimmedCondition = condition.replaceAll("\\s+", "");

        // Split the condition into placeholder and value parts
        String[] parts = trimmedCondition.split("<=|>=|<|>|==");

        if (parts.length == 2) {
            // Trim to remove any leading/trailing whitespaces
            String placeholder = parts[0].trim();
            String actualValue = PlaceholderAPI.setPlaceholders(player, placeholder);
            String restOfCondition = parts[1].trim();


            // Preserve the operator
            String operator = condition.substring(placeholder.length(), condition.length() - restOfCondition.length()).trim();

            return performComparison(actualValue, operator, restOfCondition);
        } else {
            // Log a warning or handle the case where the condition format is invalid
            Bukkit.getLogger().warning("Invalid condition format: " + condition);
            return false;
        }
    }

    default boolean performComparison(String actualValue, String operator, String expectedValue) {
        if (isNumeric(actualValue) && isNumeric(expectedValue)) {
            // Numeric comparison
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
            // Boolean comparison
            boolean actualBoolean = Boolean.parseBoolean(actualValue);
            boolean expectedBoolean = Boolean.parseBoolean(expectedValue);

            return actualBoolean == expectedBoolean;
        } else {
            // String comparison
            return actualValue.equals(expectedValue);
        }
    }

    default boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    default boolean isBoolean(String str) {
        return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
    }
}
