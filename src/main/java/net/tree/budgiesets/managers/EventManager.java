package net.tree.budgiesets.managers;

import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.processor.factory.EventProcessorFactory;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class EventManager {

    public void registerArmorEvents(@NotNull FileConfiguration fileConfiguration, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        @NotNull List<Map<?, ?>> eventsList = fileConfiguration.getMapList("Events");
        for (Map<?, ?> event : eventsList) {
            processEvent(event, plugin, playerEquipStatusHashMap);
        }
    }

    private void processEvent(Map<?, ?> eventMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        if (eventMap != null) {
            Set<? extends Map.Entry<?, ?>> entrySet = eventMap.entrySet();
            if (entrySet.size() == 1) {
                Map.Entry<?, ?> entry = entrySet.iterator().next();
                String eventType = (String) entry.getKey();
                EventProcessor processor = EventProcessorFactory.createProcessor(eventType);
                if (processor != null) {
                    Object value = entry.getValue();
                    if (value instanceof Map) {
                        processor.process((Map<?, ?>) value, plugin, playerEquipStatusHashMap);
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





