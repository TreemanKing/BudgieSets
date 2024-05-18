package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.utilities.ProcessorKeys;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Map;

public class BurnProcessor implements EffectProcessor, ProcessorKeys {

    @Override
    public void processEffect(List<?> burns, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object burn : burns) {
            if (burn instanceof Map<?, ?> burnMap) {
                if (validateBurnConifg(burnMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;
                    String actionType = (String) burnMap.get(ACTION_TYPE_KEY);
                    int time;

                    if (burnMap.get(TIME_KEY) == null) {
                        time = 5;
                    } else {
                        time = (int) burnMap.get(TIME_KEY);
                    }

                    List<String> conditions = (List<String>) burnMap.get("Conditions");

                    if (checkConditions(conditions, player) && time >= 0) {
                        actionPotionEffect(player, actionType, time);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid burn configuration: " + burnMap);
                }
            }
        }
    }

    private void actionPotionEffect(Player player, String actionType, int time) {
        if (actionType.equalsIgnoreCase("Add")) {
            player.setFireTicks(time * 20);
        } else if (actionType.equalsIgnoreCase("Remove")) {
            player.setFireTicks(0);
        } else {
            BudgieSets.getBudgieSets().getLogger().warning("You must have an action type of add or remove.");
        }
    }

    private boolean validateBurnConifg(Map<?, ?> burnMap) {
        return burnMap.containsKey(TIME_KEY)
                && burnMap.containsKey(ACTION_TYPE_KEY)
                && burnMap.get(TIME_KEY) instanceof Integer
                && burnMap.get(ACTION_TYPE_KEY) instanceof String;
    }
}
