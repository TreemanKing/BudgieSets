package net.tree.budgiesets.processor;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface EventProcessor {
    void process(Map<String, Object> effects, Player player);
    String getEventType();

    static List<Map<String, Object>> getConfigSections(Map<String, Object> section) {
        List<Map<String, Object>> configSections = new ArrayList<>();
        for (String key : section.keySet()) {
            Object value = section.get(key);
            if (value instanceof Map) {
                configSections.add((Map<String, Object>) value);
            }
        }
        return configSections;
    }

    static boolean checkConditions(Map<String, Object> conditions, Player player) {
        if (conditions != null) {
            for (String placeholder : conditions.keySet()) {
                Object value = conditions.get(placeholder);
                if (value instanceof String) {
                    String expectedValue = (String) value;

                    // Use PlaceholderAPI to get the actual value for the placeholder
                    String actualValue = PlaceholderAPI.setPlaceholders(player, "%" + placeholder + "%");

                    // Compare the actual value with the expected value
                    if (!actualValue.equals(expectedValue)) {
                        return false; // Condition not met
                    }
                }
            }
        }
        return true; // If no conditions or conditions are met
    }
}
