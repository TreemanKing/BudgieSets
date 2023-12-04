package net.tree.budgiesets.processor.effects;

import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class HealthProcessor implements EffectProcessor {

    private static final String AMOUNT_KEY = "Amount";

    @Override
    public void processEffect(List<?> healths, Player player) {
        for (Object health : healths) {
            if (health instanceof Map<?, ?>) {
                Map<?, ?> healthMap = (Map<?, ?>) health;
                if (validateHealthConfig(healthMap)) {
                    double amount = (double) healthMap.get(AMOUNT_KEY);
                    List<String> conditions = (List<String>) healthMap.get("Conditions");

                    if (checkConditions(conditions, player)) {
                        setHealth(player, amount);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    Bukkit.getLogger().warning("Invalid health configuration: " + healthMap);
                }

            }
        }
    }

    private void setHealth(@NotNull Player player, double amount) {
        player.setHealth(player.getHealth() + amount);
    }

    private boolean validateHealthConfig(Map<?, ?> healthMap) {
        return healthMap.containsKey(AMOUNT_KEY) && healthMap.get(AMOUNT_KEY) instanceof Double;
    }
}
