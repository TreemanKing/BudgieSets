package net.tree.budgiesets.managers;

import net.tree.budgiesets.processor.factory.EventProcessorFactory;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class EventManager {

    public EventManager(Player player, FileConfiguration fileConfiguration) {
        applyEventsOnEquip(player, fileConfiguration);
    }

    private void applyEventsOnEquip(Player player, FileConfiguration fileConfiguration) {
        @NotNull List<Map<?, ?>> eventsList = fileConfiguration.getMapList("Events");
        for (Map<?, ?> event : eventsList) {
            processEvent(event, player);
        }
    }

    private void processEvent(Map<?, ?> eventMap, Player player) {
        if (eventMap != null) {
            Set<? extends Map.Entry<?, ?>> entrySet = eventMap.entrySet();
            if (entrySet.size() == 1) {
                Map.Entry<?, ?> entry = entrySet.iterator().next();
                String eventType = (String) entry.getKey();
                EventProcessor processor = EventProcessorFactory.createProcessor(eventType);
                if (processor != null) {
                    Object value = entry.getValue();
                    if (value instanceof Map) {
                        processor.process((Map<?, ?>) value, player);
                    } else {
                        Bukkit.getLogger().warning("Invalid event structure found: " + eventMap);
                    }
                } else {
                    Bukkit.getLogger().warning("Invalid event type: " + eventType);
                }
            } else {
                Bukkit.getLogger().warning("Invalid event structure found: " + eventMap);
            }
        }
    }
}





