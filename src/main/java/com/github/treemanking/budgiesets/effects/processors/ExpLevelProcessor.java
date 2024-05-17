package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ExpLevelProcessor implements EffectProcessor {

    private static final String AMOUNT_KEY = "Amount";

    @Override
    public void processEffect(List<?> expLevels, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object expLevel : expLevels) {
            if (expLevel instanceof Map<?, ?>) {
                Map<?, ?> expLevelMap = (Map<?, ?>) expLevel;
                if (validateHealthConfig(expLevelMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    int amount = (int) expLevelMap.get(AMOUNT_KEY);
                    List<String> conditions = (List<String>) expLevelMap.get("Conditions");

                    if (checkConditions(conditions, player)) {
                        setExpLevel(player, amount);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    Bukkit.getLogger().warning("Invalid exp level configuration: " + expLevelMap);
                }
            }
        }
    }

    private void setExpLevel(@NotNull Player player, int amount) {
        int currentExpLevel = player.getLevel();

        if (amount < 0) {
            // Handle case where amount is negative (EXP Level is being subtracted)
            if (currentExpLevel + amount < 0) {
                player.setLevel(0);
            } else {
                player.setLevel(Math.max(currentExpLevel + amount, 0));
            }
        } else {
            player.setLevel(currentExpLevel + amount);
        }
    }

    private boolean validateHealthConfig(Map<?, ?> healthMap) {
        return healthMap.containsKey(AMOUNT_KEY) && healthMap.get(AMOUNT_KEY) instanceof Integer;
    }
}
