package com.github.treemanking.budgiesets.events;

import com.github.treemanking.budgiesets.utilities.EquipStatus;
import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.configuration.EffectsManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The EventProcessor interface defines a contract for processing events with associated effects.
 *
 * <p>Implementations are resolved at runtime by
 * {@link com.github.treemanking.budgiesets.events.EventProcessorFactory}. Shared helper
 * behaviour lives in the static utilities ({@code ConfigUtils}, {@code ConditionUtils})
 * rather than being inherited from this interface.</p>
 */
public interface EventProcessor {

    /**
     * Processes the effects associated with an event.
     *
     * @param armorSetName the name of the armor set
     * @param effectsMap the map containing the effects to be processed
     * @param plugin the BudgieSets plugin instance
     * @param playerEquipStatusHashMap a map storing players' armor set equip status
     */
    void process(String armorSetName, Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, EquipStatus> playerEquipStatusHashMap);

    EffectsManager effectManager = new EffectsManager();
}
