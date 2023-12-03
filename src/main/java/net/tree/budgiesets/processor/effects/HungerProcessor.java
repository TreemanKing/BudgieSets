package net.tree.budgiesets.processor.effects;

import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class HungerProcessor implements EffectProcessor {
    @Override
    public void processEffect(List<?> hungers, Player player) {
        for (Object hunger : hungers) {
            if (hunger instanceof Map<?, ?>) {
                Map<?, ?> hungerMap = (Map<?, ?>) hunger;

                int amount = (int) hungerMap.get("Amount");
                List<String> conditions = (List<String>) hungerMap.get("Conditions");

                if (checkConditions(conditions, player)) {
                    setHunger(player, amount);
                }
            }
        }
    }

    private void setHunger(@NotNull Player player, @NotNull int amount) {
        player.setFoodLevel(player.getFoodLevel() + amount);
    }
}
