package net.tree.budgiesets.processor.effects;

import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class HungerProcessor implements EffectProcessor {

    private static final String AMOUNT_KEY = "Amount";

    @Override
    public void processEffect(List<?> hungers, Player player, ArmorSetListener.EquipStatus equipStatus) {
        for (Object hunger : hungers) {
            if (hunger instanceof Map<?, ?>) {
                Map<?, ?> hungerMap = (Map<?, ?>) hunger;
                if (validateHungerConfig(hungerMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    int amount = (int) hungerMap.get(AMOUNT_KEY);
                    List<String> conditions = (List<String>) hungerMap.get("Conditions");

                    if (checkConditions(conditions, player)) {
                        setHunger(player, amount);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    Bukkit.getLogger().warning("Invalid hunger configuration: " + hungerMap);
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
