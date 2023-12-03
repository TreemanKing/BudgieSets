package net.tree.budgiesets.processor.events;

import net.tree.budgiesets.BudgieSets;
import net.tree.budgiesets.managers.EffectsManager;
import net.tree.budgiesets.processor.interfaces.EventProcessor;
import org.bukkit.entity.Player;
import java.util.Map;

public class EffectStaticProcessor implements EventProcessor {
    @Override
    public void process(Map<?, ?> event, Player player, BudgieSets plugin) {
        new EffectsManager(event, player);
    }
}



