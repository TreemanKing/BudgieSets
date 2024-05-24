package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.utilities.ProcessorKeys;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Map;

/**
 * The BurnProcessor class processes burn effects for players based on their armor equip status.
 */
public class BurnProcessor implements EffectProcessor, ProcessorKeys {

    /**
     * Processes burn effects based on the provided configuration.
     *
     * @param burns       A list of burn configurations.
     * @param player      The player to apply the effects to.
     * @param equipStatus The equip status of the player's armor.
     * @param event       The event triggering the effect.
     */
    @Override
    public void processEffect(List<?> burns, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object burn : burns) {
            if (burn instanceof Map<?, ?> burnMap) {
                if (validateBurnConfig(burnMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)
                            || equipStatus.equals(ArmorSetListener.EquipStatus.NULL)) return;

                    String actionType = getConfigValue(burnMap, ACTION_TYPE_KEY, String.class);
                    Integer time = getConfigValue(burnMap, TIME_KEY, Integer.class, 5);

                    if (time != null && time >= 0) {
                        actionPotionEffect(player, actionType, time);
                    }
                } else {
                    // Log an error about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid burn configuration: " + burnMap);
                }
            }
        }
    }

    /**
     * Applies the burn effect to the player based on the action type and duration.
     *
     * @param player     The player to apply the effect to.
     * @param actionType The type of action to perform ("Add" or "Remove").
     * @param time       The duration of the effect in seconds.
     */
    private void actionPotionEffect(Player player, String actionType, int time) {
        if ("Add".equalsIgnoreCase(actionType)) {
            player.setFireTicks(time * 20);
        } else if ("Remove".equalsIgnoreCase(actionType)) {
            player.setFireTicks(0);
        } else {
            BudgieSets.getBudgieSets().getLogger().warning("Invalid action type: " + actionType + ". You must use 'Add' or 'Remove'.");
        }
    }

    /**
     * Validates the burn configuration.
     *
     * @param burnMap The burn configuration map.
     * @return True if the configuration is valid, false otherwise.
     */
    private boolean validateBurnConfig(Map<?, ?> burnMap) {
        return burnMap.containsKey(ACTION_TYPE_KEY) && burnMap.get(ACTION_TYPE_KEY) instanceof String;
    }
}
