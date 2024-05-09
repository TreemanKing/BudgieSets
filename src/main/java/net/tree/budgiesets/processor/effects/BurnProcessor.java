package net.tree.budgiesets.processor.effects;

import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class BurnProcessor implements EffectProcessor {
    private static final Object BURN_KEY = "Time";

    @Override
    public void processEffect(List<?> burns, Player player, ArmorSetListener.EquipStatus equipStatus) {
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
                    Bukkit.getLogger().warning("Invalid burn configuration: " + burnMap);
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
