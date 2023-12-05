package net.tree.budgiesets.processor.interfaces;

import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface EventProcessor {
    void process(Map<?, ?> effectsMap, BudgieSets plugin, HashMap<UUID, ArmorSetListener.EquipStatus> playerEquipStatusHashMap);

}
