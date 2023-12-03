package net.tree.budgiesets.processor.interfaces;

import org.bukkit.entity.Player;

import java.util.Map;

public interface EventProcessor {
    void process(Map<?, ?> event, Player player);

}
