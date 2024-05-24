package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.PlayerEffectProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

/**
 * The ActionBarProcessor class processes action bar effects for players based on their armor equip status.
 */
public class ActionBarProcessor implements PlayerEffectProcessor {

    /**
     * Processes action bar effects based on the provided configuration.
     *
     * @param actionBars  A list of action bar configurations.
     * @param player      The player to apply the effects to.
     * @param equipStatus The equip status of the player's armor.
     * @param event       The event triggering the effect.
     */
    @Override
    public void processEffect(List<?> actionBars, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object actionBar : actionBars) {
            if (actionBar instanceof Map<?, ?> actionBarMap) {
                if (validateActionBarConfig(actionBarMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)
                            || equipStatus.equals(ArmorSetListener.EquipStatus.NULL)) return;

                    String text = getConfigValue(actionBarMap, TEXT_KEY, String.class);

                    if (text != null) {
                        applyActionBar(player, text);
                    }
                } else {
                    // Log an error about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid action bar configuration: " + actionBarMap);
                }
            }
        }
    }

    /**
     * Applies the action bar effect to the player.
     *
     * @param player        The player to apply the effect to.
     * @param actionBarText The text to display in the action bar.
     */
    private void applyActionBar(@NotNull Player player, @NotNull String actionBarText) {
        player.sendActionBar('&', actionBarText);
    }

    /**
     * Validates the action bar configuration.
     *
     * @param actionBarMap The action bar configuration map.
     * @return True if the configuration is valid, false otherwise.
     */
    private boolean validateActionBarConfig(Map<?, ?> actionBarMap) {
        return actionBarMap.containsKey(TEXT_KEY);
    }
}
