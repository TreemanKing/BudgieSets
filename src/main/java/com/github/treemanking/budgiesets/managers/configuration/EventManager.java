package com.github.treemanking.budgiesets.managers.configuration;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.events.EventProcessor;
import com.github.treemanking.budgiesets.events.EventProcessorFactory;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The EventManager class is responsible for registering events defined in the configuration file.
 */
public class EventManager {

    /**
     * Registers armor-related events specified in the configuration file.
     *
     * @param fileConfiguration the FileConfiguration instance representing the configuration file
     * @param plugin the BudgieSets plugin instance
     * @param playerEquipStatusHashMap a map storing players' armor set equip status
     */
    public void registerArmorEvents(@NotNull FileConfiguration fileConfiguration, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap) {
        @NotNull List<Map<?, ?>> eventsList = fileConfiguration.getMapList("Events");
        for (Map<?, ?> event : eventsList) {
            processEvent(event, plugin, playerEquipStatusHashMap);
        }
    }

    /**
     * Processes an event defined in the configuration file.
     *
     * @param eventMap the map representing the event
     * @param plugin the BudgieSets plugin instance
     * @param playerEquipStatusHashMap a map storing players' armor set equip status
     */
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
