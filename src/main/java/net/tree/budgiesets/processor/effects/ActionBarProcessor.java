package net.tree.budgiesets.processor.effects;

import net.tree.budgiesets.processor.interfaces.EffectProcessor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class ActionBarProcessor implements EffectProcessor {
    @Override
    public void processEffect(List<?> actionBars, Player player) {
        for (Object actionBar : actionBars) {
            if (actionBar instanceof Map<?, ?>) {
                Map<?, ?> actionBarMap = (Map<?, ?>) actionBar;

                String text = (String) actionBarMap.get("Text");
                List<String> conditions = (List<String>) actionBarMap.get("Conditions");

                if (checkConditions(conditions, player) && text != null) {
                    applyActionBar(player, text);
                }
            }
        }

    }

    private void applyActionBar(@NotNull Player player, @NotNull String actionBarText) {
        player.sendActionBar('&', actionBarText);
    }
}
