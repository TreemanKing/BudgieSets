package com.github.treemanking.budgiesets.events;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.utilities.Processor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The EventProcessor interface defines a contract for processing events with associated effects.
 */
public interface EventProcessor extends Processor {

    /**
     * Processes the effects associated with an event.
     *
     * @param armorSetName the name of the armor set
     * @param effectsMap the map containing the effects to be processed
     * @param plugin the BudgieSets plugin instance
     * @param playerEquipStatusHashMap a map storing players' armor set equip status
     */
    void process(String armorSetName, Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap);


}
