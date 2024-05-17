package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class BurnProcessor implements EffectProcessor {
    private final Object BURN_KEY = "Time";

    @Override
    public void processEffect(List<?> burns, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object burn : burns) {
            if (burn instanceof Map<?, ?>) {
                Map<?, ?> burnMap = (Map<?, ?>) burn;
                if (validateBurnConifg(burnMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    int time = (int) burnMap.get(BURN_KEY);
                    List<String> conditions = (List<String>) burnMap.get("Conditions");

                    if (checkConditions(conditions, player) && time >= 0) {
                        applyBurn(player, time);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid burn configuration: " + burnMap);
                }
            }
        }
    }

    private void applyBurn(@NotNull Player player, int seconds) {
        player.setFireTicks(seconds * 20);
    }

    private boolean validateBurnConifg(Map<?, ?> burnMap) {
        return burnMap.containsKey(BURN_KEY) && burnMap.get(BURN_KEY) instanceof Integer;
    }
}
