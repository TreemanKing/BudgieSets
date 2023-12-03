package net.tree.budgiesets.processor.effects;

import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class HealthProcessor implements EffectProcessor {

    @Override
    public void processEffect(List<?> healths, Player player) {
        for (Object health : healths) {
            if (health instanceof Map<?, ?>) {
                Map<?, ?> healthMap = (Map<?, ?>) health;

                double amount = (double) healthMap.get("Amount");
                List<String> conditions = (List<String>) healthMap.get("Conditions");

                if (checkConditions(conditions, player)) {
                    setHealth(player, amount);
                }
            }
        }
    }

    private void setHealth(@NotNull Player player, double amount) {
        player.setHealth(player.getHealth() + amount);
    }
}
