package net.tree.budgiesets.processor.interfaces;

import net.tree.budgiesets.BudgieSets;
import org.bukkit.entity.Player;
import java.util.Map;

public interface EventProcessor {
    void process(Map<?, ?> effectsMap, Player player, BudgieSets plugin);

}
