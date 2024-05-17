package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class ActionBarProcessor implements EffectProcessor {

    private final String TEXT_KEY = "Text";

    @Override
    public void processEffect(List<?> actionBars, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object actionBar : actionBars) {
            if (actionBar instanceof Map<?, ?> actionBarMap) {
                if (validateActionBarConifg(actionBarMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    String text = (String) actionBarMap.get(TEXT_KEY);
                    List<String> conditions = (List<String>) actionBarMap.get("Conditions");

                    if (checkConditions(conditions, player) && text != null) {
                        applyActionBar(player, text);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid action bar configuration: " + actionBarMap);
                }
            }
        }
    }

    private void applyActionBar(@NotNull Player player, @NotNull String actionBarText) {
        player.sendActionBar('&', actionBarText);
    }

    private boolean validateActionBarConifg(Map<?, ?> actionBarMap) {
        return actionBarMap.containsKey(TEXT_KEY);
    }
}
