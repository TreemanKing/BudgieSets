package com.github.treemanking.budgiesets.effects.processors;

import com.github.treemanking.budgiesets.BudgieSets;
import com.github.treemanking.budgiesets.utilities.ProcessorKeys;
import com.github.treemanking.budgiesets.managers.armorsets.ArmorSetListener;
import com.github.treemanking.budgiesets.effects.EffectProcessor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class HealthProcessor implements EffectProcessor, ProcessorKeys {

    @Override
    public void processEffect(List<?> healths, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object health : healths) {
            if (health instanceof Map<?, ?> healthMap) {
                if (validateHealthConfig(healthMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    double amount = (double) healthMap.get(AMOUNT_KEY);
                    List<String> conditions = (List<String>) healthMap.get("Conditions");

                    if (checkConditions(conditions, player)) {
                        setHealth(player, amount);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    BudgieSets.getBudgieSets().getLogger().warning("Invalid health configuration: " + healthMap);
                }
            }
        }
    }

    private void setHealth(@NotNull Player player, double amount) {
        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();

        if (amount < 0) {
            // Handle case where amount is negative (health is being subtracted)
            if (currentHealth + amount < 0) {
                player.setHealth(0);
            } else {
                player.setHealth(Math.max(currentHealth + amount, 0));
            }
        } else if (amount > 0) {
            // Handle case where amount is positive (health is being added)
            double newHealth = currentHealth + amount;
            player.setHealth(Math.min(newHealth, maxHealth));
        }
    }

    private boolean validateHealthConfig(Map<?, ?> healthMap) {
        return healthMap.containsKey(AMOUNT_KEY) && healthMap.get(AMOUNT_KEY) instanceof Double;
    }
}
