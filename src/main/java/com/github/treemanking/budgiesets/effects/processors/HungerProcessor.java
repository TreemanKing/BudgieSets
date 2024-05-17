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

public class HungerProcessor implements EffectProcessor {

    private final String AMOUNT_KEY = "Amount";

    @Override
    public void processEffect(List<?> hungers, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object hunger : hungers) {
            if (hunger instanceof Map<?, ?> hungerMap) {
                if (validateHungerConfig(hungerMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    int amount = (int) hungerMap.get(AMOUNT_KEY);
                    List<String> conditions = (List<String>) hungerMap.get("Conditions");

                    if (checkConditions(conditions, player)) {
                        setHunger(player, amount);
                    }


                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid hunger configuration: " + hungerMap);
                }

            }
        }
    }

    private void setHunger(@NotNull Player player, int amount) {
        player.setFoodLevel(player.getFoodLevel() + amount);
    }

    private boolean validateHungerConfig(Map<?, ?> hungerMap) {
        return hungerMap.containsKey(AMOUNT_KEY) && hungerMap.get(AMOUNT_KEY) instanceof Integer;
    }
}
