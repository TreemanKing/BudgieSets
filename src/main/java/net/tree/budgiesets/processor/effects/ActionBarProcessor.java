package net.tree.budgiesets.processor.effects;

import net.tree.budgiesets.eventlisteners.ArmorSetListener;
import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class ActionBarProcessor implements EffectProcessor {


    private static final String TEXT_KEY = "Text";

    @Override
    public void processEffect(List<?> actionBars, Player player, ArmorSetListener.EquipStatus equipStatus) {
        for (Object actionBar : actionBars) {
            if (actionBar instanceof Map<?, ?>) {
                Map<?, ?> actionBarMap = (Map<?, ?>) actionBar;
                if (validateActionBarConifg(actionBarMap)) {
                    if (equipStatus.equals(ArmorSetListener.EquipStatus.NOT_EQUIPPED)) return;

                    String text = (String) actionBarMap.get(TEXT_KEY);
                    List<String> conditions = (List<String>) actionBarMap.get("Conditions");

                    if (checkConditions(conditions, player) && text != null) {
                        applyActionBar(player, text);
                    }
                } else {
                    // Log an error or inform the user about the invalid configuration
                    Bukkit.getLogger().warning("Invalid action bar configuration: " + actionBarMap);
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
