package net.tree.budgiesets.processor.effects;

import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class HealthProcessor implements EffectProcessor {

    private static final String AMOUNT_KEY = "Amount";

    @Override
    public void processEffect(List<?> healths, Player player, ArmorSetListener.EquipStatus equipStatus, Event event) {
        for (Object health : healths) {
            if (health instanceof Map<?, ?>) {
                Map<?, ?> healthMap = (Map<?, ?>) health;
                if (validateHealthConfig(healthMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

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
