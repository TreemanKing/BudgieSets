package com.github.treemanking.budgiesets.events;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface EventProcessor {
    void process(Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap);

}
